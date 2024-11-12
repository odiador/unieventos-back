package co.edu.uniquindio.unieventos.controllers.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.unieventos.config.AuthUtils;
import co.edu.uniquindio.unieventos.controllers.CartController;
import co.edu.uniquindio.unieventos.dto.carts.AddItemCartDTO;
import co.edu.uniquindio.unieventos.dto.carts.CartDTO;
import co.edu.uniquindio.unieventos.dto.carts.ExistsCartItemDTO;
import co.edu.uniquindio.unieventos.dto.carts.RemoveItemCartDTO;
import co.edu.uniquindio.unieventos.dto.misc.ResponseDTO;
import co.edu.uniquindio.unieventos.misc.validation.ValidObjectId;
import co.edu.uniquindio.unieventos.services.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@CrossOrigin
@RequestMapping("/api/carts")
public class CartControllerImpl implements CartController {
	
	@Autowired
	private AuthUtils authUtils;
	@Autowired
	private CartService cartService;

	@Override
	@PostMapping("/create")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<ResponseDTO<CartDTO>> createCart(HttpServletRequest request) throws Exception {
		authUtils.verifyRoleClient(request);
		String id = authUtils.getId(request);
		return ResponseEntity.status(201).body(new ResponseDTO<>("Carrito creado", cartService.createCart(id)));
	}

	@Override
	@PostMapping("/cart/checkitem")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<ResponseDTO<Boolean>> checkCartItem(@Valid @RequestBody ExistsCartItemDTO dto, HttpServletRequest request)
			throws Exception {
		authUtils.verifyRoleClient(request);
		String id = authUtils.getId(request);
		boolean existsItem = cartService.validateExistsItem(dto, id);
		String existsString = "El item" + (existsItem ? "" : " no") + " existe en el carrito";
		return ResponseEntity.ok(new ResponseDTO<>(existsString, existsItem));
	}

	@Override
	@PostMapping("/cart/addItem")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<ResponseDTO<Void>> addItemToCart(@Valid @RequestBody AddItemCartDTO dto, HttpServletRequest request) throws Exception {
		authUtils.verifyRoleClient(request);
		String id = authUtils.getId(request);
		cartService.saveItemToCart(dto, id);
		return ResponseEntity.ok(new ResponseDTO<>("Item guardado", null));
		
	}

	@Override
	@PostMapping("/cart/removeItem")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<ResponseDTO<Void>> removeItemFromCart(@Valid @RequestBody RemoveItemCartDTO dto, HttpServletRequest request)
			throws Exception {
		authUtils.verifyRoleClient(request);
		String id = authUtils.getId(request);
		cartService.deleteItemFromCart(dto, id);
		return ResponseEntity.ok(new ResponseDTO<Void>("Item eliminado del carrito", null));
	}

	@Override
	@PostMapping("/find")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<ResponseDTO<CartDTO>> findCart(@Valid @NotNull @ValidObjectId @RequestBody String idCart, HttpServletRequest request)
			throws Exception {
		authUtils.verifyRoleClient(request);
		String id = authUtils.getId(request);
		CartDTO cart = cartService.getCartById(id, idCart);
		return ResponseEntity.ok(new ResponseDTO<>("Carrito encontrado", cart));
	}

	@Override
	@PostMapping("/findAll")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<ResponseDTO<List<CartDTO>>> getCarts(HttpServletRequest request) throws Exception {
		authUtils.verifyRoleClient(request);
		String userId = authUtils.getId(request);
		List<CartDTO> carts = cartService.getCartsByUserId(userId);
		return ResponseEntity.ok(new ResponseDTO<List<CartDTO>>("Carritos encontrados", carts));
	}

	@Override
	@PostMapping("/delete")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<ResponseDTO<Void>> deleteCart(@Valid @ValidObjectId @NotNull String idCart, HttpServletRequest request)
			throws Exception {
		authUtils.verifyRoleClient(request);
		String userId = authUtils.getId(request);
		cartService.deleteCartById(userId, idCart);
		return ResponseEntity.ok(new ResponseDTO<>("Carrito eliminado", null));
	}
	@Override
	@PostMapping("/clear")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<ResponseDTO<Void>> clearCart(@Valid @ValidObjectId @NotNull String idCart, HttpServletRequest request)
			throws Exception {
		authUtils.verifyRoleClient(request);
		String userId = authUtils.getId(request);
		cartService.clearCart(userId, idCart);
		return ResponseEntity.ok(new ResponseDTO<>("Carrito eliminado", null));
	}

}
