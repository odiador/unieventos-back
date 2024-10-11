package co.edu.uniquindio.unieventos.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniquindio.unieventos.model.documents.Cart;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {

	List<Cart> findByUserId(String userId);
	
	Optional<Cart> findByIdAndUserId(String id, String userId);

	/**
	 * Encuentra el carrito activo del Usuario
	 * 
	 * @param userId
	 * @return el carrito activo
	 */
	Cart findTopByUserIdOrderByDateDesc(String userId);

	boolean existsByUserId(String userId);
}
