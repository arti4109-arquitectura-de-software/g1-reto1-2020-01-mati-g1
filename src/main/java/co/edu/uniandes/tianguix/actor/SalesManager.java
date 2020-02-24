package co.edu.uniandes.tianguix.actor;

import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;
import co.edu.uniandes.tianguix.model.SaleOrderSaved;

/**
 * @author <a href="mailto:daniel.bellon@payulatam.com"> Daniel Bellón </a>
 * @since 1.0.0
 */
public class SalesManager extends AbstractBehavior<SaleOrderSaved> {

	public SalesManager(ActorContext<SaleOrderSaved> context) {

		super(context);
	}

	@Override public Receive<SaleOrderSaved> createReceive() {

		return null;
	}
}
