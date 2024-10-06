package co.edu.uniquindio.unieventos.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.unieventos.controllers.AccountController;
import co.edu.uniquindio.unieventos.dto.client.UserDataDTO;
import co.edu.uniquindio.unieventos.services.AccountService;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin
public class AccountControllerImpl implements AccountController {

	@Autowired
	private AccountService accountService;

	@Override
	@PutMapping("/edit")
	public ResponseEntity<String> editAccount(@RequestBody UserDataDTO account) throws Exception {
		String result = accountService.editAccount(account);
		return ResponseEntity.ok(result);
	}

	@Override
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteAccount(@PathVariable String code) throws Exception {
		// TODO uso esto? ResponseEntity<String> deleteAccount(@PathVariable String code, HttpServletRequest request)
		String result = accountService.deleteAccount(code);
		return ResponseEntity.ok(result);
	}

	@Override
	@GetMapping("/info/{id}")
	public ResponseEntity<UserDataDTO> getAccountInfo(@PathVariable String id) throws Exception {
		UserDataDTO accountInfo = accountService.getAccountInfo(id);
		return ResponseEntity.ok(accountInfo);
	}
}
