package co.edu.uniandes.tianguix.dao;

import co.edu.uniandes.tianguix.model.Match;
import co.edu.uniandes.tianguix.model.ArrivedOrder;
import co.edu.uniandes.tianguix.model.SavedPurchase;
import co.edu.uniandes.tianguix.model.SavedSale;

import java.util.Collection;

/**
 * @author <a href="mailto:daniel.bellon@payulatam.com"> Daniel Bellón </a>
 * @since 1.0.0
 */
public interface OrdersDao {

	void saveOrder(ArrivedOrder arrivedOrder);

	void saveMatch(Match match);

	Collection<ArrivedOrder> getPurchaseCandidates(SavedPurchase purchase);

	Collection<ArrivedOrder> getSaleCandidates(SavedSale sale);

}
