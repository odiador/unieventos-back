package co.edu.uniquindio.unieventos.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import co.edu.uniquindio.unieventos.model.documents.Calendar;

public interface CalendarRepository extends MongoRepository<Calendar, String> {

	Optional<Calendar> findByName(String name);


	Page<Calendar> findAll(Pageable pageable);
}
