package co.edu.uniquindio.unieventos.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.unieventos.controllers.AccountController;
import co.edu.uniquindio.unieventos.dto.ChangePasswordDTO;
import co.edu.uniquindio.unieventos.dto.CreateAccountDTO;
import co.edu.uniquindio.unieventos.dto.LoginDTO;
import co.edu.uniquindio.unieventos.dto.UserDataDTO;
import co.edu.uniquindio.unieventos.model.Account;
import co.edu.uniquindio.unieventos.services.AccountService;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin
public class AccountControllerImpl implements AccountController {

	@Autowired
	private AccountService accountService;

	@Override
	@PostMapping("/create")
	public ResponseEntity<Account> createAccount(@RequestBody CreateAccountDTO account) throws Exception {
		Account createdAccount = accountService.createAccount(account);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
	}

	@Override
	@PutMapping("/edit")
	public ResponseEntity<String> editAccount(@RequestBody UserDataDTO account) throws Exception {
		String result = accountService.editAccount(account);
		return ResponseEntity.ok(result);
	}

	@Override
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteAccount(@PathVariable String id) throws Exception {
		String result = accountService.deleteAccount(id);
		return ResponseEntity.ok(result);
	}

	@Override
	@GetMapping("/{id}")
	public ResponseEntity<UserDataDTO> getAccountInfo(@PathVariable String id) throws Exception {
		UserDataDTO accountInfo = accountService.getAccountInfo(id);
		return ResponseEntity.ok(accountInfo);
	}

	@Override
	@PostMapping("/password/recovery")
	public ResponseEntity<String> sendRecuperationCode(@RequestParam String email) throws Exception {
		String result = accountService.sendRecuperationCode(email);
		return ResponseEntity.ok(result);
	}

	@Override
	@PostMapping("/password/change")
	public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO change) throws Exception {
		String result = accountService.changePassword(change);
		return ResponseEntity.ok(result);
	}

	@Override
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) throws Exception {
		String token = accountService.login(loginDTO);
		return ResponseEntity.ok(token);
	}
}
