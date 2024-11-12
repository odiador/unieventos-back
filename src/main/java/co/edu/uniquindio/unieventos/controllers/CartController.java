package co.edu.uniquindio.unieventos.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;

import co.edu.uniquindio.unieventos.dto.carts.AddItemCartDTO;
import co.edu.uniquindio.unieventos.dto.carts.CartDTO;
import co.edu.uniquindio.unieventos.dto.carts.ExistsCartItemDTO;
import co.edu.uniquindio.unieventos.dto.carts.RemoveItemCartDTO;
import co.edu.uniquindio.unieventos.dto.misc.ResponseDTO;
import co.edu.uniquindio.unieventos.misc.validation.ValidObjectId;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface CartController {

	ResponseEntity<ResponseDTO<CartDTO>> createCart(HttpServletRequest request) throws Exception;

	ResponseEntity<ResponseDTO<Boolean>> checkCartItem(@Valid ExistsCartItemDTO dto, HttpServletRequest request)
			throws Exception;

	ResponseEntity<ResponseDTO<Void>> addItemToCart(@Valid AddItemCartDTO dto, HttpServletRequest request)
			throws Exception;

	ResponseEntity<ResponseDTO<Void>> removeItemFromCart(@Valid RemoveItemCartDTO dto, HttpServletRequest request)
			throws Exception;

	ResponseEntity<ResponseDTO<CartDTO>> findCart(@Valid @NotNull @ValidObjectId String idCart,
			HttpServletRequest request) throws Exception;

	ResponseEntity<ResponseDTO<List<CartDTO>>> getCarts(HttpServletRequest request) throws Exception;

	ResponseEntity<ResponseDTO<Void>> deleteCart(@Valid @ValidObjectId @NotNull String idCart,
			HttpServletRequest request) throws Exception;

	ResponseEntity<ResponseDTO<Void>> clearCart(@Valid @ValidObjectId @NotNull String idCart,
			HttpServletRequest request) throws Exception;

}
