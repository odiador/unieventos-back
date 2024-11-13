package co.edu.uniquindio.unieventos.dto.coupons;

public record AppliedCouponDTO(
		String id,
		String code,
		float discount,
		boolean forSpecialEvent,
		String calendarId,
		String eventId,
		boolean isUnique
) {}
