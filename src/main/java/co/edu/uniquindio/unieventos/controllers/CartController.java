package co.edu.uniquindio.unieventos.controllers;

import org.springframework.http.ResponseEntity;

import co.edu.uniquindio.unieventos.dto.carts.AddItemCartDTO;
import co.edu.uniquindio.unieventos.dto.carts.ExistsCartItemDTO;
import co.edu.uniquindio.unieventos.dto.carts.RemoveItemCartDTO;
import co.edu.uniquindio.unieventos.misc.validation.ValidObjectId;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface CartController {

	ResponseEntity<?> addItemToCart(@Valid AddItemCartDTO dto, HttpServletRequest request) throws Exception;

	ResponseEntity<?> removeItemFromCart(@Valid RemoveItemCartDTO dto, HttpServletRequest request) throws Exception;

	ResponseEntity<?> findCart(@Valid @NotNull @ValidObjectId String idCart, HttpServletRequest request) throws Exception;

	ResponseEntity<?> deleteCart(@Valid @ValidObjectId @NotNull String idCart, HttpServletRequest request) throws Exception;

	ResponseEntity<?> createCart(HttpServletRequest request) throws Exception;

	ResponseEntity<?> getCarts(HttpServletRequest request) throws Exception;

	ResponseEntity<?> checkCartItem(@Valid ExistsCartItemDTO dto, HttpServletRequest request) throws Exception;
}
