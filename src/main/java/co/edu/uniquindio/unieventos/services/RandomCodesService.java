package co.edu.uniquindio.unieventos.services;

public interface RandomCodesService {

	String getRandomCouponCode() throws Exception;

	String getPasswordRecoveryCode() throws Exception;

	String getAccountVerifyCode() throws Exception;

	String getRandomRegisterCode() throws Exception;
}
