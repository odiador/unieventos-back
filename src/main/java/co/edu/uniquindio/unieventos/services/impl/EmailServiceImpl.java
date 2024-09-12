package co.edu.uniquindio.unieventos.services.impl;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import co.edu.uniquindio.unieventos.dto.CouponMailSendDTO;
import co.edu.uniquindio.unieventos.dto.RecoveryPasswordMailSendDTO;
import co.edu.uniquindio.unieventos.dto.VerifyMailSendDTO;
import co.edu.uniquindio.unieventos.services.EmailService;

@Service
@Component
public class EmailServiceImpl implements EmailService {

	private final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

	@Value("${spring.mail.username}")
	private String sender;

	@Override
	public void sendVerificationMail(VerifyMailSendDTO dto) throws Exception {
	// TODO in quarantine

	}

	@Override
	public void sendPasswordRecoveryMail(RecoveryPasswordMailSendDTO dto) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendFirstPurchaseCouponMail(CouponMailSendDTO dto) throws Exception {
		// TODO Auto-generated method stub

	}

}
