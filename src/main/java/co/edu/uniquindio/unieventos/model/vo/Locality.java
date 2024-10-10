package co.edu.uniquindio.unieventos.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Locality {
	@EqualsAndHashCode.Include
	private String name;
	private float price;
	private int ticketsSold;
	private int maxCapability;

}
