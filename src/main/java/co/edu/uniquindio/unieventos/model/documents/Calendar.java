package co.edu.uniquindio.unieventos.model.documents;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import co.edu.uniquindio.unieventos.model.vo.Event;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "calendars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Calendar {
	@Id
	private String id;
	private String name;
	private List<Event> events;
	private String image, bannerImage, description;

	public void addEvent(Event event) {
		events.add(event);
	}

	public void updateEvent(Event event) {
		for (int i = 0; i < events.size(); i++)
			if (event.getId().equals(events.get(i).getId())) {
				events.set(i, event);
				return;
			}
	}

}
