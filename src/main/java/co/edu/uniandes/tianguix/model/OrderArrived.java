package co.edu.uniandes.tianguix.model;

import akka.actor.typed.ActorRef;
import lombok.Data;

/**
 * @author <a href="mailto:daniel.bellon@payulatam.com"> Daniel Bellón </a>
 * @since 1.0.0
 */
@Data
public class OrderArrived {

	private OrderType orderType;
	private String asset;
	private Integer assetAmount;
	private ActorRef<OrderSaved> replyTo;

}
