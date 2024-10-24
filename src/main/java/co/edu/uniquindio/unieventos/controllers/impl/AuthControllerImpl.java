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
import co.edu.uniquindio.unieventos.dto.auth.CreateAccountDTO;
import co.edu.uniquindio.unieventos.dto.auth.LoginDTO;
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
	public ResponseEntity<?> createAccount(@Valid @RequestBody CreateAccountDTO account) throws Exception {
		return ResponseEntity.status(HttpStatus.CREATED).body(
				new ResponseDTO<Account>("Tu cuenta ha sido creada con éxito", 
						accountService.createAccount(account)));
	}

	@Override
	@PostMapping("/password/recovery")
	public ResponseEntity<?> sendRecuperationCode(@Valid @Email @RequestParam("email") String email) throws Exception {
		return ResponseEntity.ok(accountService.sendRecuperationCode(email));
	}

	@Override
	@PostMapping("/activation/send")
	public ResponseEntity<?> resendActivationCode(@Valid @Email @RequestParam("email") String email) throws Exception {
		return ResponseEntity.ok(accountService.resendActivationCode(email));
	}

	@Override
	@PostMapping("/activation/activate")
	public ResponseEntity<?> activateAccount(@Valid @RequestBody ActivateAccountDTO dto) throws Exception {
		return ResponseEntity.ok(accountService.activateAccount(dto));
	}

	@Override
	@PostMapping("/password/change")
	public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO change) throws Exception {
		return ResponseEntity.ok(accountService.changePassword(change));
	}
	
	@Override
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) throws Exception {
		return ResponseEntity.ok(accountService.login(loginDTO));
	}
	@Override
	@PostMapping("/checkUser")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<?> checkUser(HttpServletRequest request) throws Exception {
		String mail = authUtils.getMail(request);
		System.out.println("mail: " + mail);
		if (mail == null)
			throw new UnauthorizedAccessException("No tienes permiso para acceder a este recurso");
		return ResponseEntity.ok(accountService.checkUser(mail));
	}

	@Override
	@GetMapping("/validateMail")
	public ResponseEntity<?> validateMail(@Valid @Email @NotBlank @RequestParam("mail") String email) throws Exception {
		return ResponseEntity.ok(accountService.validateMail(email));
	}

}
