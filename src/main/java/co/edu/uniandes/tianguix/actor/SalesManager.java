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
public class SalesManager extends AbstractBehavior<SaleOrderSaved> {

	private SalesManager(ActorContext<SaleOrderSaved> context) {

		super(context);
	}

	public static Behavior<SaleOrderSaved> create() {

		return Behaviors.setup(SalesManager::new);
	}

	@Override public Receive<SaleOrderSaved> createReceive() {

		return newReceiveBuilder()
				.onMessage(SaleOrderSaved.class, this::onArrivedSaveOrder)
				.build();
	}

	private Behavior<SaleOrderSaved> onArrivedSaveOrder(SaleOrderSaved saleOrderSaved) {

		var candidates = new OrderDaoMock().getSaleCandidates(((SaleOrderSaved) saleOrderSaved));

		var candidatesRetrieved = new SaleCandidatesRetrieved()
				.withSaleCandidates(candidates)
				.withPurchase(new OrderArrived()
									  .withOrderType(OrderType.SALE)
									  .withAsset(((SaleOrderSaved) saleOrderSaved).getAsset())
									  .withAssetAmount(((SaleOrderSaved) saleOrderSaved).getAssetAmount())
							 );

		return this;
	}

}
