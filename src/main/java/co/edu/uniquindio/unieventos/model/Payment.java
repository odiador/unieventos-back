package co.edu.uniquindio.unieventos.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class Payment {

	@EqualsAndHashCode.Include
	private String id;
	private String currency, paymentType, statusDetail, authorizationCode, status;
    private LocalDateTime timestamp;
    private float transationValue;

}
