package co.edu.uniquindio.unieventos.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.uniquindio.unieventos.dto.carts.CreateCartDTO;
import co.edu.uniquindio.unieventos.dto.carts.CreateCartDetailDTO;
import co.edu.uniquindio.unieventos.model.Cart;
import co.edu.uniquindio.unieventos.model.CartDetail;
import co.edu.uniquindio.unieventos.repositories.CartRepository;
import co.edu.uniquindio.unieventos.services.CartService;

public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository cartRepository;

	@Override
	public Cart createCart(CreateCartDTO cartDTO) throws Exception {
		try {
			Cart cart = new Cart();
			cart.setDate(LocalDateTime.now());
			cart.setUserId(cartDTO.userId());
			cart.setItems(cartDTO.items().stream().map(this::parseToCartDetail).collect(Collectors.toList()));
			return cartRepository.save(cart);
		} catch (Exception e) {
			throw new Exception("Ha ocurrido un error interno.");
		}
	}

	@Override
	public Cart getCartById(String id) throws Exception {
		return cartRepository.findById(id).orElseThrow(() -> new Exception("El carrito no pudo ser obtenido"));
	}

	@Override
	public List<Cart> getCartsByUserId(String userId) {
		return cartRepository.findByUserId(userId);
	}

	@Override
	public Cart clearCart(String id) throws Exception {
		Cart cart = getCartById(id);
		cart.setItems(List.of());
		return cartRepository.save(cart);
	}

	@Override
	public void deleteCartById(String id) throws Exception {
		try {
			cartRepository.deleteById(id);
		} catch (IllegalArgumentException e) {
			throw new Exception("El carrito no pudo ser eliminado");
		}
	}

	private CartDetail parseToCartDetail(CreateCartDetailDTO cartDetailDTO) {
		return CartDetail.builder().eventId(cartDetailDTO.eventId()).localityName(cartDetailDTO.localityName())
				.quantity(cartDetailDTO.quantity()).build();
	}
}
