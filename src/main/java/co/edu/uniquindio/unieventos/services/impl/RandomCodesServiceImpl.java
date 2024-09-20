package co.edu.uniquindio.unieventos.services.impl;

import java.security.SecureRandom;

import org.springframework.stereotype.Service;

import co.edu.uniquindio.unieventos.services.RandomCodesService;

@Service
public class RandomCodesServiceImpl implements RandomCodesService {

	private static final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private final SecureRandom random = new SecureRandom();

	@Override
	public String getRandomCouponCode() {
		return generateCode(8);
	}

	@Override
	public String getPasswordRecoveryCode() {
		return generateCode(6);
	}

	@Override
	public String getAccountVerifyCode() {
		return generateCode(8);
	}

	@Override
	public String getRandomRegisterCode() {
		return generateCode(8);
	}

	private String generateCode(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++)
			sb.append(chars.charAt(random.nextInt(chars.length())));
		return sb.toString();
	}
}
