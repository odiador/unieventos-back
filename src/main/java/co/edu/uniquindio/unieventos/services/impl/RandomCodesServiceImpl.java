package co.edu.uniquindio.unieventos.services.impl;

import java.security.SecureRandom;

import org.springframework.stereotype.Service;

import co.edu.uniquindio.unieventos.services.RandomCodesService;

@Service
public class RandomCodesServiceImpl implements RandomCodesService {

	private static final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private final SecureRandom random = new SecureRandom();

	@Override
	public String getRandomCouponCode() throws Exception {
		return generateCode(8);
	}

	@Override
	public String getPasswordRecoveryCode() throws Exception {
		return generateCode(6);
	}

	@Override
	public String getAccountVerifyCode() throws Exception {
		return generateCode(8);
	}

	@Override
	public String getRandomRegisterCode() throws Exception {
		return generateCode(8);
	}

	private String generateCode(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++)
			sb.append(chars.charAt(random.nextInt(chars.length())));
		return sb.toString();
	}
}
