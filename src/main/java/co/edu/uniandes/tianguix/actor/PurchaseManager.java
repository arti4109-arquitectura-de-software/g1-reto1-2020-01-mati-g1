package co.edu.uniandes.tianguix.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;
import co.edu.uniandes.tianguix.dao.OrderDaoMock;
import co.edu.uniandes.tianguix.dao.OrdersDao;
import co.edu.uniandes.tianguix.model.CandidatesRetrieved;
import co.edu.uniandes.tianguix.model.OrderArrived;
import co.edu.uniandes.tianguix.model.PurchaseOrderSaved;

import java.util.Collection;

/**
 * @author <a href="mailto:daniel.bellon@payulatam.com"> Daniel Bellón </a>
 * @since 1.0.0
 */
public class PurchaseManager extends AbstractBehavior<PurchaseOrderSaved> {

	public PurchaseManager(ActorContext<PurchaseOrderSaved> context) {

		super(context);
	}

	@Override public Receive<PurchaseOrderSaved> createReceive() {

		return newReceiveBuilder()
			.onMessage(PurchaseOrderSaved.class, this::onArrivedPurchaseOrder)
			.build();
	}

	private Behavior<PurchaseOrderSaved> onArrivedPurchaseOrder(PurchaseOrderSaved purchaseOrderSaved) {

		OrdersDao ordersDao = new OrderDaoMock();
		Collection<OrderArrived> candidates = ordersDao.getPurchaseCandidates(purchaseOrderSaved);

		CandidatesRetrieved candidatesRetrieved =
			new CandidatesRetrieved().withCandidates(candidates).withOrderSaved(purchaseOrderSaved);

		purchaseOrderSaved.getReplayTo().tell(candidatesRetrieved);
		return this;
	}
}
