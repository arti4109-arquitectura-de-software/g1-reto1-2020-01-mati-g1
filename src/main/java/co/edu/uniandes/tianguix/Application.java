package co.edu.uniandes.tianguix;

import akka.actor.typed.javadsl.Behaviors;
import co.edu.uniandes.tianguix.actor.OrdersManager;

/**
 * @author <a href="mailto:daniel.bellon@payulatam.com"> Daniel Bellón </a>
 * @since 1.0.0
 */
public class Application {

	public static void main(String[] args) {

		var rootBehaviour = Behaviors.setup(context -> {
			var ordersManageractor = context.spawn(OrdersManager.create(), "OrdersManager");
			return Behaviors.empty();
		});
	}
}
