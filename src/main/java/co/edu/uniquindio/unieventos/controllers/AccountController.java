package co.edu.uniquindio.unieventos.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import co.edu.uniquindio.unieventos.dto.ChangePasswordDTO;
import co.edu.uniquindio.unieventos.dto.CreateAccountDTO;
import co.edu.uniquindio.unieventos.dto.LoginDTO;
import co.edu.uniquindio.unieventos.dto.UserDataDTO;
import co.edu.uniquindio.unieventos.model.Account;


public interface AccountController {

	ResponseEntity<Account> createAccount(@RequestBody CreateAccountDTO account) throws Exception;

	ResponseEntity<String> editAccount(@RequestBody UserDataDTO account) throws Exception;

	ResponseEntity<String> deleteAccount(@PathVariable String id) throws Exception;

	ResponseEntity<UserDataDTO> getAccountInfo(@PathVariable String id) throws Exception;

	ResponseEntity<String> sendRecuperationCode(@RequestParam String email) throws Exception;

	ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO change) throws Exception;

	ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) throws Exception;

}
