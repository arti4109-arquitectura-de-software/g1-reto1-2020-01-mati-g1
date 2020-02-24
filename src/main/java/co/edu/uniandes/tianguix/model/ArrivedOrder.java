package co.edu.uniandes.tianguix.model;

import akka.actor.typed.ActorRef;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/**
 * @author <a href="mailto:daniel.bellon@payulatam.com"> Daniel Bellón </a>
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArrivedOrder {

	@With private OrderType orderType;
	@With private String asset;
	@With private Integer assetAmount;
	@With private ActorRef<MatchedOrder> replyTo;

}
