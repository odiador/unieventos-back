package co.edu.uniquindio.unieventos.services.impl;

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
import co.edu.uniquindio.unieventos.dto.auth.RecuperateAccountDTO;
import co.edu.uniquindio.unieventos.dto.auth.TokenDTO;
import co.edu.uniquindio.unieventos.dto.auth.VerifyMailSendDTO;
import co.edu.uniquindio.unieventos.dto.client.UserDataDTO;
import co.edu.uniquindio.unieventos.exceptions.DocumentFoundException;
import co.edu.uniquindio.unieventos.exceptions.DocumentNotFoundException;
import co.edu.uniquindio.unieventos.exceptions.InvalidCodeException;
import co.edu.uniquindio.unieventos.exceptions.InvalidLoginException;
import co.edu.uniquindio.unieventos.exceptions.InvalidPasswordException;
import co.edu.uniquindio.unieventos.exceptions.InvalidUsernameException;
import co.edu.uniquindio.unieventos.exceptions.MailSendingException;
import co.edu.uniquindio.unieventos.model.Account;
import co.edu.uniquindio.unieventos.model.AccountStatus;
import co.edu.uniquindio.unieventos.model.Role;
import co.edu.uniquindio.unieventos.model.UserData;
import co.edu.uniquindio.unieventos.model.ValidationCode;
import co.edu.uniquindio.unieventos.repositories.AccountRepository;
import co.edu.uniquindio.unieventos.services.AccountService;
import co.edu.uniquindio.unieventos.services.EmailService;
import co.edu.uniquindio.unieventos.services.RandomCodesService;
import jakarta.validation.Valid;
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
		if (repo.existsByEmailAndUser_Cedula(account.email(), account.cedula()))
			throw new DocumentFoundException("La cuenta con ese email o cédula ya existe");

		UserData userData = UserData.builder().adress(account.adress()).cedula(account.cedula()).name(account.name())
				.phone(account.phone()).build();

		ValidationCode registerValidation = ValidationCode.builder().timestamp(LocalDateTime.now())
				.code(randomCodesService.getRandomRegisterCode()).build();

		Account createdAccount = Account.builder()
				.email(account.email())
				.password(encryptPassword(account.password()))
				.registerValidationCode(registerValidation)
				.user(userData)
				.status(AccountStatus.UNVERIFIED)
				.registrationTime(LocalDateTime.now())
				.role(Role.CLIENT)
				.build();

		try {
			emailService.sendVerificationMail(new VerifyMailSendDTO(account.email(), registerValidation.getCode()));
		} catch (Exception e) {
			throw new MailSendingException(e.getMessage());
		}

		repo.save(createdAccount);
		return createdAccount;
	}

	@Override
	public String editAccount(@Valid UserDataDTO dto) throws DocumentNotFoundException {
		Optional<Account> accountOpt = repo.findByUser_Cedula(dto.id());
		if (accountOpt.isEmpty())
			throw new DocumentNotFoundException("La cuenta no existe");
		UserData ud = UserData.builder()
				.id(dto.id())
				.cedula(dto.cedula())
				.adress(dto.adress())
				.name(dto.name())
				.phone(dto.phone())
				.build();
		Account account = accountOpt.get();
		account.setUser(ud);
		repo.save(account);
		return null;
	}

	@Override
	public String deleteAccount(String id) throws DocumentNotFoundException {
		Optional<Account> accountOpt = repo.findByUser_Cedula(id);
		if (accountOpt.isEmpty())
			throw new DocumentNotFoundException("La cuenta no existe");
		Account account = accountOpt.get();
		account.setStatus(AccountStatus.DELETED);
		repo.save(account);
		return null;
	}

	@Override
	public UserDataDTO getAccountInfo(String id) throws DocumentNotFoundException {
		return parseUserData(repo.findByUser_Cedula(id)
				.orElseThrow(() -> new DocumentNotFoundException("No se pudo encontrar la información de la cuenta"))
				.getUser());
	}

	@Override
	public String sendRecuperationCode(String email) throws DocumentNotFoundException, MailSendingException {
		// TODO verificar fecha
		final String code = randomCodesService.getPasswordRecoveryCode();
		emailService.sendPasswordRecoveryMail(new RecoveryPasswordMailSendDTO(email, code));
		return null;
	}

	@Override
	public String changePassword(@Valid ChangePasswordDTO dto) throws DocumentNotFoundException, InvalidPasswordException {
		if (!dto.newPassword().equals(dto.confirmPassword()))
			throw new InvalidPasswordException("Las contraseñas no coinciden");
		Account optional = repo.findByEmail(dto.email())
				.orElseThrow(() -> new DocumentNotFoundException("La cuenta no fue encontrada"));
		optional.setPassword(dto.newPassword());
		repo.save(optional);
		return null;
	}

	@Override
	public TokenDTO login(@Valid LoginDTO loginDTO) throws InvalidLoginException, InvalidUsernameException {
		Account accFound = repo.findByEmail(loginDTO.email()).orElse(null);
		if (accFound == null)
			throw new InvalidUsernameException("No tienes cuenta");
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		if (!passwordEncoder.matches(loginDTO.password(), accFound.getPassword()))
			throw new InvalidLoginException("Verifica tus credenciales de acceso");

		Map<String, Object> map = buildClaims(accFound);
		return new TokenDTO(jwtUtils.generateToken(accFound.getEmail(), map));

	}

	public UserDataDTO parseUserData(@Valid UserData ud) {
		return new UserDataDTO(ud.getId(), ud.getPhone(), ud.getAdress(), ud.getCedula(), ud.getName());
	}

	@Override
	public String recuperateAccount(@Valid RecuperateAccountDTO dto) throws DocumentNotFoundException, InvalidCodeException {
		// TODO
		return null;
	}

	@Override
	public String activateAccount(@Valid ActivateAccountDTO dto) throws DocumentNotFoundException, InvalidCodeException {
		// TODO
		return null;
	}

	private String encryptPassword(String password) {
		return new BCryptPasswordEncoder().encode(password);
	}

	private Map<String, Object> buildClaims(Account acc) {
		return Map.of("rol", acc.getRole(), "nombre", acc.getUser().getName(), "id", acc.getId());
	}

}
