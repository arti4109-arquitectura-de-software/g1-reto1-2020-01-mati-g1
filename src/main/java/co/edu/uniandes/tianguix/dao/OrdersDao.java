package co.edu.uniandes.tianguix.dao;

import co.edu.uniandes.tianguix.model.Match;
import co.edu.uniandes.tianguix.model.OrderArrived;
import co.edu.uniandes.tianguix.model.PurchaseOrderSaved;
import co.edu.uniandes.tianguix.model.SaleOrderSaved;

import java.util.Collection;

/**
 * @author <a href="mailto:daniel.bellon@payulatam.com"> Daniel Bellón </a>
 * @since 1.0.0
 */
public interface OrdersDao {

	void saveOrder(OrderArrived orderArrived);

	void saveMatch(Match match);

	Collection<OrderArrived> getPurchaseCandidates(PurchaseOrderSaved purchase);

	Collection<OrderArrived> getSaleCandidates(SaleOrderSaved sale);

}
