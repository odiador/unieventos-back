package co.edu.uniquindio.unieventos.dto.event;

import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import co.edu.uniquindio.unieventos.misc.validation.ValidDateTimeFutureFormat;
import co.edu.uniquindio.unieventos.misc.validation.ValidEnum;
import co.edu.uniquindio.unieventos.misc.validation.ValidObjectId;
import co.edu.uniquindio.unieventos.model.enums.EventStatus;
import co.edu.uniquindio.unieventos.model.enums.EventType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEventDTO {
	

	@NotNull	
	@ValidObjectId
	private String idCalendar;
	
	@NotBlank
	@Length(min = 0, max = 100)
	private String name;

	private MultipartFile eventImage;

	private MultipartFile localityImage;

	@NotBlank
	@Length(min = 0, max = 50)
	private String city;

	@NotBlank
	@Length(min = 0, max = 400)
	private String description;

	@NotBlank
	@Length(min = 0, max = 250)
	private String address;

	@NotNull
	@ValidDateTimeFutureFormat
	private String startTime;

	@NotNull
	@ValidDateTimeFutureFormat
	private String endTime;

	private List<@Valid LocalityDTO> localities;

	private List<@Valid EventTagDTO> tags;

	@NotNull
	@ValidEnum(enumClass = EventStatus.class, message = "El estado del evento es inválido")
	private String status;

	@NotNull
	@ValidEnum(enumClass = EventType.class, message = "El tipo del evento es inválido")
	private String type;

}
