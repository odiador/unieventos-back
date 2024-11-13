package co.edu.uniquindio.unieventos.model.vo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Field;

import co.edu.uniquindio.unieventos.model.enums.EventStatus;
import co.edu.uniquindio.unieventos.model.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

	@Field("id")
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

	public void updateLocality(Locality locality, int i) {
		if (i >= 0 && i < localities.size()) {
			localities.set(i, locality);
		}
	}
}
