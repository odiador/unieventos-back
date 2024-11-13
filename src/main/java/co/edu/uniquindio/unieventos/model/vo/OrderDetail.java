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
public class OrderDetail {

    private String calendarId;
    private String eventId;
    private String localityId;
    private float price;
    private int quantity;

}
