package co.edu.uniquindio.unieventos.dto.event;

import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import co.edu.uniquindio.unieventos.misc.validation.ValidDateTimeFutureFormat;
import co.edu.uniquindio.unieventos.misc.validation.ValidEnum;
import co.edu.uniquindio.unieventos.model.enums.EventStatus;
import co.edu.uniquindio.unieventos.model.enums.EventType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record EditEventDTO(
		@NotNull
		String idCalendar,

		@Length(min = 0, max = 100)
		String name,

		MultipartFile eventImage,

		MultipartFile localityImage,

		@Length(min = 0, max = 50)
		String city, 

		@Length(min = 0, max = 400)
		String description,

		@Length(min = 0, max = 250)
		String address,

		@ValidDateTimeFutureFormat
		String startTime,

		@ValidDateTimeFutureFormat
		String endTime,

		List<@Valid EditLocalityDTO> localities,

		List<@Valid EventTagDTO> tags,

		@ValidEnum(enumClass = EventStatus.class, message = "El estado del evento es inválido")
		String status,

		@ValidEnum(enumClass = EventType.class, message = "El tipo del evento es inválido")
		String type
) {}
