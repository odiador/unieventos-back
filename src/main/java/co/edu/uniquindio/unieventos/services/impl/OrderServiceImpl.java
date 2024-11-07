package co.edu.uniquindio.unieventos.services.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
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

import co.edu.uniquindio.unieventos.config.Mappers;
import co.edu.uniquindio.unieventos.config.MercadoPagoProps;
import co.edu.uniquindio.unieventos.dto.exceptions.BiErrorStringDTO;
import co.edu.uniquindio.unieventos.dto.orders.CreateOrderDTO;
import co.edu.uniquindio.unieventos.dto.orders.OrderDTO;
import co.edu.uniquindio.unieventos.dto.orders.PurchaseDTO;
import co.edu.uniquindio.unieventos.exceptions.CartEmptyException;
import co.edu.uniquindio.unieventos.exceptions.DocumentNotFoundException;
import co.edu.uniquindio.unieventos.exceptions.MultiErrorException;
import co.edu.uniquindio.unieventos.exceptions.PaymentException;
import co.edu.uniquindio.unieventos.exceptions.UnauthorizedAccessException;
import co.edu.uniquindio.unieventos.model.documents.Account;
import co.edu.uniquindio.unieventos.model.documents.Calendar;
import co.edu.uniquindio.unieventos.model.documents.Cart;
import co.edu.uniquindio.unieventos.model.documents.Coupon;
import co.edu.uniquindio.unieventos.model.documents.Order;
import co.edu.uniquindio.unieventos.model.enums.CouponStatus;
import co.edu.uniquindio.unieventos.model.enums.CouponType;
import co.edu.uniquindio.unieventos.model.enums.OrderStatus;
import co.edu.uniquindio.unieventos.model.vo.CartDetail;
import co.edu.uniquindio.unieventos.model.vo.Event;
import co.edu.uniquindio.unieventos.model.vo.Locality;
import co.edu.uniquindio.unieventos.model.vo.OrderDetail;
import co.edu.uniquindio.unieventos.repositories.AccountRepository;
import co.edu.uniquindio.unieventos.repositories.CalendarRepository;
import co.edu.uniquindio.unieventos.repositories.CartRepository;
import co.edu.uniquindio.unieventos.repositories.CouponRepository;
import co.edu.uniquindio.unieventos.repositories.OrderRepository;
import co.edu.uniquindio.unieventos.services.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private CalendarRepository calendarRepository;
	@Autowired
	private OrderRepository repo;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private CouponRepository couponRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private MercadoPagoProps customProperties;
	@Autowired
	private Mappers mappers;

	@Override
	public Preference realizarPago(String idOrden, String userId, String origin) throws Exception {

		// Obtener la orden guardada en la base de datos y los ítems de la orden
		Order ordenGuardada = getOrder(idOrden);
		if (!ordenGuardada.getClientId().toString().equals(userId)) 
			throw new UnauthorizedAccessException("La orden no está a tu nombre");
			
		List<PreferenceItemRequest> itemsPasarela = new ArrayList<>();
		List<String> errors = new ArrayList<String>();
		// Recorrer los items de la orden y crea los ítems de la pasarela
		for (OrderDetail item : ordenGuardada.getItems()) {

			Calendar calendar = calendarRepository.findById(item.getCalendarId().toString())
					.orElseThrow(() -> new DocumentNotFoundException("Tu calendario no fue encontrado"));
			Event event;
			{
				Optional<Event> optional = calendar.getEvents().stream()
						.filter(e -> e.getName().equals(item.getEventName())).findFirst();
				if (optional.isEmpty()) {
					errors.add(
							String.format("El evento \"%s\" no fue encontrado en el calendario", item.getEventName()));
					continue;
				}
				event = optional.get();
			}
			Locality locality;
			{

				Optional<Locality> optional = event.getLocalities().stream()
						.filter(l -> l.getName().equals(item.getLocalityName())).findFirst();
				if (optional.isEmpty()) {
					errors.add(String.format("La localidad \"%s\" no fue encontrada en el evento %s",
							item.getLocalityName(), item.getEventName()));
					continue;
				}
				locality = optional.get();

			}
			// Crear el item de la pasarela
			PreferenceItemRequest itemRequest = PreferenceItemRequest.builder().title(event.getName())
					.title(String.format("%s - %s", item.getCalendarId().toString(), event.getName()))
					.pictureUrl(event.getEventImage()).categoryId(event.getType().name()).quantity(item.getQuantity())
					.currencyId("COP").unitPrice(BigDecimal.valueOf(locality.getPrice())).build();

			itemsPasarela.add(itemRequest);
		}

		// Configurar las credenciales de MercadoPago
		MercadoPagoConfig.setAccessToken(customProperties.getAccesstoken());

		// Configurar las urls de retorno de la pasarela (Frontend)
		PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest
				.builder()
				.success(String.format("%s/home/orders/%s/status", origin, idOrden))
				.failure(String.format("%s/home/orders/%s/status", origin, idOrden))
				.pending(String.format("%s/home/orders/%s/status", origin, idOrden))
				.build();

		// Construir la preferencia de la pasarela con los ítems, metadatos y urls de
		// retorno
		String format = String.format("%s/api/orders/pay/notification", customProperties.getNgrokurl());
		PreferenceRequest preferenceRequest = PreferenceRequest.builder()
				.backUrls(backUrls).items(itemsPasarela)
				.metadata(Map.of("id_orden", ordenGuardada.getId()))
				.notificationUrl(format).build();
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

	@Override
	public OrderDTO createOrder(CreateOrderDTO dto) throws Exception {
		Cart cart = cartRepository.findById(dto.cartId())
				.orElseThrow(() -> new DocumentNotFoundException("El carrito con ese ID no fue encontrado"));
		Account account = accountRepository.findById(cart.getUserId())
				.orElseThrow(() -> new DocumentNotFoundException("El usuario en el carrito no fue encontrado"));
		if(!account.getEmail().equals(dto.email()))
			throw new UnauthorizedAccessException("Tu cuenta no coincide con la cuenta del carrito");

		if(cart.isEmpty())
			throw new CartEmptyException("El carrito no puede estar vacío");

		Coupon coupon = null;
		if (dto.couponCode() != null) {
			coupon = couponRepository.findByCode(dto.couponCode())
					.orElseThrow(() -> new DocumentNotFoundException("Cupón no encontrado"));

			if (coupon.getStatus() == CouponStatus.DELETED)
				throw new DocumentNotFoundException("Cupón no encontrado");
			if (coupon.getStatus() == CouponStatus.UNAVAILABLE)
				throw new DocumentNotFoundException("Cupón no disponible");
			if(!coupon.getExpiryDate().isAfter(LocalDateTime.now()))
				throw new DocumentNotFoundException("Tu cupón ya está vencido");
		}
		List<BiErrorStringDTO> errors = new ArrayList<>();
		List<OrderDetail> items = new ArrayList<>();

		float subtotal = 0f; 
		List<CartDetail> cartItems = cart.getItems();
		if (coupon != null && coupon.isForSpecialEvent()) {
			final Coupon aux = coupon;
			Calendar calendar = calendarRepository.findById(coupon.getCalendarId())
					.orElseThrow(() -> new DocumentNotFoundException("El calendario no se pudo encontrar"));

			Event eventFound = calendar.getEvents().stream().filter(event -> event.getName().equals(aux.getEventName()))
					.findFirst().orElseThrow(() -> new DocumentNotFoundException("El evento no se pudo encontrar"));

			for (CartDetail detail : cartItems) {
				if (!calendar.getId().equals(detail.getCalendarId())) {
					errors.add(new BiErrorStringDTO("calendar", calendar.getName()));
					continue;
				}

				if (!detail.getEventName().equals(eventFound.getName())) {
					errors.add(new BiErrorStringDTO("event", detail.getEventName()));
					continue;
				}

				Locality locality = eventFound.getLocalities().stream()
						.filter(loc -> loc.getName().equals(detail.getLocalityName())).findFirst().orElse(null);

				if (locality == null) {
					errors.add(new BiErrorStringDTO("locality", detail.getLocalityName()));
					continue;
				}

				OrderDetail orderDetail = mappers.getCartToOrderMapper().apply(detail);
				orderDetail.setPrice(locality.getPrice());
				subtotal += locality.getPrice();
				items.add(orderDetail);
			}

			if (!errors.isEmpty())
				throw new MultiErrorException("Errores en los detalles del carrito", errors, 400);

		} else {
			for (CartDetail detail : cart.getItems()) {
				Calendar calendar = calendarRepository.findById(detail.getCalendarId())
						.orElseThrow(() -> new DocumentNotFoundException("El calendario no se pudo encontrar"));

				Event eventFound = calendar.getEvents().stream()
						.filter(event -> event.getName().equals(detail.getEventName()))
						.findFirst()
						.orElse(null);

				if (eventFound == null) {
					errors.add(new BiErrorStringDTO("event", detail.getEventName()));
					continue;
				}
				Locality locality = eventFound.getLocalities().stream()
						.filter(loc -> loc.getName().equals(detail.getLocalityName()))
						.findFirst()
						.orElse(null);

				if (locality == null) {
					errors.add(new BiErrorStringDTO("locality", detail.getLocalityName()));
					continue;
				}
				OrderDetail orderDetail = mappers.getCartToOrderMapper().apply(detail);
				orderDetail.setPrice(locality.getPrice());
				subtotal += locality.getPrice();
				items.add(orderDetail);
			}
			if (!errors.isEmpty())
				throw new MultiErrorException("Errores en los detalles del carrito", errors, 400);
		}
		if (coupon.getType() == CouponType.UNIQUE)
			coupon.setStatus(CouponStatus.UNAVAILABLE);
		if (coupon != null)
			subtotal = subtotal * (100 - coupon.getDiscount());
		Order order = Order.builder()
				.clientId(new ObjectId(cart.getUserId()))
				.couponId(new ObjectId(coupon.getId()))
				.timestamp(LocalDateTime.now())
				.items(items)
				.status(OrderStatus.CREATED)
				.total(subtotal)
				.build();
		
		return mappers.getOrderMapper().apply(repo.save(order));
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

	@Override
	public List<PurchaseDTO> getPurchaseHistory(String mail) throws Exception {
		String id = accountRepository.findByEmail(mail)
				.orElseThrow(() -> new DocumentNotFoundException("La cuenta no fue encontrada"))
				.getId();
		List<Order> findByClientId = orderRepository.findByClientIdAndStatus(new ObjectId(id), OrderStatus.PAID);
		return findByClientId.stream().map(mappers.getOrderToPurchaseMapper()).collect(Collectors.toList());
	}

}
