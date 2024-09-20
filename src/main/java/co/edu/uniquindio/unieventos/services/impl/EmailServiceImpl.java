package co.edu.uniquindio.unieventos.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import co.edu.uniquindio.unieventos.dto.CouponMailSendDTO;
import co.edu.uniquindio.unieventos.dto.RecoveryPasswordMailSendDTO;
import co.edu.uniquindio.unieventos.dto.VerifyMailSendDTO;
import co.edu.uniquindio.unieventos.exceptions.MailSendingException;
import co.edu.uniquindio.unieventos.services.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public void sendVerificationMail(VerifyMailSendDTO dto) throws MailSendingException {
        SimpleMailMessage mimeMessage = new SimpleMailMessage();

        mimeMessage.setFrom("amaeventosuq@gmail.com");
        mimeMessage.setTo(dto.email());
        mimeMessage.setSubject("Registro en Unieventos | Confirma tu cuenta");
        mimeMessage.setText(String.format("Tu codigo es: %s", dto.verificationCode()));
        mimeMessage.setCc(dto.email());
        sendMessage(mimeMessage);
    }

    @Override
    public void sendPasswordRecoveryMail(RecoveryPasswordMailSendDTO dto) throws MailSendingException {
        SimpleMailMessage mimeMessage = new SimpleMailMessage();

        mimeMessage.setFrom("amaeventosuq@gmail.com");
        mimeMessage.setTo(dto.email());
        mimeMessage.setSubject("¿Olvidaste tu contraseña? | Recupera tu cuenta");
        mimeMessage.setText(String.format("Tu codigo para recuperar tu cuenta es: %s", dto.code()));
        mimeMessage.setCc(dto.email());
        sendMessage(mimeMessage);
    }

    @Override
    public void sendFirstPurchaseCouponMail(CouponMailSendDTO dto) throws MailSendingException {
        SimpleMailMessage mimeMessage = new SimpleMailMessage();

        mimeMessage.setFrom("amaeventosuq@gmail.com");
        mimeMessage.setTo(dto.email());
        mimeMessage.setSubject("Primera compra en AmaEventos | Cupón de 10% de DCTO");
        mimeMessage.setText(String.format("Tu codigo de cupón para un 10%% de descuento es: %s", dto.couponCode()));
        mimeMessage.setCc(dto.email());
        sendMessage(mimeMessage);
    }

	private void sendMessage(SimpleMailMessage mimeMessage) throws MailSendingException {
		try {
			mailSender.send(mimeMessage);
		} catch (MailException e) {
			throw new MailSendingException(e.getMessage());
		}
	}

}
