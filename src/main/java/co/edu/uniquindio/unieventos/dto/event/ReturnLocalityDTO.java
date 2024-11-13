package co.edu.uniquindio.unieventos.dto.event;

public record ReturnLocalityDTO(

		String id,
		String name,
		float price,
		int ticketsSold,
		int maxCapability
) {}
