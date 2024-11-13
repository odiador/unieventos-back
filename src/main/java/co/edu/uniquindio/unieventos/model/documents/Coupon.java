package co.edu.uniquindio.unieventos.model.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import co.edu.uniquindio.unieventos.model.enums.CouponStatus;
import co.edu.uniquindio.unieventos.model.enums.CouponType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Document(collection = "coupons")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Coupon {

	@Id
	private String id;
	private float discount;
	private LocalDateTime expiryDate;
	private String code;
	private CouponStatus status;
	private CouponType type;
	private String name;
	private String calendarId;
	private String eventId;

	public boolean isForSpecialEvent() {
		return calendarId != null && eventId != null;
	}

}
