package co.edu.uniandes.tianguix.route;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Scheduler;
import akka.actor.typed.javadsl.AskPattern;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.Route;
import akka.japi.function.Function;
import co.edu.uniandes.tianguix.model.Order;
import co.edu.uniandes.tianguix.model.OrderArrived;
import co.edu.uniandes.tianguix.model.OrderSaved;
import co.edu.uniandes.tianguix.model.OrderType;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.CompletionStage;

import static akka.http.javadsl.server.Directives.complete;
import static akka.http.javadsl.server.Directives.entity;
import static akka.http.javadsl.server.Directives.onSuccess;
import static akka.http.javadsl.server.Directives.path;
import static akka.http.javadsl.server.Directives.post;

/**
 * @author <a href="mailto:daniel.bellon@payulatam.com"> Daniel Bellón </a>
 * @since 1.0.0
 */
@Slf4j
public class TianguixRoutes {

	// -----------------------------------------------------------------------------------------------------------------
	// Attributes
	// -----------------------------------------------------------------------------------------------------------------

	private Duration askTimeout;
	private Scheduler scheduler;
	private ActorRef<OrderArrived> orderManagerActor;
	private Function<ActorRef<OrderSaved>, OrderArrived> messageFactory;

	// -----------------------------------------------------------------------------------------------------------------
	// Constructor
	// -----------------------------------------------------------------------------------------------------------------

	public TianguixRoutes(ActorSystem<?> system, ActorRef<OrderArrived> orderManagerActor) {

		this.orderManagerActor = orderManagerActor;
		this.scheduler = system.scheduler();
		this.askTimeout = system.settings().config().getDuration("tianguix.routes.ask-timeout");
	}

	// -----------------------------------------------------------------------------------------------------------------
	// Methods
	// -----------------------------------------------------------------------------------------------------------------

	public Route getTianguixRoutes() {

		return path("matches", this::matches);
	}

	public Route matches() {

		return post(() -> entity(
				Jackson.unmarshaller(Order.class),
				order -> onSuccess(
						saveOrder(order),
						performed -> complete(StatusCodes.CREATED, performed, Jackson.marshaller()))));
	}

	// -----------------------------------------------------------------------------------------------------------------
	// Helper Methods
	// -----------------------------------------------------------------------------------------------------------------

	private CompletionStage<OrderSaved> saveOrder(Order order) {

		messageFactory = ref -> new OrderArrived()
				.withAsset(order.getAsset())
				.withAssetAmount(order.getAmount())
				.withOrderType(OrderType.valueOf(order.getType()))
				.withReplyTo(ref);

		return AskPattern.ask(orderManagerActor, messageFactory, askTimeout, scheduler);
	}
}
