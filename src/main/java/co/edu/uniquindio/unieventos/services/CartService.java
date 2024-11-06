package co.edu.uniquindio.unieventos.services;

import java.util.List;

import co.edu.uniquindio.unieventos.dto.carts.AddItemCartDTO;
import co.edu.uniquindio.unieventos.dto.carts.CartDTO;
import co.edu.uniquindio.unieventos.dto.carts.ExistsCartItemDTO;
import co.edu.uniquindio.unieventos.dto.carts.RemoveItemCartDTO;
import co.edu.uniquindio.unieventos.dto.misc.ResponseDTO;
import co.edu.uniquindio.unieventos.misc.validation.ValidObjectId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public interface CartService {

	ResponseDTO<CartDTO> createCart(String id) throws Exception;

	ResponseDTO<CartDTO> getCartById(String id, String idCart) throws Exception;
	
	ResponseDTO<List<CartDTO>> getCartsByUserId(String userId);
	
	void deleteCartById(String id, String idCart) throws Exception;

	ResponseDTO<Boolean> saveItemToCart(AddItemCartDTO dto, String id) throws Exception;

	ResponseDTO<Boolean> deleteItemFromCart(RemoveItemCartDTO dto, String userId) throws Exception;

	ResponseDTO<CartDTO> clearCart(String id, String userId) throws Exception;

	ResponseDTO<Boolean> validateExistsItem(ExistsCartItemDTO dto, @NotBlank @ValidObjectId @Valid String userId) throws Exception;
}
