package co.edu.uniquindio.unieventos.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import co.edu.uniquindio.unieventos.dto.auth.LoginDTO;
import co.edu.uniquindio.unieventos.dto.client.EditUserDataDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


public interface AccountController {


	ResponseEntity<?> editAccount(@Valid @RequestBody EditUserDataDTO account, HttpServletRequest request) throws Exception;

	ResponseEntity<?> getAccountInfo(HttpServletRequest request) throws Exception;

	ResponseEntity<String> deleteAccount(@Valid LoginDTO dto, HttpServletRequest request) throws Exception;

}
