package co.edu.uniquindio.unieventos.dto.orders;

import java.time.LocalDateTime;

public record FindOrderDetailDTO(

	    String calendarId,
	    String eventId,
	    String localityId,
	    String calendarName,
	    String eventName,
	    String localityName,
	    String eventImage,
	    LocalDateTime startTime,
	    LocalDateTime endTime,
	    String city,
	    float price,
	    int quantity

) {}
