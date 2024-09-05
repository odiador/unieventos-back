package co.edu.uniquindio.unieventos.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document("users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserData {

    @Id
    private String id;
    private String phone;
    private String adress;
    private String cedula;
    private String name;

}
