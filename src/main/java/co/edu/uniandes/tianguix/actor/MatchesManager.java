package co.edu.uniandes.tianguix.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import co.edu.uniandes.tianguix.model.CandidatesRetrieved;
import co.edu.uniandes.tianguix.model.MatchedOrder;
import co.edu.uniandes.tianguix.model.Order;
import co.edu.uniandes.tianguix.model.OrderType;
import co.edu.uniandes.tianguix.model.PurchaseCandidateRetrieved;
import co.edu.uniandes.tianguix.model.SaleCandidatesRetrieved;

import java.util.ArrayList;

/**
 * @author <a href="mailto:daniel.bellon@payulatam.com"> Daniel Bellón </a>
 * @since 1.0.0
 */
public class MatchesManager extends AbstractBehavior<CandidatesRetrieved> {

	public static Behavior<CandidatesRetrieved> create() {

		return Behaviors.setup(MatchesManager::new);
	}

	public MatchesManager(ActorContext<CandidatesRetrieved> context) {

		super(context);
	}

	@Override public Receive<CandidatesRetrieved> createReceive() {

		return newReceiveBuilder()
				.onMessage(CandidatesRetrieved.class, this::onCandidatesRetrieved)
				.build();
	}

	private Behavior<CandidatesRetrieved> onCandidatesRetrieved(CandidatesRetrieved candidatesRetrieved) {

		if (candidatesRetrieved instanceof SaleCandidatesRetrieved) {
			var candidates = (SaleCandidatesRetrieved) candidatesRetrieved;
			var order = new Order()
					.withType(OrderType.SALE.name())
					.withAmount(candidates.getPurchase().getAssetAmount())
					.withAsset(candidates.getPurchase().getAsset());

			candidates.getPurchase()
					  .getReplyTo()
					  .tell(new MatchedOrder(order, new ArrayList<>()));
		} else {
			((PurchaseCandidateRetrieved) candidatesRetrieved).getSale().getReplyTo().tell(new MatchedOrder());
		}
		return this;
	}
}
