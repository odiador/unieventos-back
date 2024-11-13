package co.edu.uniquindio.unieventos.dto.orders;

import java.util.List;

import org.bson.types.ObjectId;

import co.edu.uniquindio.unieventos.model.vo.Payment;

public record FindOrderDTO(

	    String id,
	    ObjectId clientId,
	    String timestamp,
	    Payment payment,
	    List<FindOrderDetailDTO> items,
	    String status,
	    float total,
	    ObjectId couponId

) {}
