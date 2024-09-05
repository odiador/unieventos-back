package co.edu.uniquindio.unieventos.model;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {

    private String id;
    private ObjectId eventId;
    private String localityName;
    private float price;
    private int quantity;

}
