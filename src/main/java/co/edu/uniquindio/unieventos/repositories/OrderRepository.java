package co.edu.uniquindio.unieventos.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniquindio.unieventos.model.documents.Order;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

}
