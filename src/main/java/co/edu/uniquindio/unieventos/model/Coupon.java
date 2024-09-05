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
    private float descuento;
    private LocalDateTime fechaVencimiento;
    private String codigo;
    private CouponStatus estado;
    private CouponType tipo;
    private String nombre;

}
