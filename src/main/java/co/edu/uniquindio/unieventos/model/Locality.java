package co.edu.uniquindio.unieventos.model;

import com.google.auto.value.AutoValue.Builder;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Locality {
	@EqualsAndHashCode.Include
	private String name;
	private float price;
	private int ticketsSold;
	private int maxCapability;

}
