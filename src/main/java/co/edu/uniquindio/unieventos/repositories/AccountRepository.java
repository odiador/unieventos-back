package co.edu.uniquindio.unieventos.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniquindio.unieventos.model.documents.Account;
import co.edu.uniquindio.unieventos.model.enums.AccountStatus;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

	Optional<Account> findByEmail(String email);

	boolean existsByEmailOrUser_Cedula(String email, String cedula);

	boolean existsByEmail(String email);

	boolean existsByEmailAndStatus(String email, AccountStatus status);

	Optional<Account> findByEmailAndPassword(String email, String password);

	Optional<Account> findByUser_Cedula(String id);
}
