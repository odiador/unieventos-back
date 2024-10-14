package co.edu.uniquindio.unieventos.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import co.edu.uniquindio.unieventos.dto.auth.ActivateAccountDTO;
import co.edu.uniquindio.unieventos.dto.auth.ChangePasswordDTO;
import co.edu.uniquindio.unieventos.dto.auth.CreateAccountDTO;
import co.edu.uniquindio.unieventos.dto.auth.LoginDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface AuthController {

	ResponseEntity<?> createAccount(@Valid @RequestBody CreateAccountDTO account) throws Exception;

	ResponseEntity<?> sendRecuperationCode(@Valid @Email @RequestParam("email") String email) throws Exception;

	ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO change) throws Exception;

	ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) throws Exception;

	ResponseEntity<?> resendActivationCode(@Valid @Email @RequestParam("email") String email) throws Exception;

	ResponseEntity<?> validateMail(@Valid @Email @NotBlank @RequestParam("mail") String email) throws Exception;

	ResponseEntity<?> activateAccount(@Valid ActivateAccountDTO dto) throws Exception;
}
