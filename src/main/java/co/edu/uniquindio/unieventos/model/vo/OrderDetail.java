package co.edu.uniquindio.unieventos.model.vo;

import org.bson.types.ObjectId;

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

    private ObjectId calendarId;
    private String eventName;
    private String localityName;
    private int quantity;

}
