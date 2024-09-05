package co.edu.uniquindio.unieventos.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDetail {

    private int cantidad;
    private String nombreLocalidad;
    private String idEvento;

}
