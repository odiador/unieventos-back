package co.edu.uniquindio.unieventos.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.unieventos.config.AuthUtils;
import co.edu.uniquindio.unieventos.controllers.AccountController;
import co.edu.uniquindio.unieventos.dto.auth.LoginDTO;
import co.edu.uniquindio.unieventos.dto.client.EditUserDataDTO;
import co.edu.uniquindio.unieventos.dto.client.UserDataDTO;
import co.edu.uniquindio.unieventos.exceptions.DocumentNotFoundException;
import co.edu.uniquindio.unieventos.services.AccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin
public class AccountControllerImpl implements AccountController {

	@Autowired
	private AccountService accountService;
	@Autowired
	private AuthUtils authUtils;

	@Override
	@PutMapping("/edit")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<?> editAccount(@Valid @RequestBody EditUserDataDTO account, HttpServletRequest request)
			throws Exception {
		String mail = authUtils.getMail(request);
		if (mail == null)
			throw new DocumentNotFoundException("Tu cuenta no fue encontrada");
		return ResponseEntity.ok(accountService.editAccount(mail, account));
	}

	@Override
	@DeleteMapping("/delete")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<?> deleteAccount(@Valid @RequestBody LoginDTO dto, HttpServletRequest request)
			throws Exception {
		authUtils.verifyMail(dto.email(), request);
		return ResponseEntity.ok(accountService.deleteAccount(dto));
	}

	@Override
	@GetMapping("/info")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<UserDataDTO> getAccountInfo(HttpServletRequest request) throws Exception {
		String mail = authUtils.getMail(request);
		if (mail == null)
			throw new DocumentNotFoundException("Tu cuenta no fue encontrada");
		UserDataDTO accountInfo = accountService.getAccountInfo(mail);
		return ResponseEntity.ok(accountInfo);
	}
}
