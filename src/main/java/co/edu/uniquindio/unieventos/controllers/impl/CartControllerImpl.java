package co.edu.uniquindio.unieventos.controllers.impl;

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
import co.edu.uniquindio.unieventos.dto.carts.ExistsCartItemDTO;
import co.edu.uniquindio.unieventos.dto.carts.RemoveItemCartDTO;
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
	public ResponseEntity<?> createCart(HttpServletRequest request) throws Exception {
		authUtils.verifyRoleClient(request);
		String id = authUtils.getId(request);
		return ResponseEntity.status(201).body(cartService.createCart(id));
	}

	@Override
	@PostMapping("/cart/checkitem")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<?> checkCartItem(@Valid @RequestBody ExistsCartItemDTO dto, HttpServletRequest request)
			throws Exception {
		authUtils.verifyRoleClient(request);
		String id = authUtils.getId(request);
		return ResponseEntity.ok(cartService.validateExistsItem(dto, id));
	}

	@Override
	@PostMapping("/cart/addItem")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<?> addItemToCart(@Valid @RequestBody AddItemCartDTO dto, HttpServletRequest request) throws Exception {
		authUtils.verifyRoleClient(request);
		String id = authUtils.getId(request);
		return ResponseEntity.ok(cartService.saveItemToCart(dto, id));
	}

	@Override
	@PostMapping("/cart/removeItem")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<?> removeItemFromCart(@Valid @RequestBody RemoveItemCartDTO dto, HttpServletRequest request)
			throws Exception {
		authUtils.verifyRoleClient(request);
		String id = authUtils.getId(request);
		System.out.println(id);
		return ResponseEntity.ok(cartService.deleteItemFromCart(dto,id));
	}

	@Override
	@PostMapping("/find")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<?> findCart(@Valid @NotNull @ValidObjectId @RequestBody String idCart, HttpServletRequest request)
			throws Exception {
		authUtils.verifyRoleClient(request);
		String id = authUtils.getId(request);
		return ResponseEntity.ok(cartService.getCartById(id,idCart));
	}

	@Override
	@PostMapping("/findAll")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<?> getCarts(HttpServletRequest request) throws Exception {
		authUtils.verifyRoleClient(request);
		String userId = authUtils.getId(request);
		return ResponseEntity.ok(cartService.getCartsByUserId(userId));
	}

	@Override
	@PostMapping("/delete")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<?> deleteCart(@Valid @ValidObjectId @NotNull String idCart, HttpServletRequest request)
			throws Exception {
		authUtils.verifyRoleClient(request);
		String userId = authUtils.getId(request);
		cartService.deleteCartById(userId, idCart);
		return ResponseEntity.ok("R");
	}

}
