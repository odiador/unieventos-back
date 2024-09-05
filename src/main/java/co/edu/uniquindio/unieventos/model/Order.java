package co.edu.uniquindio.unieventos.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "ordenes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    private String id;
    private ObjectId clientId;
    private LocalDateTime timestamp;
    private ObjectId paymentId;
    private List<OrderDetail> items;
    private float total;
    private ObjectId couponId;

}
