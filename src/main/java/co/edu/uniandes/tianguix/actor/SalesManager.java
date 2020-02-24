package co.edu.uniandes.tianguix.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;
import co.edu.uniandes.tianguix.dao.OrderDaoMock;
import co.edu.uniandes.tianguix.model.*;

/**
 * @author <a href="mailto:daniel.bellon@payulatam.com"> Daniel Bellón </a>
 * @since 1.0.0
 */
public class SalesManager extends AbstractBehavior<SaleOrderSaved> {

	public SalesManager(ActorContext<SaleOrderSaved> context) {

		super(context);
	}

	@Override public Receive<SaleOrderSaved> createReceive() {

		return newReceiveBuilder()
			.onMessage(SaleOrderSaved.class, this::onArrivedSaveOrder)
			.build();
	}

	private Behavior<SaleOrderSaved> onArrivedSaveOrder(SaleOrderSaved saleOrderSaved) {

		var candidates = new OrderDaoMock().getSaleCandidates(saleOrderSaved);

		CandidatesRetrieved candidatesRetrieved = new SaleCandidatesRetrieved()
			.withSaleCandidates(candidates)
			.withPurchase(new OrderArrived()
				.withOrderType(OrderType.SALE)
				.withAsset(saleOrderSaved.getAsset())
				.withAssetAmount(saleOrderSaved.getAssetAmount())
			);

		saleOrderSaved.getReplayTo().tell(candidatesRetrieved);

		return this;
	}

}
