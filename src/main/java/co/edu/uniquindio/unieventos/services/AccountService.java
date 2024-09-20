package co.edu.uniquindio.unieventos.services;

import co.edu.uniquindio.unieventos.dto.ActivateAccountDTO;
import co.edu.uniquindio.unieventos.dto.ChangePasswordDTO;
import co.edu.uniquindio.unieventos.dto.CreateAccountDTO;
import co.edu.uniquindio.unieventos.dto.LoginDTO;
import co.edu.uniquindio.unieventos.dto.RecuperateAccountDTO;
import co.edu.uniquindio.unieventos.dto.TokenDTO;
import co.edu.uniquindio.unieventos.dto.UserDataDTO;
import co.edu.uniquindio.unieventos.exceptions.DelayException;
import co.edu.uniquindio.unieventos.exceptions.DocumentFoundException;
import co.edu.uniquindio.unieventos.exceptions.DocumentNotFoundException;
import co.edu.uniquindio.unieventos.exceptions.InvalidCodeException;
import co.edu.uniquindio.unieventos.exceptions.InvalidLoginException;
import co.edu.uniquindio.unieventos.exceptions.InvalidPasswordException;
import co.edu.uniquindio.unieventos.exceptions.InvalidUsernameException;
import co.edu.uniquindio.unieventos.exceptions.MailSendingException;
import co.edu.uniquindio.unieventos.model.Account;

public interface AccountService {

	Account createAccount(CreateAccountDTO account) throws DocumentFoundException, MailSendingException;

	String editAccount(UserDataDTO account) throws DocumentNotFoundException;

	String deleteAccount(String id) throws DocumentNotFoundException;

	UserDataDTO getAccountInfo(String id) throws DocumentNotFoundException;

	String sendRecuperationCode(String email) throws DocumentNotFoundException, MailSendingException, DelayException;

	String recuperateAccount(RecuperateAccountDTO dto) throws DocumentNotFoundException, InvalidCodeException;

	String activateAccount(ActivateAccountDTO dto) throws DocumentNotFoundException, InvalidCodeException;

	String changePassword(ChangePasswordDTO change) throws DocumentNotFoundException,InvalidPasswordException;

	TokenDTO login(LoginDTO loginDTO) throws InvalidLoginException, InvalidUsernameException;

}
