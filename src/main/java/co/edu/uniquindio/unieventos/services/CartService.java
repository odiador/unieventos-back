package co.edu.uniquindio.unieventos.services;

import java.util.List;

import co.edu.uniquindio.unieventos.dto.carts.CreateCartDTO;
import co.edu.uniquindio.unieventos.model.documents.Cart;

public interface CartService {

	public Cart createCart(CreateCartDTO cartDTO) throws Exception;

	public Cart getCartById(String id) throws Exception;
	
	public List<Cart> getCartsByUserId(String userId);
	
	public Cart clearCart(String id) throws Exception;
	
	public void deleteCartById(String id) throws Exception;
}
