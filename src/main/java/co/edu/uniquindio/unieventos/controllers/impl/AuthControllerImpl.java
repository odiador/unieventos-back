package co.edu.uniquindio.unieventos.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.unieventos.controllers.AuthController;
import co.edu.uniquindio.unieventos.dto.auth.ChangePasswordDTO;
import co.edu.uniquindio.unieventos.dto.auth.CreateAccountDTO;
import co.edu.uniquindio.unieventos.dto.auth.LoginDTO;
import co.edu.uniquindio.unieventos.model.Account;
import co.edu.uniquindio.unieventos.services.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
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
	public ResponseEntity<?> sendRecuperationCode(@Valid @Email @RequestParam String email) throws Exception {
		String result = accountService.sendRecuperationCode(email);
		return ResponseEntity.ok(result);
	}

	@Override
	@PostMapping("/password/change")
	public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDTO change) throws Exception {
		String result = accountService.changePassword(change);
		return ResponseEntity.ok(result);
	}

	@Override
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) throws Exception {
		return ResponseEntity.ok(accountService.login(loginDTO));
	}
}
