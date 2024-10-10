package co.edu.uniquindio.unieventos.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import co.edu.uniquindio.unieventos.model.documents.Calendar;

public interface CalendarRepository extends MongoRepository<Calendar, String> {


	/**
	 * @author chatgpt
	 * @param name
	 * @param city
	 * @param date
	 * @return
	 */
	@Aggregation(pipeline = {
	        "{ $unwind: '$events' }",
	        "{ $match: { $and: [ " + 
	        "{ 'events.name': { $regex: ?0, $options: 'i' } }, " +
	        "{ 'events.city': { $regex: ?1, $options: 'i' } }, " +
	        "{ 'events.status': { $ne: 'DELETED' } }, " +
	        "{ 'events.date': { $eq: ?2 } }, " +
	        "{ 'events.tags.name': { $regex: ?3, $options: 'i' } } ] } }",
	        "{ $group: { _id: '$_id', name: { $first: '$name' }, events: { $push: '$events' } } }",
	        "{ $skip: ?4 }",
	        "{ $limit: ?5 }"
	})
	List<Calendar> findCalendarsWithFilteredEvents(String name, String city, LocalDate date, String tagName, int skip,
			int limit);


	Optional<Calendar> findByName(String name);
}
