package co.edu.uniquindio.unieventos.dto.calendar;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import co.edu.uniquindio.unieventos.misc.validation.ValidObjectId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditCalendarDTO {

	@NotBlank
	@Length(min = 0, max = 100)
	String name;
	
	@NotNull
	@ValidObjectId
	String id;

	MultipartFile image;

	MultipartFile bannerImage;

	@NotNull
	@Length(min = 0, max = 400)
	String description;
}
