package co.edu.uniandes.tianguix.dao;

import co.edu.uniandes.tianguix.model.*;

import java.util.Collection;

/**
 * @author <a href="mailto:daniel.bellon@payulatam.com"> Daniel Bellón </a>
 * @since 1.0.0
 */
public interface OrdersDao {

	void saveOrder(ArrivedOrder arrivedOrder);

	void saveMatch(Match match);

	Collection<Order> getPurchaseCandidates(SavedPurchase purchase);

	Collection<Order> getSaleCandidates(SavedSale sale);

}
