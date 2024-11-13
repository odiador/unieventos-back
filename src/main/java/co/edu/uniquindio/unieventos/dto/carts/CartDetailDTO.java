package co.edu.uniquindio.unieventos.dto.carts;

public record CartDetailDTO(

		int quantity,
		String calendarId,
		String eventId,
		String eventName,
		String localityId,
		String localityName,
		double price,
		String calendarName,
		String eventImage,
		int freeTickets

) {}
