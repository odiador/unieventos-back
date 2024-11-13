package co.edu.uniquindio.unieventos.dto.orders;

public record OrderDetailDTO(

	    String calendarId,
	    String eventId,
	    String localityId,
	    float price,
	    int quantity

) {}
