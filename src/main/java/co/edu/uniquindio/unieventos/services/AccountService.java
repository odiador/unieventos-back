package co.edu.uniquindio.unieventos.services;

import co.edu.uniquindio.unieventos.dto.auth.ActivateAccountDTO;
import co.edu.uniquindio.unieventos.dto.auth.ChangePasswordDTO;
import co.edu.uniquindio.unieventos.dto.auth.CreateAccountDTO;
import co.edu.uniquindio.unieventos.dto.auth.LoginDTO;
import co.edu.uniquindio.unieventos.dto.auth.TokenDTO;
import co.edu.uniquindio.unieventos.dto.client.EditUserDataDTO;
import co.edu.uniquindio.unieventos.dto.client.UserDataDTO;
import co.edu.uniquindio.unieventos.exceptions.ConflictException;
import co.edu.uniquindio.unieventos.exceptions.DocumentFoundException;
import co.edu.uniquindio.unieventos.exceptions.DocumentNotFoundException;
import co.edu.uniquindio.unieventos.exceptions.InvalidCodeException;
import co.edu.uniquindio.unieventos.exceptions.MailSendingException;
import co.edu.uniquindio.unieventos.model.documents.Account;
import jakarta.validation.Valid;

public interface AccountService {

	Account createAccount(@Valid CreateAccountDTO account) throws DocumentFoundException, MailSendingException;

	String editAccount(String mail, @Valid EditUserDataDTO account) throws Exception;

	String deleteAccount(@Valid LoginDTO dto) throws Exception;

	UserDataDTO getAccountInfo(String id) throws DocumentNotFoundException;

	String sendRecuperationCode(String email) throws Exception;

	String resendActivationCode(String email) throws Exception;

	String activateAccount(@Valid ActivateAccountDTO dto) throws DocumentNotFoundException, InvalidCodeException, ConflictException;

	String changePassword(@Valid ChangePasswordDTO change) throws DocumentNotFoundException, InvalidCodeException;

	TokenDTO login(@Valid LoginDTO loginDTO) throws Exception;

}
