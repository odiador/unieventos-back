package co.edu.uniquindio.unieventos.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import co.edu.uniquindio.unieventos.dto.auth.ChangePasswordDTO;
import co.edu.uniquindio.unieventos.dto.auth.CreateAccountDTO;
import co.edu.uniquindio.unieventos.dto.auth.LoginDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

public interface AuthController {

	ResponseEntity<?> createAccount(@Valid @RequestBody CreateAccountDTO account) throws Exception;

	ResponseEntity<?> sendRecuperationCode(@Email @RequestParam String email) throws Exception;

	ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO change) throws Exception;

	ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) throws Exception;
}
