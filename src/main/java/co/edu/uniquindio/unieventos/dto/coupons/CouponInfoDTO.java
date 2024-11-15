package co.edu.uniquindio.unieventos.dto.coupons;

public record CouponInfoDTO(

		float discount,
		String expiryDate,
		String code,
		String status,
		String type,
		String name,
		String calendarId,
		String calendarName,
		String eventId,
		String eventName
		

) {}
