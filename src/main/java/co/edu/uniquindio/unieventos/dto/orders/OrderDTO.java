package co.edu.uniquindio.unieventos.dto.orders;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;

import co.edu.uniquindio.unieventos.model.enums.OrderStatus;
import co.edu.uniquindio.unieventos.model.vo.Payment;

public record OrderDTO(

		String id,
	    ObjectId clientId,
	    LocalDateTime timestamp,
	    Payment payment,
	    List<OrderDetailDTO> items,
	    OrderStatus status,
	    float total,
	    ObjectId couponId

) {}
