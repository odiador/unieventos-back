package co.edu.uniquindio.unieventos.repositories.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import co.edu.uniquindio.unieventos.model.documents.Calendar;
import co.edu.uniquindio.unieventos.repositories.CalendarRepositoryCustom;

/**
 * @author chatgpt
 */
@Repository
public class CalendarRepositoryCustomImpl implements CalendarRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<Calendar> findCalendarsWithFilteredEvents(String id, String name, String city, LocalDate date,
			String tagName, int skip, int limit) {
		List<Criteria> criteriaList = new ArrayList<>();

		if (name != null) {
			criteriaList.add(Criteria.where("events.name").regex(name, "i"));
		}
		if (id != null) {
			criteriaList.add(Criteria.where("_id").is(id));
		}
		if (city != null) {
			criteriaList.add(Criteria.where("events.city").regex(city, "i"));
		}
		if (date != null) {
			criteriaList.add(Criteria.where("events.date").is(date));
		}
		if (tagName != null) {
			criteriaList.add(Criteria.where("events.tags").elemMatch(Criteria.where("name").regex(tagName, "i")));
		}

		criteriaList.add(Criteria.where("events.status").ne("DELETED"));

		Aggregation aggregation = Aggregation.newAggregation(Aggregation.unwind("events"),
				Aggregation.match(new Criteria().andOperator(criteriaList.toArray(new Criteria[0]))),
				Aggregation.group("_id").first("name").as("name").push("events").as("events"), Aggregation.skip(skip),
				Aggregation.limit(limit));

		AggregationResults<Calendar> results = mongoTemplate.aggregate(aggregation, "calendars", Calendar.class);

		return results.getMappedResults();
	}

}
