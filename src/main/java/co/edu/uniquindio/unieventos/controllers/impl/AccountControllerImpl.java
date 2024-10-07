package co.edu.uniquindio.unieventos.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.unieventos.config.AuthUtils;
import co.edu.uniquindio.unieventos.controllers.AccountController;
import co.edu.uniquindio.unieventos.dto.auth.LoginDTO;
import co.edu.uniquindio.unieventos.dto.client.EditUserDataDTO;
import co.edu.uniquindio.unieventos.dto.client.UserDataDTO;
import co.edu.uniquindio.unieventos.services.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin
@RequiredArgsConstructor
public class AccountControllerImpl implements AccountController {

	@Autowired
	private AccountService accountService;
	
	private final AuthUtils authUtils;

	@Override
	@PutMapping("/edit")
	public ResponseEntity<String> editAccount(@Valid @RequestBody EditUserDataDTO account, HttpServletRequest request)
			throws Exception {
		authUtils.verifyMail(account.email(), request);

		String result = accountService.editAccount(account);
		return ResponseEntity.ok(result);
	}


	@Override
	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteAccount(@Valid @RequestBody LoginDTO dto, HttpServletRequest request) throws Exception {
		authUtils.verifyMail(dto.email(), request);
		String result = accountService.deleteAccount(dto);
		return ResponseEntity.ok(result);
	}

	@Override
	@GetMapping("/info")
	public ResponseEntity<UserDataDTO> getAccountInfo(@RequestParam("email") String email) throws Exception {
		UserDataDTO accountInfo = accountService.getAccountInfo(email);
		return ResponseEntity.ok(accountInfo);
	}
}
