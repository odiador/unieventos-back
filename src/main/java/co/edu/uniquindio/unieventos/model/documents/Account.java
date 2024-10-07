package co.edu.uniquindio.unieventos.model.documents;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import co.edu.uniquindio.unieventos.model.enums.AccountStatus;
import co.edu.uniquindio.unieventos.model.enums.Role;
import co.edu.uniquindio.unieventos.model.vo.UserData;
import co.edu.uniquindio.unieventos.model.vo.ValidationCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "accounts")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Account {

	@Id
	private String id;
	private Role role;
	private String email, password;
	private ValidationCode registerValidationCode, passwordRecuperationCode, loginValidationCode;
	private UserData user;
	private List<ObjectId> subscribedCalendars, orders;
	private LocalDateTime registrationTime;
	private AccountStatus status;

}
