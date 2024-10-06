package co.edu.uniquindio.unieventos.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import co.edu.uniquindio.unieventos.dto.client.UserDataDTO;
import jakarta.validation.Valid;


public interface AccountController {


	ResponseEntity<?> editAccount(@Valid @RequestBody UserDataDTO account) throws Exception;

	ResponseEntity<?> deleteAccount(@PathVariable String code) throws Exception;

	ResponseEntity<?> getAccountInfo(@PathVariable String id) throws Exception;

}
