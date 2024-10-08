package co.edu.uniquindio.unieventos.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;

import co.edu.uniquindio.unieventos.config.MercadoPagoProps;
import co.edu.uniquindio.unieventos.dto.calendar.CalendarDTO;
import co.edu.uniquindio.unieventos.dto.event.EventDTO;
import co.edu.uniquindio.unieventos.dto.event.ReturnLocalityDTO;
import co.edu.uniquindio.unieventos.exceptions.DocumentNotFoundException;
import co.edu.uniquindio.unieventos.exceptions.PaymentException;
import co.edu.uniquindio.unieventos.model.documents.Order;
import co.edu.uniquindio.unieventos.model.vo.OrderDetail;
import co.edu.uniquindio.unieventos.repositories.OrderRepository;
import co.edu.uniquindio.unieventos.services.CalendarService;
import co.edu.uniquindio.unieventos.services.OrderService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private CalendarService calendarService;
	@Autowired
	private OrderRepository repo;
	@Autowired
	private MercadoPagoProps customProperties;

	@Override
	public Preference realizarPago(String idOrden) throws Exception {

		// Obtener la orden guardada en la base de datos y los ítems de la orden
		Order ordenGuardada = getOrder(idOrden);
		List<PreferenceItemRequest> itemsPasarela = new ArrayList<>();
		List<String> errors = new ArrayList<String>();
		// Recorrer los items de la orden y crea los ítems de la pasarela
		for (OrderDetail item : ordenGuardada.getItems()) {

			CalendarDTO calendar = calendarService.findCalendarById(item.getCalendarId().toString());
			EventDTO event;
			{
				Optional<EventDTO> optional = calendar.events().stream()
						.filter(e -> e.name().equals(item.getEventName())).findFirst();
				if (optional.isEmpty()) {
					errors.add(
							String.format("El evento \"%s\" no fue encontrado en el calendario", item.getEventName()));
					continue;
				}
				event = optional.get();
			}
			ReturnLocalityDTO locality;
			{

				Optional<ReturnLocalityDTO> optional = event.localities().stream()
						.filter(l -> l.name().equals(item.getLocalityName())).findFirst();
				if (optional.isEmpty()) {
					errors.add(String.format("La localidad \"%s\" no fue encontrada en el evento %s",
							item.getLocalityName(), item.getEventName()));
					continue;
				}
				locality = optional.get();

			}
			// Crear el item de la pasarela
			PreferenceItemRequest itemRequest = PreferenceItemRequest.builder().id(event.name())
					.title(String.format("%s - %s", item.getCalendarId().toString(), event.name()))
					.pictureUrl(event.eventImage()).categoryId(event.type().name()).quantity(item.getQuantity())
					.currencyId("COP").unitPrice(BigDecimal.valueOf(locality.price())).build();

			itemsPasarela.add(itemRequest);
		}

		// Configurar las credenciales de MercadoPago
		MercadoPagoConfig.setAccessToken(customProperties.getAccesstoken());

		// Configurar las urls de retorno de la pasarela (Frontend)
		PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder().success("URL PAGO EXITOSO")
				.failure("URL PAGO FALLIDO").pending("URL PAGO PENDIENTE").build();

		// Construir la preferencia de la pasarela con los ítems, metadatos y urls de
		// retorno
		String format = String.format("%s/api/orders/pay/notification", customProperties.getNgrokurl());
		PreferenceRequest preferenceRequest = PreferenceRequest.builder().backUrls(backUrls).items(itemsPasarela)
				.metadata(Map.of("id_orden", ordenGuardada.getId())).notificationUrl(format).build();

		// Crear la preferencia en la pasarela de MercadoPago
		PreferenceClient client = new PreferenceClient();
		Preference preference = client.create(preferenceRequest);

		// Guardar el código de la pasarela en la orden
		ordenGuardada.setId(preference.getId());
		repo.save(ordenGuardada);

		return preference;
	}

	@Override
	public void receiveMercadoPagoNotification(Map<String, Object> request) throws PaymentException {
		try {

			// Obtener el tipo de notificación
			Object tipo = request.get("type");

			// Si la notificación es de un pago entonces obtener el pago y la orden asociada
			if ("payment".equals(tipo)) {

				// Capturamos el JSON que viene en el request y lo convertimos a un String
				String input = request.get("data").toString();

				// Extraemos los números de la cadena, es decir, el id del pago
				String idPago = input.replaceAll("\\D+", "");

				// Se crea el cliente de MercadoPago y se obtiene el pago con el id
				PaymentClient client = new PaymentClient();
				Payment payment = client.get(Long.parseLong(idPago));

				// Obtener el id de la orden asociada al pago que viene en los metadatos
				String idOrden = payment.getMetadata().get("id_orden").toString();

				// Se obtiene la orden guardada en la base de datos y se le asigna el pago
				Order orden = getOrder(idOrden);
				co.edu.uniquindio.unieventos.model.vo.Payment pago = createPayment(payment);
				orden.setPayment(pago);
				repo.save(orden);
			}

		} catch (Exception e) {
			throw new PaymentException("No se pudo hacer el pago. Reason:" + e.getMessage());
		}
	}

	@Override
	public Order getOrder(String idOrden) throws DocumentNotFoundException {
		return repo.findById(idOrden).orElseThrow(() -> new DocumentNotFoundException("La orden no fue encontrada"));
	}

	private co.edu.uniquindio.unieventos.model.vo.Payment createPayment(Payment payment) {
		co.edu.uniquindio.unieventos.model.vo.Payment pago = new co.edu.uniquindio.unieventos.model.vo.Payment();
		pago.setId(payment.getId().toString());
		pago.setTimestamp(payment.getDateCreated().toLocalDateTime());
		pago.setStatus(payment.getStatus());
		pago.setStatusDetail(payment.getStatusDetail());
		pago.setPaymentType(payment.getPaymentTypeId());
		pago.setCurrency(payment.getCurrencyId());
		pago.setAuthorizationCode(payment.getAuthorizationCode());
		pago.setTransationValue(payment.getTransactionAmount().floatValue());
		return pago;
	}

}
