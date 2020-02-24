package co.edu.uniandes.tianguix.model;

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
public class Order {

	@With private String type;
	@With private String asset;
	@With private Integer amount;

}
