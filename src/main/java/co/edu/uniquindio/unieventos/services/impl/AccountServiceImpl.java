package co.edu.uniquindio.unieventos.services.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import co.edu.uniquindio.unieventos.config.JWTUtils;
import co.edu.uniquindio.unieventos.dto.auth.ActivateAccountDTO;
import co.edu.uniquindio.unieventos.dto.auth.ChangePasswordDTO;
import co.edu.uniquindio.unieventos.dto.auth.CreateAccountDTO;
import co.edu.uniquindio.unieventos.dto.auth.LoginDTO;
import co.edu.uniquindio.unieventos.dto.auth.RecoveryPasswordMailSendDTO;
import co.edu.uniquindio.unieventos.dto.auth.TokenDTO;
import co.edu.uniquindio.unieventos.dto.auth.VerifyMailSendDTO;
import co.edu.uniquindio.unieventos.dto.client.EditUserDataDTO;
import co.edu.uniquindio.unieventos.dto.client.UserDataDTO;
import co.edu.uniquindio.unieventos.exceptions.ConflictException;
import co.edu.uniquindio.unieventos.exceptions.DelayException;
import co.edu.uniquindio.unieventos.exceptions.DocumentFoundException;
import co.edu.uniquindio.unieventos.exceptions.DocumentNotFoundException;
import co.edu.uniquindio.unieventos.exceptions.InvalidCodeException;
import co.edu.uniquindio.unieventos.exceptions.InvalidLoginException;
import co.edu.uniquindio.unieventos.exceptions.InvalidUsernameException;
import co.edu.uniquindio.unieventos.exceptions.MailSendingException;
import co.edu.uniquindio.unieventos.model.documents.Account;
import co.edu.uniquindio.unieventos.model.enums.AccountStatus;
import co.edu.uniquindio.unieventos.model.enums.Role;
import co.edu.uniquindio.unieventos.model.vo.UserData;
import co.edu.uniquindio.unieventos.model.vo.ValidationCode;
import co.edu.uniquindio.unieventos.repositories.AccountRepository;
import co.edu.uniquindio.unieventos.services.AccountService;
import co.edu.uniquindio.unieventos.services.EmailService;
import co.edu.uniquindio.unieventos.services.RandomCodesService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository repo;

	@Autowired
	private EmailService emailService;

	@Autowired
	private RandomCodesService randomCodesService;

	private final JWTUtils jwtUtils;

	@Override
	public Account createAccount(@Valid CreateAccountDTO account) throws DocumentFoundException, MailSendingException {
		if (repo.existsByEmailOrUser_Cedula(account.email(), account.cedula()))
			throw new DocumentFoundException("La cuenta con ese email o cédula ya existe");

		UserData userData = UserData.builder()
				.adress(account.adress())
				.cedula(account.cedula())
				.name(account.name())
				.city(account.city())
				.phone(account.phone())
				.build();

		ValidationCode registerValidation = ValidationCode.builder()
				.timestamp(LocalDateTime.now())
				.code(randomCodesService.getRandomRegisterCode())
				.build();

		Account createdAccount = Account
				.builder()
				.email(account.email())
				.password(encryptPassword(account.password()))
				.registerValidationCode(registerValidation)
				.user(userData)
				.status(AccountStatus.UNVERIFIED)
				.registrationTime(LocalDateTime.now())
				.role(Role.CLIENT)
				.build();

		emailService.sendVerificationMail(new VerifyMailSendDTO(account.email(), registerValidation.getCode()));
		repo.save(createdAccount);
		return createdAccount;
	}

	@Override
	public String editAccount(@Valid EditUserDataDTO dto) throws Exception {
		Optional<Account> accountOpt = repo.findByEmail(dto.email());
		if (accountOpt.isEmpty())
			throw new DocumentNotFoundException("La cuenta no existe");
		Account account = accountOpt.get();
		if (account.getStatus() != AccountStatus.ACTIVE)
			throw new ConflictException("Tu cuenta no esta activa");
		UserData ud = UserData.builder()
				.cedula(dto.cedula())
				.adress(dto.adress())
				.name(dto.name())
				.phone(dto.phone())
				.build();
		account.setUser(ud);
		repo.save(account);
		return null;
	}

	@Override
	public String deleteAccount(LoginDTO dto) throws Exception {
		Account account = verifyLogin(dto);
		account.setStatus(AccountStatus.DELETED);
		repo.save(account);
		return null;
	}

	@Override
	public UserDataDTO getAccountInfo(String email) throws DocumentNotFoundException {
		return parseUserData(repo.findByEmail(email)
				.orElseThrow(() -> new DocumentNotFoundException("No se pudo encontrar la información de la cuenta"))
				.getUser());
	}

	@Override
	public String sendRecuperationCode(@Valid @Email String email) throws Exception {
		Account account = repo.findByEmail(email)
				.orElseThrow(() -> new DocumentNotFoundException("Tu cuenta no existe"));
		if (account.getStatus() == AccountStatus.UNVERIFIED)
			throw new ConflictException("El estado de tu cuenta no permite esta acción");
		if (account.getStatus() == AccountStatus.DELETED)
			throw new ConflictException("Tu cuenta ha sido eliminada");
		if (account.getStatus() != AccountStatus.ACTIVE)
			throw new ConflictException("Tu cuenta no esta activa");
		ValidationCode valCode = account.getPasswordRecuperationCode();
		if (valCode != null)
			validate15Minutes(valCode);

		ValidationCode newValCode = ValidationCode
				.builder()
				.timestamp(LocalDateTime.now())
				.code(randomCodesService.getPasswordRecoveryCode())
				.build();

		account.setPasswordRecuperationCode(newValCode);
		emailService.sendPasswordRecoveryMail(new RecoveryPasswordMailSendDTO(email, newValCode.getCode()));
		repo.save(account);
		return null;
	}


	private void validate15Minutes(ValidationCode valCode) throws DelayException {
		LocalDateTime now = LocalDateTime.now();
		Duration duration = Duration.between(valCode.getTimestamp(), now);
		if (duration.toMinutes() < 15)
			throw new DelayException("Debes esperar 15 minutos antes de solicitar otro código");
	}

	@Override
	public String changePassword(@Valid ChangePasswordDTO dto)
			throws DocumentNotFoundException, InvalidCodeException {
		Account account = repo.findByEmail(dto.email())
				.orElseThrow(() -> new DocumentNotFoundException("La cuenta no fue encontrada"));
		ValidationCode recuperationCode = account.getPasswordRecuperationCode();
		if (recuperationCode == null || codeExpired(recuperationCode.getTimestamp()))
			throw new InvalidCodeException("Tu código ya está vencido");
		if (!dto.passwordCode().equals(recuperationCode.getCode()))
			throw new InvalidCodeException("Tu código es inválido");
		account.setPassword(dto.newPassword());
		repo.save(account);
		return null;
	}

	private boolean codeExpired(LocalDateTime timestamp) {
		LocalDateTime now = LocalDateTime.now();
		Duration duration = Duration.between(timestamp, now);
		return duration.toMinutes() > 15;
	}

	@Override
	public TokenDTO login(@Valid LoginDTO loginDTO) throws Exception {
		Account accFound = verifyLogin(loginDTO);
		Map<String, Object> map = buildClaims(accFound);
		return new TokenDTO(jwtUtils.generateToken(accFound.getEmail(), map));

	}

	private Account verifyLogin(LoginDTO loginDTO)
			throws InvalidUsernameException, InvalidLoginException, ConflictException {
		Account accFound = repo.findByEmail(loginDTO.email()).orElse(null);
		if (accFound == null)
			throw new InvalidUsernameException("No tienes cuenta");
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		if (!passwordEncoder.matches(loginDTO.password(), accFound.getPassword()))
			throw new InvalidLoginException("Verifica tus credenciales de acceso");
		if (accFound.getStatus() != AccountStatus.ACTIVE)
			throw new ConflictException("Tu cuenta no esta activa");
		return accFound;
	}

	public UserDataDTO parseUserData(@Valid UserData ud) {
		return new UserDataDTO(ud.getName(), ud.getCedula(), ud.getAdress(), ud.getCity(), ud.getPhone());
	}

	@Override
	public String resendActivationCode(String email) throws Exception {
		Account account = repo.findByEmail(email)
				.orElseThrow(() -> new DocumentNotFoundException("Tu cuenta no existe"));
		if (account.getStatus() != AccountStatus.UNVERIFIED)
			throw new ConflictException("El estado de tu cuenta no permite esta acción");
		ValidationCode valCode = account.getRegisterValidationCode();
		if (valCode != null)
			validate15Minutes(valCode);

		ValidationCode newValCode = ValidationCode
				.builder()
				.timestamp(LocalDateTime.now())
				.code(randomCodesService.getRandomRegisterCode())
				.build();

		account.setRegisterValidationCode(newValCode);
		emailService.sendVerificationMail(new VerifyMailSendDTO(email, newValCode.getCode()));
		repo.save(account);
		return null;
	}

	@Override
	public String activateAccount(@Valid ActivateAccountDTO dto)
			throws DocumentNotFoundException, InvalidCodeException, ConflictException {
		Account account = repo.findByEmail(dto.email())
				.orElseThrow(() -> new DocumentNotFoundException("La cuenta no fue encontrada"));
		ValidationCode recuperationCode = account.getRegisterValidationCode();
		if (account.getStatus() != AccountStatus.UNVERIFIED)
			throw new ConflictException("El estado de tu cuenta no permite esta acción");
		if (recuperationCode == null || codeExpired(recuperationCode.getTimestamp()))
			throw new InvalidCodeException("Tu código ya está vencido");
		if (!dto.code().equals(recuperationCode.getCode()))
			throw new InvalidCodeException("Tu código es inválido");
		account.setStatus(AccountStatus.ACTIVE);
		repo.save(account);
		return null;
	}

	private String encryptPassword(String password) {
		return new BCryptPasswordEncoder().encode(password);
	}

	private Map<String, Object> buildClaims(Account acc) {
		return Map.of("role", acc.getRole(), "name", acc.getUser().getName(), "id", acc.getId());
	}

}
