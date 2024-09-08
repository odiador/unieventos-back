package co.edu.uniquindio.unieventos.services.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.uniquindio.unieventos.dto.ChangePasswordDTO;
import co.edu.uniquindio.unieventos.dto.CreateAccountDTO;
import co.edu.uniquindio.unieventos.dto.LoginDTO;
import co.edu.uniquindio.unieventos.dto.RecoveryPasswordMailSendDTO;
import co.edu.uniquindio.unieventos.dto.UserDataDTO;
import co.edu.uniquindio.unieventos.model.Account;
import co.edu.uniquindio.unieventos.model.AccountStatus;
import co.edu.uniquindio.unieventos.model.Role;
import co.edu.uniquindio.unieventos.model.UserData;
import co.edu.uniquindio.unieventos.model.ValidationCode;
import co.edu.uniquindio.unieventos.repositories.AccountRepository;
import co.edu.uniquindio.unieventos.services.AccountService;
import co.edu.uniquindio.unieventos.services.EmailService;
import co.edu.uniquindio.unieventos.services.RandomCodesService;

public class AccountServiceImpl implements AccountService {

	@Autowired
	AccountRepository repo;

	@Autowired
	EmailService emailService;

	@Autowired
	RandomCodesService randomCodesService;

	@Override
	public Account createAccount(CreateAccountDTO account) throws Exception {
		if (repo.existsByEmailAndUser_Cedula(account.email(), account.cedula()))
			throw new Exception("La cuenta con ese email o cedula ya existe");

		UserData userData = UserData.builder()
				.adress(account.adress())
				.cedula(account.cedula())
				.name(account.name())
				.phone(account.phone())
				.build();

		ValidationCode registerValidation = ValidationCode
				.builder()
				.timestamp(LocalDateTime.now())
				.code(randomCodesService.getRandomRegisterCode())
				.build();

		Account createdAccount = Account.builder()
				.email(account.email())
				.password(account.password())
				.registerValidationCode(registerValidation)
				.user(userData)
				.status(AccountStatus.UNVERIFIED)
				.registrationTime(LocalDateTime.now())
				.role(Role.CLIENT)
				.build();

		repo.save(createdAccount);
		return createdAccount;
	}

	@Override
	public String editAccount(UserDataDTO dto) throws Exception {
		Optional<Account> accountOpt = repo.findById(dto.id());
		if (accountOpt.isEmpty())
			throw new Exception("La cuenta no existe");
		UserData ud = UserData.builder()
				.id(dto.id())
				.cedula(dto.cedula())
				.adress(dto.adress())
				.name(dto.name())
				.phone(dto.phone()).build();
		Account account = accountOpt.get();
		account.setUser(ud);
		repo.save(account);
		return null;
	}

	@Override
	public String deleteAccount(String id) throws Exception {
		repo.deleteById(id);
		return null;
	}

	@Override
	public UserDataDTO getAccountInfo(String id) throws Exception {
		return parseUserData(repo.findById(id)
				.orElseThrow(() -> new Exception("No se pudo encontrar la información de la cuenta"))
				.getUser());
	}

	@Override
	public String sendRecuperationCode(String email) throws Exception {
		final String code = randomCodesService.getPasswordRecoveryCode();
		emailService.sendPasswordRecoveryMail(new RecoveryPasswordMailSendDTO(email, code));
		return null;
	}

	@Override
	public String changePassword(ChangePasswordDTO dto) throws Exception {
		if (!dto.newPassword().equals(dto.confirmPassword()))
			throw new Exception("Las contraseñas no coinciden");
		Account optional = repo.findById(dto.id()).orElseThrow(() -> new Exception("La cuenta no fue encontrada"));
		optional.setPassword(dto.newPassword());
		repo.save(optional);
		return null;
	}

	@Override
	public String login(LoginDTO loginDTO) throws Exception {
		Optional<Account> account = repo.findByEmailAndPassword(loginDTO.email(), loginDTO.password());
		account.orElseThrow(() -> new Exception("Verifica tus credenciales de acceso"));
		return account.get().getId();
	}
	
	public UserDataDTO parseUserData(UserData ud) {
		return new UserDataDTO(ud.getId(), ud.getPhone(), ud.getAdress(), ud.getCedula(), ud.getName());
	}

}
