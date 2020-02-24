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
public class PurchaseManager extends AbstractBehavior<PurchaseOrderSaved> {

	private PurchaseManager(ActorContext<PurchaseOrderSaved> context) {

		super(context);
	}

	public static Behavior<PurchaseOrderSaved> create() {

		return Behaviors.setup(PurchaseManager::new);
	}

	@Override public Receive<PurchaseOrderSaved> createReceive() {

		return newReceiveBuilder()
				.onMessage(PurchaseOrderSaved.class, this::onArrivedPurchaseOrder)
				.build();
	}

	private Behavior<PurchaseOrderSaved> onArrivedPurchaseOrder(PurchaseOrderSaved purchaseOrderSaved) {

		var ordersDao = new OrderDaoMock();
		var candidates = ordersDao.getPurchaseCandidates(purchaseOrderSaved);
		var candidatesRetrieved =
				new PurchaseCandidateRetrieved()
						.withPurchaseCandidates(candidates)
						.withSale(new OrderArrived()
										  .withOrderType(OrderType.PURCHASE)
										  .withAsset(purchaseOrderSaved.getAsset())
										  .withAssetAmount(purchaseOrderSaved.getAssetAmount()));

		purchaseOrderSaved.getReplayTo().tell(candidatesRetrieved);
		return this;
	}
}
