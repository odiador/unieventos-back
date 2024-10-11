package co.edu.uniquindio.unieventos.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CartDetail {

    private int quantity;
    private String calendarId;
    private String eventName;
    private String localityName;


}
