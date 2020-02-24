package co.edu.uniandes.tianguix.actor;

import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;
import co.edu.uniandes.tianguix.model.PurchaseOrderSaved;

/**
 * @author <a href="mailto:daniel.bellon@payulatam.com"> Daniel Bellón </a>
 * @since 1.0.0
 */
public class PurchaseManager extends AbstractBehavior<PurchaseOrderSaved> {

	public PurchaseManager(ActorContext<PurchaseOrderSaved> context) {

		super(context);
	}

	@Override public Receive<PurchaseOrderSaved> createReceive() {

		// on receive:
		return null;
	}
}
