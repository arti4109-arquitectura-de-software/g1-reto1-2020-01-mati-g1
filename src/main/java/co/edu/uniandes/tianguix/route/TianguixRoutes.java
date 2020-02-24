package co.edu.uniandes.tianguix.route;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Scheduler;
import akka.actor.typed.javadsl.AskPattern;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.Route;
import akka.japi.function.Function;
import co.edu.uniandes.tianguix.model.ArrivedOrder;
import co.edu.uniandes.tianguix.model.MatchedOrder;
import co.edu.uniandes.tianguix.model.Order;
import co.edu.uniandes.tianguix.model.OrderType;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.CompletionStage;

import static akka.http.javadsl.server.Directives.*;

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
	private ActorRef<ArrivedOrder> orderManagerActor;

	// -----------------------------------------------------------------------------------------------------------------
	// Constructor
	// -----------------------------------------------------------------------------------------------------------------

	public TianguixRoutes(ActorSystem<?> system, ActorRef<ArrivedOrder> orderManagerActor) {

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

	private CompletionStage<MatchedOrder> saveOrder(Order order) {

		return AskPattern.ask(orderManagerActor, getMessageFactory(order), askTimeout, scheduler);
	}

	private Function<ActorRef<MatchedOrder>, ArrivedOrder> getMessageFactory(Order order) {

		return ref -> new ArrivedOrder()
				.withAsset(order.getAsset())
				.withAssetAmount(order.getAmount())
				.withOrderType(OrderType.valueOf(order.getType()))
				.withReplyTo(ref);
	}
}
