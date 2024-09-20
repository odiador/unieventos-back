package co.edu.uniquindio.unieventos.services;

public interface RandomCodesService {

	String getRandomCouponCode();

	String getPasswordRecoveryCode();

	String getAccountVerifyCode();

	String getRandomRegisterCode();
}
