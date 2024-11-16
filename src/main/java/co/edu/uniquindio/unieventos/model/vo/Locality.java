package co.edu.uniquindio.unieventos.model.vo;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Locality {
	@EqualsAndHashCode.Include
	@Field("id")
	@Getter
	@Setter
	private String id;

	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
	private float price;

	@Setter
	private int ticketsSold;

	@Getter
	@Setter
	private int retention;

	@Getter
	@Setter
	private int maxCapability;

	public int getFreeTickets() {
		return maxCapability - ticketsSold;
	}

	public int minEditingCapability() {
		return ticketsSold + retention;
	}

	public int getTicketsSold() {
		return ticketsSold;
	}
}
