package co.edu.uniquindio.unieventos.controllers;

import org.springframework.http.ResponseEntity;

import co.edu.uniquindio.unieventos.dto.auth.LoginDTO;
import co.edu.uniquindio.unieventos.dto.client.EditUserDataDTO;
import co.edu.uniquindio.unieventos.dto.client.UserDataDTO;
import co.edu.uniquindio.unieventos.dto.misc.ResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


public interface AccountController {

	ResponseEntity<ResponseDTO<UserDataDTO>> editAccount(@Valid EditUserDataDTO account, HttpServletRequest request)
			throws Exception;

	ResponseEntity<ResponseDTO<String>> deleteAccount(@Valid LoginDTO dto, HttpServletRequest request) throws Exception;

	ResponseEntity<ResponseDTO<UserDataDTO>> getAccountInfo(HttpServletRequest request) throws Exception;



}
