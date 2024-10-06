package co.edu.uniquindio.unieventos.model.documents;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import co.edu.uniquindio.unieventos.model.enums.EventStatus;
import co.edu.uniquindio.unieventos.model.enums.EventType;
import co.edu.uniquindio.unieventos.model.vo.EventTag;
import co.edu.uniquindio.unieventos.model.vo.Locality;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Document(collection = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

	@Id
	private String id;
	private String name, eventImage, localityImage, city, description, address;
	private LocalDateTime startTime, endTime;
	private List<Locality> localities;
	private List<EventTag> tags;
	private EventStatus status;
	private EventType type;

	public Locality getLocality(String localityName) {
		for (Locality locality : localities)
			if (locality.getName().equals(localityName))
				return locality;
		return null;
	}
}
