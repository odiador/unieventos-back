package co.edu.uniquindio.unieventos.dto.orders;

public record FindOrderDetailDTO(

	    String calendarId,
	    String eventId,
	    String localityId,
	    String calendarName,
	    String eventName,
	    String localityName,
	    String eventImage,
	    float price,
	    int quantity

) {}
