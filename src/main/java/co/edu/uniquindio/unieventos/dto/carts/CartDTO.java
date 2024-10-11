package co.edu.uniquindio.unieventos.dto.carts;

import java.time.LocalDateTime;
import java.util.List;

public record CartDTO(
	String id,
	LocalDateTime date,
	List<CartDetailDTO> items,
	String userId
) {}
