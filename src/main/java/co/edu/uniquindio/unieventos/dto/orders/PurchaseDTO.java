package co.edu.uniquindio.unieventos.dto.orders;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;

import co.edu.uniquindio.unieventos.model.vo.Payment;

public record PurchaseDTO(

	    String id,
	    LocalDateTime timestamp,
	    Payment payment,
	    List<OrderDetailDTO> items,
	    float total,
	    ObjectId couponId

) {}
