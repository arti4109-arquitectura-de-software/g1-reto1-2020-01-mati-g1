package co.edu.uniandes.tianguix.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import co.edu.uniandes.tianguix.model.OrderArrived;
import co.edu.uniandes.tianguix.model.OrderType;
import co.edu.uniandes.tianguix.model.PurchaseOrderSaved;
import co.edu.uniandes.tianguix.model.SaleOrderSaved;

/**
 * @author <a href="mailto:daniel.bellon@payulatam.com"> Daniel Bellón </a>
 * @since 1.0.0
 */
public class OrdersManager extends AbstractBehavior<OrderArrived> {

	private OrdersManager(ActorContext<OrderArrived> context) {

		super(context);
	}

	public static Behavior<OrderArrived> create() {

		return Behaviors.setup(OrdersManager::new);
	}

	@Override public Receive<OrderArrived> createReceive() {

		return newReceiveBuilder()
				.onMessage(OrderArrived.class, this::onArrivedOrder)
				.build();
	}

	private Behavior<OrderArrived> onArrivedOrder(OrderArrived orderArrived) {
		// TODO: 23/02/20 save

		// evaluate if the arrived order is a sale or a purchase

		var savedOrder = orderArrived.getOrderType().equals(OrderType.SALE) ?
						 new SaleOrderSaved()
								 .withAsset(orderArrived.getAsset())
								 .withAssetAmount(orderArrived.getAssetAmount()) :
						 new PurchaseOrderSaved()
								 .withAsset(orderArrived.getAsset())
								 .withAssetAmount(orderArrived.getAssetAmount());

		var replayTo = getContext().spawn(SalesManager.create(), "salesManager");
		replayTo.tell(((SaleOrderSaved) savedOrder));
		//orderArrived.getReplyTo().tell(savedOrder);
		return this;
	}
}
