package co.edu.uniquindio.unieventos.services;

import co.edu.uniquindio.unieventos.dto.CouponMailSendDTO;
import co.edu.uniquindio.unieventos.dto.RecoveryPasswordMailSendDTO;
import co.edu.uniquindio.unieventos.dto.VerifyMailSendDTO;

public interface EmailService {

	void sendVerificationMail(VerifyMailSendDTO dto) throws Exception;

	void sendPasswordRecoveryMail(RecoveryPasswordMailSendDTO dto) throws Exception;

	void sendFirstPurchaseCouponMail(CouponMailSendDTO dto) throws Exception;
}
