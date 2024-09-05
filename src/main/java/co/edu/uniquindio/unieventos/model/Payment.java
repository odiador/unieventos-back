package co.edu.uniquindio.unieventos.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "paymentsx")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    private String currency;
    private String paymentType;
    private String statusDetail;
    private String authorizationCode;
    private LocalDateTime timestamp;
    private float transationValue;
    private String status;

}
