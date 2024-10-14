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

import co.edu.uniquindio.unieventos.controllers.AuthController;
import co.edu.uniquindio.unieventos.dto.auth.ActivateAccountDTO;
import co.edu.uniquindio.unieventos.dto.auth.ChangePasswordDTO;
import co.edu.uniquindio.unieventos.dto.auth.CreateAccountDTO;
import co.edu.uniquindio.unieventos.dto.auth.LoginDTO;
import co.edu.uniquindio.unieventos.model.documents.Account;
import co.edu.uniquindio.unieventos.services.AccountService;
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

	@Override
	@PostMapping("/create")
	public ResponseEntity<?> createAccount(@Valid @RequestBody CreateAccountDTO account) throws Exception {
		Account createdAccount = accountService.createAccount(account);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
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
	@GetMapping("/validateMail")
	public ResponseEntity<?> validateMail(@Valid @Email @NotBlank @RequestParam("mail") String email) throws Exception {
		return ResponseEntity.ok(accountService.validateMail(email));
	}

}
