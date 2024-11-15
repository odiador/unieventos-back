package co.edu.uniquindio.unieventos.services;

import co.edu.uniquindio.unieventos.dto.auth.RecoveryPasswordMailSendDTO;
import co.edu.uniquindio.unieventos.dto.auth.VerifyMailSendDTO;
import co.edu.uniquindio.unieventos.dto.coupons.CouponMailSendDTO;
import co.edu.uniquindio.unieventos.dto.orders.FindOrderDTO;
import co.edu.uniquindio.unieventos.exceptions.MailSendingException;

public interface EmailService {

	void sendVerificationMail(VerifyMailSendDTO dto) throws MailSendingException;

	void sendPasswordRecoveryMail(RecoveryPasswordMailSendDTO dto) throws MailSendingException;

	void sendFirstPurchaseCouponMail(CouponMailSendDTO dto) throws MailSendingException;

	void sendQRsOrder(String to, String subject, String text, FindOrderDTO orderDto) throws Exception;
}
