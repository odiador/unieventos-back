package co.edu.uniquindio.unieventos.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.unieventos.config.AuthUtils;
import co.edu.uniquindio.unieventos.controllers.AuthController;
import co.edu.uniquindio.unieventos.dto.auth.ActivateAccountDTO;
import co.edu.uniquindio.unieventos.dto.auth.ChangePasswordDTO;
import co.edu.uniquindio.unieventos.dto.auth.CheckUserDTO;
import co.edu.uniquindio.unieventos.dto.auth.CreateAccountDTO;
import co.edu.uniquindio.unieventos.dto.auth.LoginDTO;
import co.edu.uniquindio.unieventos.dto.auth.LoginResponseDTO;
import co.edu.uniquindio.unieventos.dto.misc.ResponseDTO;
import co.edu.uniquindio.unieventos.exceptions.UnauthorizedAccessException;
import co.edu.uniquindio.unieventos.model.documents.Account;
import co.edu.uniquindio.unieventos.services.AccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
@Validated
public class AuthControllerImpl implements AuthController {

	@Autowired
	private AccountService accountService;
	@Autowired
	private AuthUtils authUtils;

	@Override
	@PostMapping("/create")
	public ResponseEntity<ResponseDTO<Account>> createAccount(@Valid @RequestBody CreateAccountDTO account) throws Exception {
		return ResponseEntity.status(HttpStatus.CREATED).body(
				new ResponseDTO<>("Tu cuenta ha sido creada con éxito", 
						accountService.createAccount(account)));
	}

	@Override
	@PostMapping("/password/recovery")
	public ResponseEntity<ResponseDTO<Void>> sendRecuperationCode(@Valid @Email @RequestParam("email") String email) throws Exception {
		accountService.sendRecuperationCode(email);
		return ResponseEntity.ok(new ResponseDTO<>("Se ha enviado el correo de recuperación de contraseña exitosamente", null));
	}

	@Override
	@PostMapping("/activation/send")
	public ResponseEntity<ResponseDTO<Void>> resendActivationCode(@Valid @Email @RequestParam("email") String email) throws Exception {
		accountService.resendActivationCode(email);
		ResponseDTO<Void> dto = new ResponseDTO<>("Se mandó un código de activación a tu cuenta exitosamente",
				null);
		return ResponseEntity.ok(dto);
	}

	@Override
	@PostMapping("/activation/activate")
	public ResponseEntity<ResponseDTO<Void>> activateAccount(@Valid @RequestBody ActivateAccountDTO dto) throws Exception {
		accountService.activateAccount(dto);
		ResponseDTO<Void> responseDTO = new ResponseDTO<>("Tu cuenta ha sido activada exitosamente", null);
		return ResponseEntity.ok(responseDTO);
	}

	@Override
	@PostMapping("/password/change")
	public ResponseEntity<ResponseDTO<Void>> changePassword(@Valid @RequestBody ChangePasswordDTO change) throws Exception {
		accountService.changePassword(change);
		return ResponseEntity.ok(new ResponseDTO<>("Tu contraseña ha sido cambiada exitosamente", null));
	}

	@Override
	@PostMapping("/login")
	public ResponseEntity<ResponseDTO<LoginResponseDTO>> login(@Valid @RequestBody LoginDTO loginDTO) throws Exception {
		LoginResponseDTO login = accountService.login(loginDTO);
		return ResponseEntity.ok(new ResponseDTO<>("Se pudo iniciar sesión", login));
	}

	@Override
	@PostMapping("/checkUser")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<ResponseDTO<CheckUserDTO>> checkUser(HttpServletRequest request) throws Exception {
		String mail = authUtils.getMail(request);
		if (mail == null)
			throw new UnauthorizedAccessException("No tienes permiso para acceder a este recurso");
		return ResponseEntity.ok(new ResponseDTO<>("Tu cuenta existe", accountService.checkUser(mail)));
	}

	@Override
	@GetMapping("/validateMail")
	public ResponseEntity<ResponseDTO<Void>> validateMail(@Valid @Email @NotBlank @RequestParam("mail") String email) throws Exception {
		accountService.validateMail(email);
		return ResponseEntity.ok(new ResponseDTO<>("Tu cuenta si está activa", null));
	}

}
