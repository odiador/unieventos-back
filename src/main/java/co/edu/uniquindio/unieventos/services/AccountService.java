package co.edu.uniquindio.unieventos.services;

import org.springframework.stereotype.Service;

import co.edu.uniquindio.unieventos.dto.ChangePasswordDTO;
import co.edu.uniquindio.unieventos.dto.CreateAccountDTO;
import co.edu.uniquindio.unieventos.dto.LoginDTO;
import co.edu.uniquindio.unieventos.dto.UserDataDTO;
import co.edu.uniquindio.unieventos.model.Account;

@Service
public interface AccountService {

	Account createAccount(CreateAccountDTO account) throws Exception;

	String editAccount(UserDataDTO account) throws Exception;

	String deleteAccount(String id) throws Exception;

	UserDataDTO getAccountInfo(String id) throws Exception;

	String sendRecuperationCode(String email) throws Exception;

	String changePassword(ChangePasswordDTO change) throws Exception;

	String login(LoginDTO loginDTO) throws Exception;

}
