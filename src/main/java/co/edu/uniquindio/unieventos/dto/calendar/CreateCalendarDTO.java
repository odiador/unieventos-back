package co.edu.uniquindio.unieventos.dto.calendar;

import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import co.edu.uniquindio.unieventos.misc.validation.ValidObjectId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateCalendarDTO{

		@NotNull
		@NotBlank
		@Length(min = 0, max = 100)
		String name;

		List<@NotNull @ValidObjectId String> events;

		@NotNull
		MultipartFile image;

		@NotNull
		MultipartFile bannerImage;

		@NotNull
		@Length(min = 0, max = 400)
		String description;


}
