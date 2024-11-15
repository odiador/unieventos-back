package co.edu.uniquindio.unieventos.services.impl;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import co.edu.uniquindio.unieventos.dto.auth.RecoveryPasswordMailSendDTO;
import co.edu.uniquindio.unieventos.dto.auth.VerifyMailSendDTO;
import co.edu.uniquindio.unieventos.dto.coupons.CouponMailSendDTO;
import co.edu.uniquindio.unieventos.dto.orders.FindOrderDTO;
import co.edu.uniquindio.unieventos.dto.orders.FindOrderDetailDTO;
import co.edu.uniquindio.unieventos.exceptions.MailSendingException;
import co.edu.uniquindio.unieventos.services.EmailService;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;

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

	private byte[] generarQr(FindOrderDetailDTO orderDetail)
			throws Exception {
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");
		String formato = "BEGIN:VEVENT\nSUMMARY:%s\nDTSTART:%s\nDTEND:%s\nLOCATION:%s\nDESCRIPTION:Calendario %s\\nPrecio %s\\nCantidad %s\\nSubtotal %s\nEND:VEVENT";
		String data = String.format(formato,
				String.format("%s (%s)", orderDetail.eventName(), orderDetail.localityName()),
				orderDetail.startTime().format(formatter),
				orderDetail.endTime().format(formatter),
				orderDetail.city(),
				orderDetail.calendarName(),
				String.format("%.2f", (orderDetail.price())),
				orderDetail.quantity() + "",
				String.format("%.2f", (orderDetail.price() * orderDetail.quantity())));

		BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 500, 500);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(matrix, "jpg", baos);
		return baos.toByteArray();
	}

	@Override
	@Async
	public void sendQRsOrder(String to, String subject, String text,
			FindOrderDTO orderDto) throws Exception {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(text);
		helper.setFrom("amaeventosuq@gmail.com");

		List<FindOrderDetailDTO> items = orderDto.items();
		for (int i = 0; i < items.size(); i++) {
			FindOrderDetailDTO findOrderDetailDTO = items.get(i);
			ByteArrayDataSource qrDataSource = new ByteArrayDataSource(
					generarQr(findOrderDetailDTO), "image/jpeg");
			helper.addAttachment("Detail_" + i + ".jpg", qrDataSource);
		}

		mailSender.send(message);
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
