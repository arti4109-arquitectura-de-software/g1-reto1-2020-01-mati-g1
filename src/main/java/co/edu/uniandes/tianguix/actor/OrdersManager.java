package co.edu.uniandes.tianguix.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import co.edu.uniandes.tianguix.dao.OrderDaoMock;
import co.edu.uniandes.tianguix.model.ArrivedOrder;
import co.edu.uniandes.tianguix.model.OrderType;
import co.edu.uniandes.tianguix.model.SavedPurchase;
import co.edu.uniandes.tianguix.model.SavedSale;

/**
 * @author <a href="mailto:daniel.bellon@payulatam.com"> Daniel Bellón </a>
 * @since 1.0.0
 */
public class OrdersManager extends AbstractBehavior<ArrivedOrder> {

	private OrdersManager(ActorContext<ArrivedOrder> context) {

		super(context);
	}

	public static Behavior<ArrivedOrder> create() {

		return Behaviors.setup(OrdersManager::new);
	}

	@Override public Receive<ArrivedOrder> createReceive() {

		return newReceiveBuilder()
				.onMessage(ArrivedOrder.class, this::onArrivedOrder)
				.build();
	}

	private Behavior<ArrivedOrder> onArrivedOrder(ArrivedOrder arrivedOrder) {

		var dao = new OrderDaoMock();
		dao.saveOrder(arrivedOrder);

		if (arrivedOrder.getOrderType().equals(OrderType.SALE)) {
			var replayTo = getContext().spawn(SalesManager.create(), "salesManager");
			replayTo.tell(new SavedSale(arrivedOrder));
		} else {
			var replayTo = getContext().spawn(PurchasesManager.create(), "purchasesManager");
			replayTo.tell(new SavedPurchase(arrivedOrder));
		}

		return this;
	}
}
