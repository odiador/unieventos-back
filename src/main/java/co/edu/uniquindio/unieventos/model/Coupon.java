package co.edu.uniquindio.unieventos.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Document(collection = "coupons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    @Id
    private String id;
    private float discount;
    private LocalDateTime expiryDate;
    private String code;
    private CouponStatus status;
    private CouponType type;
    private String name;

}
