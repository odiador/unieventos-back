package co.edu.uniquindio.unieventos.model.vo;

import org.springframework.data.annotation.Id;

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
public class UserData {

    @Id
    private String id;
	private String phone, adress, city, cedula, name;

}
