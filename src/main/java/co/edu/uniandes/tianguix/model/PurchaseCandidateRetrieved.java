package co.edu.uniandes.tianguix.model;

import java.util.Collection;

/**
 * @author <a href="mailto:daniel.bellon@payulatam.com"> Daniel Bellón </a>
 * @since 1.0.0
 */
public class PurchaseCandidateRetrieved implements CandidatesRetrieved {

	private OrderArrived sale;
	private Collection<OrderArrived> purchaseCandidates;
}
