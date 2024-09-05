package co.edu.uniquindio.unieventos.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Document(collection = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

	@Id
	private String id;
	private Role role;
	private String email, password;
	private ValidationCode registerValidationCode, passwordValidationCode;
	private UserData user;
	private LocalDateTime registrationTime;
	private AccountStatus status;

}
