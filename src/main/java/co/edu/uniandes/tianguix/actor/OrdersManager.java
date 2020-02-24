package co.edu.uniandes.tianguix.actor;

import akka.actor.Props;
import akka.actor.typed.ActorRef;
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

import java.util.Optional;

/**
 * @author <a href="mailto:daniel.bellon@payulatam.com"> Daniel Bellón </a>
 * @since 1.0.0
 */
public class OrdersManager extends AbstractBehavior<ArrivedOrder> {

	private Optional<ActorRef<SavedSale>> saleReplayTo = Optional.empty();
	private Optional<ActorRef<SavedPurchase>> purchaseReplayTo = Optional.empty();

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
			var replayTo = saleReplayTo.orElseGet(() -> getContext().spawn(SalesManager.create(), "salesManager"));
			replayTo.tell(new SavedSale(arrivedOrder));
			saleReplayTo = Optional.of(replayTo);
		} else {
			var replayTo = purchaseReplayTo.orElseGet(() -> getContext().spawn(PurchasesManager.create(), "purchasesManager"));
			replayTo.tell(new SavedPurchase(arrivedOrder));
			purchaseReplayTo = Optional.of(replayTo);
		}

		return this;
	}
}
