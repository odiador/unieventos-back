package co.edu.uniquindio.unieventos.services;

import co.edu.uniquindio.unieventos.dto.auth.ActivateAccountDTO;
import co.edu.uniquindio.unieventos.dto.auth.ChangePasswordDTO;
import co.edu.uniquindio.unieventos.dto.auth.CheckUserDTO;
import co.edu.uniquindio.unieventos.dto.auth.CreateAccountDTO;
import co.edu.uniquindio.unieventos.dto.auth.LoginDTO;
import co.edu.uniquindio.unieventos.dto.auth.LoginResponseDTO;
import co.edu.uniquindio.unieventos.dto.client.EditUserDataDTO;
import co.edu.uniquindio.unieventos.dto.client.UserDataDTO;
import co.edu.uniquindio.unieventos.dto.misc.ResponseDTO;
import co.edu.uniquindio.unieventos.exceptions.ConflictException;
import co.edu.uniquindio.unieventos.exceptions.DocumentFoundException;
import co.edu.uniquindio.unieventos.exceptions.DocumentNotFoundException;
import co.edu.uniquindio.unieventos.exceptions.InvalidCodeException;
import co.edu.uniquindio.unieventos.exceptions.MailSendingException;
import co.edu.uniquindio.unieventos.model.documents.Account;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public interface AccountService {

	Account createAccount(@Valid CreateAccountDTO account) throws DocumentFoundException, MailSendingException;

	UserDataDTO editAccount(String mail, @Valid EditUserDataDTO account) throws Exception;

	void deleteAccount(@Valid LoginDTO dto) throws Exception;

	UserDataDTO getAccountInfo(String id) throws DocumentNotFoundException;

	void sendRecuperationCode(String email) throws Exception;

	void resendActivationCode(String email) throws Exception;

	void activateAccount(@Valid ActivateAccountDTO dto) throws DocumentNotFoundException, InvalidCodeException, ConflictException;

	void changePassword(@Valid ChangePasswordDTO change) throws DocumentNotFoundException, InvalidCodeException;

	LoginResponseDTO login(@Valid LoginDTO loginDTO) throws Exception;

	void validateMail(@Valid @Email String email) throws Exception;

	CheckUserDTO checkUser(@Email @NotNull String mail) throws Exception;

}
