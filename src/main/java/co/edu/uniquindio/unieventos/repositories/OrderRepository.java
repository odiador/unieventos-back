package co.edu.uniquindio.unieventos.repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniquindio.unieventos.model.documents.Order;
import co.edu.uniquindio.unieventos.model.enums.OrderStatus;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
	List<Order> findByClientIdAndStatus(ObjectId clientId, OrderStatus status);
	List<Order> findByClientId(ObjectId clientId);
}
