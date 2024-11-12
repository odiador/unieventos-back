package co.edu.uniquindio.unieventos.controllers;

import org.springframework.http.ResponseEntity;

import co.edu.uniquindio.unieventos.dto.auth.ActivateAccountDTO;
import co.edu.uniquindio.unieventos.dto.auth.ChangePasswordDTO;
import co.edu.uniquindio.unieventos.dto.auth.CheckUserDTO;
import co.edu.uniquindio.unieventos.dto.auth.CreateAccountDTO;
import co.edu.uniquindio.unieventos.dto.auth.LoginDTO;
import co.edu.uniquindio.unieventos.dto.auth.LoginResponseDTO;
import co.edu.uniquindio.unieventos.dto.misc.ResponseDTO;
import co.edu.uniquindio.unieventos.model.documents.Account;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface AuthController {

	ResponseEntity<ResponseDTO<Account>> createAccount(@Valid CreateAccountDTO account) throws Exception;

	ResponseEntity<ResponseDTO<Void>> sendRecuperationCode(@Valid @Email String email) throws Exception;

	ResponseEntity<ResponseDTO<Void>> resendActivationCode(@Valid @Email String email) throws Exception;

	ResponseEntity<ResponseDTO<Void>> activateAccount(@Valid ActivateAccountDTO dto) throws Exception;

	ResponseEntity<ResponseDTO<Void>> changePassword(@Valid ChangePasswordDTO change) throws Exception;

	ResponseEntity<ResponseDTO<LoginResponseDTO>> login(@Valid LoginDTO loginDTO) throws Exception;

	ResponseEntity<ResponseDTO<CheckUserDTO>> checkUser(HttpServletRequest request) throws Exception;

	ResponseEntity<ResponseDTO<Void>> validateMail(@Valid @Email @NotBlank String email) throws Exception;

}
