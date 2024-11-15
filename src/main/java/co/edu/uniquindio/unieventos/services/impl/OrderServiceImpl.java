package co.edu.uniquindio.unieventos.services.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.AbstractMap.SimpleEntry;
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
import co.edu.uniquindio.unieventos.dto.coupons.AppliedCouponDTO;
import co.edu.uniquindio.unieventos.dto.exceptions.BiErrorStringDTO;
import co.edu.uniquindio.unieventos.dto.orders.CreateOrderDTO;
import co.edu.uniquindio.unieventos.dto.orders.FindOrderDTO;
import co.edu.uniquindio.unieventos.dto.orders.FindOrderDetailDTO;
import co.edu.uniquindio.unieventos.dto.orders.MercadoPagoURLDTO;
import co.edu.uniquindio.unieventos.dto.orders.OrderDTO;
import co.edu.uniquindio.unieventos.dto.orders.PurchaseDTO;
import co.edu.uniquindio.unieventos.exceptions.CartEmptyException;
import co.edu.uniquindio.unieventos.exceptions.ConflictException;
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
import co.edu.uniquindio.unieventos.services.CouponService;
import co.edu.uniquindio.unieventos.services.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private CalendarRepository calendarRepository;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private CouponRepository couponRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private CouponService couponService;

	@Autowired
	private MercadoPagoProps customProperties;
	@Autowired
	private Mappers mappers;

	@Override
	public MercadoPagoURLDTO realizarPago(String idOrden, String userId, String origin) throws Exception {

		// Obtener la orden guardada en la base de datos y los ítems de la orden
		Order ordenGuardada = getOrder(idOrden);
		if (ordenGuardada.getInitPoint() != null) {
			return new MercadoPagoURLDTO(ordenGuardada.getInitPoint());
		}
		if (!ordenGuardada.getClientId().toString().equals(userId))
			throw new UnauthorizedAccessException("La orden no está a tu nombre");
		float newPriceFactor = 1;
		Coupon coupon = null;
		if (ordenGuardada.getCouponId() != null) {
			coupon = couponRepository.findById(ordenGuardada.getCouponId().toString()).orElse(null);
			newPriceFactor = coupon == null ? 1 : (100f - coupon.getDiscount()) / 100f;
		}
		List<PreferenceItemRequest> itemsPasarela = new ArrayList<>();
		List<String> errors = new ArrayList<String>();
		// Recorrer los items de la orden y crea los ítems de la pasarela
		for (OrderDetail item : ordenGuardada.getItems()) {

			Calendar calendar = calendarRepository.findById(item.getCalendarId().toString())
					.orElseThrow(() -> new DocumentNotFoundException("Tu calendario no fue encontrado"));
			Event event;
			{
				Optional<Event> optional = calendar.getEvents().stream()
						.filter(e -> e.getId().equals(item.getEventId())).findFirst();
				if (optional.isEmpty()) {
					errors.add(String.format("El evento \"%s\" no fue encontrado en el calendario", item.getEventId()));
					continue;
				}
				event = optional.get();
			}
			Locality locality;
			{

				Optional<Locality> optional = event.getLocalities().stream()
						.filter(l -> l.getId().equals(item.getLocalityId())).findFirst();
				if (optional.isEmpty()) {
					errors.add(String.format("La localidad \"%s\" no fue encontrada en el evento %s",
							item.getLocalityId(), item.getEventId()));
					continue;
				}
				locality = optional.get();

			}
			float price = locality.getPrice() * (coupon == null ? 1
					: (coupon.isForSpecialEvent()
							? (coupon.getCalendarId().equals(calendar.getId())
									&& coupon.getEventId().equals(event.getId()) ? newPriceFactor : 1)
							: newPriceFactor));
			// Crear el item de la pasarela
			PreferenceItemRequest itemRequest = PreferenceItemRequest.builder().title(event.getName())
					.title(String.format("%s - %s", calendar.getName(), event.getName()))
					.pictureUrl(event.getEventImage())
					.categoryId(event.getType().name())
					.quantity(item.getQuantity())
					.currencyId("COP")
					.unitPrice(BigDecimal.valueOf(price)).build();

			itemsPasarela.add(itemRequest);
		}

		// Configurar las credenciales de MercadoPago
		MercadoPagoConfig.setAccessToken(customProperties.getAccesstoken());

		// Configurar las urls de retorno de la pasarela (Frontend)
		PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
				.success(String.format("%s/home/orders/%s", origin, idOrden))
				.failure(String.format("%s/home/orders/%s", origin, idOrden))
				.pending(String.format("%s/home/orders/%s", origin, idOrden)).build();

		// Construir la preferencia de la pasarela con los ítems, metadatos y urls de retorno
		String format = String.format("%s/api/orders/pay/notification", customProperties.getNgrokurl());
		PreferenceRequest preferenceRequest = PreferenceRequest.builder()
				.backUrls(backUrls)
				.expires(false)
				.items(itemsPasarela)
				.metadata(Map.of("id_orden", ordenGuardada.getId()))
				.notificationUrl(format)
				.build();
		// Crear la preferencia en la pasarela de MercadoPago
		PreferenceClient client = new PreferenceClient();
		Preference preference = client.create(preferenceRequest);
		// Guardar el código de la pasarela en la orden
		ordenGuardada.setInitPoint(preference.getInitPoint());
		orderRepository.save(ordenGuardada);
		return new MercadoPagoURLDTO(ordenGuardada.getInitPoint());
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
				List<Calendar> editedCalendars = new ArrayList<Calendar>();
				for (OrderDetail detail : orden.getItems()) {
					Calendar calendar = calendarRepository.findById(detail.getCalendarId()).orElse(null);
					if (calendar == null)
						throw new ConflictException(String.format("El calendario \"%s\" no fue encontrado", detail.getCalendarId()));
					Event event = findEvent(detail.getEventId(), calendar);
					if (event == null)
						throw new ConflictException(String.format("El evento \"%s\" no fue encontrado", detail.getEventId()));
					List<Locality> localities = event.getLocalities();
					SimpleEntry<Locality, Integer> localityWIndex = getLocality(detail.getLocalityId(), localities);
					if (localityWIndex == null)
						throw new ConflictException(String.format("La localidad \"%s\" no fue encontrada", detail.getLocalityId()));
					Locality locality = localityWIndex.getKey();
					if (locality.getFreeTickets() < detail.getQuantity())
						throw new ConflictException(String.format(
								"La localidad \"%s\" no tiene suficientes tickets disponibles", locality.getName()));
					locality.setTicketsSold(locality.getTicketsSold() + detail.getQuantity());
					event.updateLocality(locality, localityWIndex.getValue());
					calendar.updateEvent(event);
					editedCalendars.add(calendar);
				}
				calendarRepository.saveAll(editedCalendars);
				orden.setStatus(OrderStatus.PAID);
				orden.setPayment(pago);
				orderRepository.save(orden);
			}

		} catch (Exception e) {
			throw new PaymentException("No se pudo hacer el pago. Reason: " + e.getMessage());
		}
	}

	private SimpleEntry<Locality, Integer> getLocality(String id, List<Locality> localities) {
		for (int i = 0; i < localities.size(); i++) {
			Locality locality = localities.get(i);
			if (locality.getId().equals(id))
				return new SimpleEntry<Locality, Integer>(locality, i);
		}
		return null;
	}

	private Event findEvent(String id, Calendar calendar) {
		List<Event> events = calendar.getEvents();
		for (int i = 0; i < events.size(); i++) {
			Event event = events.get(i);
			if (event.getId().equals(id))
				return event;
		}
		return null;
	}

	@Override
	public Order getOrder(String idOrden) throws DocumentNotFoundException {
		return orderRepository.findById(idOrden).orElseThrow(() -> new DocumentNotFoundException("La orden no fue encontrada"));
	}

	@Override
	public OrderDTO createOrder(CreateOrderDTO dto) throws Exception {
		Cart cart = cartRepository.findById(dto.cartId())
				.orElseThrow(() -> new DocumentNotFoundException("El carrito con ese ID no fue encontrado"));
		Account account = accountRepository.findById(cart.getUserId())
				.orElseThrow(() -> new DocumentNotFoundException("El usuario en el carrito no fue encontrado"));
		if (!account.getEmail().equals(dto.email()))
			throw new UnauthorizedAccessException("Tu cuenta no coincide con la cuenta del carrito");

		if (cart.isEmpty())
			throw new CartEmptyException("El carrito no puede estar vacío");

		float newPriceFactor = 1;
		AppliedCouponDTO coupon = null;
		if (dto.couponCode() != null) {
			coupon = couponService.applyCouponByCode(dto.couponCode());
			newPriceFactor = (100f - coupon.discount()) / 100f;
		}
		List<BiErrorStringDTO> errors = new ArrayList<>();
		List<OrderDetail> items = new ArrayList<>();

		float subtotal = 0f;
		List<CartDetail> cartItems = cart.getItems();

		for (CartDetail detail : cartItems) {
			Calendar calendar = calendarRepository.findById(detail.getCalendarId()).orElse(null);
			if (calendar == null) {
				errors.add(new BiErrorStringDTO("calendar", detail.getCalendarId()));
				continue;
			}

			Event eventFound = calendar.getEvents().stream().filter(event -> event.getId().equals(detail.getEventId()))
					.findFirst().orElse(null);

			if (eventFound == null) {
				errors.add(new BiErrorStringDTO("event", detail.getEventId()));
				continue;
			}
			Locality locality = eventFound.getLocalities().stream()
					.filter(loc -> loc.getId().equals(detail.getLocalityId())).findFirst().orElse(null);

			if (locality == null) {
				errors.add(new BiErrorStringDTO("locality", detail.getLocalityId()));
				continue;
			}
			OrderDetail orderDetail = mappers.getCartToOrderMapper().apply(detail);
			float price = locality.getPrice();
			if (coupon != null) {
				if (coupon.forSpecialEvent()) {
					if (coupon.calendarId().equals(detail.getCalendarId())
							&& detail.getEventId().equals(coupon.eventId())) {
						price *= newPriceFactor;
					}
				} else {
					price *= newPriceFactor;
				}
			}
			orderDetail.setPrice(price);
			subtotal += price * detail.getQuantity();
			items.add(orderDetail);
		}
		if (!errors.isEmpty())
			throw new MultiErrorException("Errores en los detalles del carrito", errors, 400);
		if (coupon != null && coupon.isUnique()) {
			couponService.changeCouponStatus(dto.couponCode(), CouponStatus.UNAVAILABLE);
		}

		Order order = Order.builder().clientId(new ObjectId(cart.getUserId()))
				.couponId(coupon != null ? new ObjectId(coupon.id()) : null).timestamp(LocalDateTime.now()).items(items)
				.status(OrderStatus.CREATED).total(subtotal).build();

		return mappers.getOrderMapper().apply(orderRepository.save(order));
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
				.orElseThrow(() -> new DocumentNotFoundException("La cuenta no fue encontrada")).getId();
		List<Order> findByClientId = orderRepository.findByClientIdAndStatus(new ObjectId(id), OrderStatus.PAID);
		return findByClientId.stream().map(mappers.getOrderToPurchaseMapper()).collect(Collectors.toList());
	}

	@Override
	public FindOrderDTO getOrderDTO(String idOrden, String mail) throws Exception {
		Account account = accountRepository.findByEmail(mail)
				.orElseThrow(() -> new DocumentNotFoundException("Tu cuenta no fue encontrada"));
		Order order = getOrder(idOrden);
		if (!account.getId().equals(order.getClientId().toString()))
			new DocumentNotFoundException("No puedes ver esta orden");
		return mapOrderToFindOrderDTO(order);
	}

	private FindOrderDTO mapOrderToFindOrderDTO(Order order) {
		List<FindOrderDetailDTO> orderDetails = new ArrayList<FindOrderDetailDTO>();
		for (OrderDetail orderDetail : order.getItems()) {
			Calendar calendar = calendarRepository.findById(orderDetail.getCalendarId()).orElse(null);
			if (calendar == null)
				continue;
			String id = orderDetail.getLocalityId();
			Event event = findEvent(calendar, orderDetail.getEventId());
			if (event == null)
				continue;
			Locality locality = findLocality(id, event);
			if (locality == null)
				continue;

			orderDetails.add(new FindOrderDetailDTO(
					orderDetail.getCalendarId(),
					orderDetail.getEventId(),
					orderDetail.getLocalityId(),
					calendar.getName(),
					event.getName(),
					locality.getName(),
					event.getEventImage(),
					orderDetail.getPrice(),
					orderDetail.getQuantity()));
		}
		FindOrderDTO orderDto = new FindOrderDTO(
				order.getId(),
				order.getClientId(),
				order.getTimestamp().toString(),
				order.getInitPoint(),
				order.getPayment(), 
				orderDetails,
				order.getStatus().toString(),
				order.getTotal(),
				order.getCouponId());
		return orderDto;
	}

	private Locality findLocality(String id, Event event) {
		for (Locality l : event.getLocalities()) {
			if (l.getId().equals(id)) {
				return l;
			}
		}
		return null;
	}

	private Event findEvent(Calendar calendar, String id) {
		for (Event e : calendar.getEvents()) {
			if (e.getId().equals(id)) {
				return e;
			}
		}
		return null;
	}

	@Override
	public List<FindOrderDTO> getOrdersDTO(String mail) throws DocumentNotFoundException {
		Account account = accountRepository.findByEmail(mail)
				.orElseThrow(() -> new DocumentNotFoundException("Tu cuenta no fue encontrada"));
		return orderRepository.findByClientId(new ObjectId(account.getId())).stream()
				.map(order -> mapOrderToFindOrderDTO(order)).toList();
	}

}
