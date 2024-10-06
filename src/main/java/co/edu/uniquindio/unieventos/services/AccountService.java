package co.edu.uniquindio.unieventos.services;

import co.edu.uniquindio.unieventos.dto.auth.ActivateAccountDTO;
import co.edu.uniquindio.unieventos.dto.auth.ChangePasswordDTO;
import co.edu.uniquindio.unieventos.dto.auth.CreateAccountDTO;
import co.edu.uniquindio.unieventos.dto.auth.LoginDTO;
import co.edu.uniquindio.unieventos.dto.auth.RecuperateAccountDTO;
import co.edu.uniquindio.unieventos.dto.auth.TokenDTO;
import co.edu.uniquindio.unieventos.dto.client.UserDataDTO;
import co.edu.uniquindio.unieventos.exceptions.DelayException;
import co.edu.uniquindio.unieventos.exceptions.DocumentFoundException;
import co.edu.uniquindio.unieventos.exceptions.DocumentNotFoundException;
import co.edu.uniquindio.unieventos.exceptions.InvalidCodeException;
import co.edu.uniquindio.unieventos.exceptions.InvalidLoginException;
import co.edu.uniquindio.unieventos.exceptions.InvalidPasswordException;
import co.edu.uniquindio.unieventos.exceptions.InvalidUsernameException;
import co.edu.uniquindio.unieventos.exceptions.MailSendingException;
import co.edu.uniquindio.unieventos.model.Account;
import jakarta.validation.Valid;

public interface AccountService {

	Account createAccount(@Valid CreateAccountDTO account) throws DocumentFoundException, MailSendingException;

	String editAccount(@Valid UserDataDTO account) throws DocumentNotFoundException;

	String deleteAccount(String id) throws DocumentNotFoundException;

	UserDataDTO getAccountInfo(String id) throws DocumentNotFoundException;

	String sendRecuperationCode(String email) throws DocumentNotFoundException, MailSendingException, DelayException;

	String recuperateAccount(@Valid RecuperateAccountDTO dto) throws DocumentNotFoundException, InvalidCodeException;

	String activateAccount(@Valid ActivateAccountDTO dto) throws DocumentNotFoundException, InvalidCodeException;

	String changePassword(@Valid ChangePasswordDTO change) throws DocumentNotFoundException, InvalidPasswordException;

	TokenDTO login(@Valid LoginDTO loginDTO) throws InvalidLoginException, InvalidUsernameException;

}
