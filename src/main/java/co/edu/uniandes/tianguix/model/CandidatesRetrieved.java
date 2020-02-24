package co.edu.uniandes.tianguix.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.Collection;

/**
 * @author <a href="mailto:daniel.bellon@payulatam.com"> Daniel Bellón </a>
 * @since
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidatesRetrieved {

    @With private OrderSaved orderSaved;
    @With private Collection<OrderArrived> candidates;
}
