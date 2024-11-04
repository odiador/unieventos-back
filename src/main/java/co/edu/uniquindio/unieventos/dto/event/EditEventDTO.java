package co.edu.uniquindio.unieventos.dto.event;

import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import co.edu.uniquindio.unieventos.misc.validation.ValidDateTimeFormat;
import co.edu.uniquindio.unieventos.misc.validation.ValidDateTimeFutureFormat;
import co.edu.uniquindio.unieventos.misc.validation.ValidEnum;
import co.edu.uniquindio.unieventos.model.enums.EventStatus;
import co.edu.uniquindio.unieventos.model.enums.EventType;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EditEventDTO {
	private String idCalendar;

	@Length(min = 0, max = 100)
	private String name;

	@Length(min = 0, max = 100)
	private String newName;

	private MultipartFile eventImage;

	private MultipartFile localityImage;

	@Length(min = 0, max = 50)
	private String city;

	@Length(min = 0, max = 400)
	private String description;

	@Length(min = 0, max = 250)
	private String address;

	
	@ValidDateTimeFormat(message = "Formato de fecha inválido")
	@ValidDateTimeFutureFormat(message = "La fecha de inicio no debe de haber pasado")
	private String startTime;


	@ValidDateTimeFormat(message = "Formato de fecha inválido")
	@ValidDateTimeFutureFormat(message = "La fecha de fin no debe de haber pasado")
	private String endTime;

	private List<@Valid EditLocalityDTO> localities;

	private List<@Valid EventTagDTO> tags;

	@ValidEnum(enumClass = EventStatus.class, message = "El estado del evento es inválido")
	private String status;

	@ValidEnum(enumClass = EventType.class, message = "El tipo del evento es inválido")
	private String type;
	
}
