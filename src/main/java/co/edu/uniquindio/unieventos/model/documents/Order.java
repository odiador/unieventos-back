package co.edu.uniquindio.unieventos.model.documents;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import co.edu.uniquindio.unieventos.model.enums.OrderStatus;
import co.edu.uniquindio.unieventos.model.vo.OrderDetail;
import co.edu.uniquindio.unieventos.model.vo.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "orders")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Order {

    @Id
    private String id;
    private ObjectId clientId;
    private LocalDateTime timestamp;
    private Payment payment;
    private List<OrderDetail> items;
    private OrderStatus status;
    private float total;
    private ObjectId couponId;

}
