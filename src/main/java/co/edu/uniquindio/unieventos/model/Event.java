package co.edu.uniquindio.unieventos.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

	@Id
	private String id;
	private String name;
	private String eventImage;
	private String localityImage;
	private String city;
	private String description;
	private String adress;
	private LocalDate date;
	private LocalTime startTime, endTime;
	private List<Locality> localities;
	private EventStatus status;
	private EventType type;

	public boolean isAllday() {
		return startTime == null && endTime == null;
	}
}
