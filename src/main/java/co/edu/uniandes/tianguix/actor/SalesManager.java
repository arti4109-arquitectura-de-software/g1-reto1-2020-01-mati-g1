package co.edu.uniandes.tianguix.actor;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;
import co.edu.uniandes.tianguix.dao.OrderDaoMock;
import co.edu.uniandes.tianguix.model.CandidatesRetrieved;
import co.edu.uniandes.tianguix.model.OrderArrived;
import co.edu.uniandes.tianguix.model.SaleOrderSaved;

import java.util.Collection;

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

		Collection<OrderArrived> candidates = new OrderDaoMock().getSaleCandidates(saleOrderSaved);

		CandidatesRetrieved candidatesRetrieved = new CandidatesRetrieved()
			.withCandidates(candidates)
			.withOrderSaved(saleOrderSaved);

		saleOrderSaved.getReplayTo().tell(candidatesRetrieved);

		return this;
	}

}
