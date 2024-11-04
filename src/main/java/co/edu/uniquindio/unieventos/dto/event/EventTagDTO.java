package co.edu.uniquindio.unieventos.dto.event;

import org.hibernate.validator.constraints.Length;

import co.edu.uniquindio.unieventos.misc.validation.ValidColor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventTagDTO {
		@NotBlank
		@Length(min = 0, max = 20)
		private String name;

		@NotNull
		@ValidColor
		private String color;

		@NotNull
		@ValidColor
		private String textColor;
		
}
