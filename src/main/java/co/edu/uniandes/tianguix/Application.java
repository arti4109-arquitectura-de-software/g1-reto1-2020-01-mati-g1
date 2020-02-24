package co.edu.uniandes.tianguix;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.Adapter;
import akka.actor.typed.javadsl.Behaviors;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.server.Route;
import akka.stream.Materializer;
import co.edu.uniandes.tianguix.actor.OrdersManager;
import co.edu.uniandes.tianguix.route.TianguixRoutes;

import java.util.Objects;

/**
 * @author <a href="mailto:daniel.bellon@payulatam.com"> Daniel Bellón </a>
 * @since 1.0.0
 */
public class Application {

	static void startHttpServer(Route route, ActorSystem<?> system) {

		var actorSystem = Adapter.toClassic(system);
		var materializer = Materializer.matFromSystem(system);
		var http = Http.get(actorSystem);
		var routeFlow = route.flow(actorSystem, materializer);
		var host = ConnectHttp.toHost("localhost", 9090);
		var futureBinding = http.bindAndHandle(routeFlow, host, materializer);

		futureBinding.whenComplete(((binding, exception) -> {
			if (Objects.nonNull(binding)) {
				var address = binding.localAddress();
				system.log().info("Server online at http://{}:{}", address.getHostName(), address.getPort());
			} else {
				system.log().error("Failed to bind HTTP endpoint, terminating system", exception);
				system.terminate();
			}
		}));
	}

	public static void main(String[] args) {

		var rootBehaviour = Behaviors.setup(context -> {
			var ordersManagerActor = context.spawn(OrdersManager.create(), "OrdersManager");
			var routes = new TianguixRoutes(context.getSystem(), ordersManagerActor);
			startHttpServer(routes.getTianguixRoutes(), context.getSystem());
			return Behaviors.empty();
		});

		ActorSystem.create(rootBehaviour, "TianguixServer");
	}
}
