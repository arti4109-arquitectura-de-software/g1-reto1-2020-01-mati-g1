package co.edu.uniandes.tianguix.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import co.edu.uniandes.tianguix.dao.OrderDaoMock;
import co.edu.uniandes.tianguix.model.*;

/**
 * @author <a href="mailto:daniel.bellon@payulatam.com"> Daniel Bellón </a>
 * @since 1.0.0
 */
public class PurchasesManager extends AbstractBehavior<SavedPurchase> {

	private PurchasesManager(ActorContext<SavedPurchase> context) {

		super(context);
	}

	public static Behavior<SavedPurchase> create() {

		return Behaviors.setup(PurchasesManager::new);
	}

	@Override public Receive<SavedPurchase> createReceive() {

		return newReceiveBuilder()
				.onMessage(SavedPurchase.class, this::onArrivedPurchaseOrder)
				.build();
	}

	private Behavior<SavedPurchase> onArrivedPurchaseOrder(SavedPurchase savedPurchase) {

		var ordersDao = new OrderDaoMock();
		var candidates = ordersDao.getPurchaseCandidates(savedPurchase);
		var candidatesRetrieved =
				new PurchaseCandidateRetrieved()
						.withPurchaseCandidates(candidates)
						.withSale(savedPurchase.getArrivedOrder());

		var replayTo = getContext().spawn(MatchesManager.create(), "matchesManager");
		replayTo.tell(candidatesRetrieved);

		return this;
	}
}
