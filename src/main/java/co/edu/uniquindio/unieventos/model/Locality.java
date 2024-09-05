package co.edu.uniquindio.unieventos.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Locality {

	private String name;
	private float price;
	private int ticketsSold;
	private int maxCapability;

}
