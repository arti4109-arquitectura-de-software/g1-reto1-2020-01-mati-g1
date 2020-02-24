package co.edu.uniandes.tianguix.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.Collection;

/**
 * @author <a href="mailto:daniel.bellon@payulatam.com"> Daniel Bellón </a>
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleCandidatesRetrieved implements CandidatesRetrieved{

	@With private ArrivedOrder purchase;
	@With private Collection<Order> saleCandidates;
}
