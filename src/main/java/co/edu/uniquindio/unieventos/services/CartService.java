package co.edu.uniquindio.unieventos.services;

import java.util.List;

import co.edu.uniquindio.unieventos.dto.carts.AddItemCartDTO;
import co.edu.uniquindio.unieventos.dto.carts.CartDTO;
import co.edu.uniquindio.unieventos.dto.carts.RemoveItemCartDTO;
import co.edu.uniquindio.unieventos.model.documents.Cart;

public interface CartService {

	public CartDTO createCart(String id) throws Exception;

	public Cart getCartById(String id, String idCart) throws Exception;
	
	public List<CartDTO> getCartsByUserId(String userId);
	
	public void deleteCartById(String id, String idCart) throws Exception;

	public CartDTO saveItemToCart(AddItemCartDTO dto, String id) throws Exception;

	public CartDTO deleteItemFromCart(RemoveItemCartDTO dto, String userId) throws Exception;

	CartDTO clearCart(String id, String userId) throws Exception;
}
