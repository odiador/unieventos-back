package co.edu.uniquindio.unieventos.dto.carts;

public record CartDetailDTO(

		int quantity,
		String calendarId,
		String eventName,
		String localityName,
		double price,
		String calendarName,
		String eventImage,
		int freeTickets

) {}
