package co.edu.uniquindio.unieventos.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniquindio.unieventos.dto.carts.AddItemCartDTO;
import co.edu.uniquindio.unieventos.dto.carts.CartDTO;
import co.edu.uniquindio.unieventos.dto.carts.CartDetailDTO;
import co.edu.uniquindio.unieventos.dto.carts.ExistsCartItemDTO;
import co.edu.uniquindio.unieventos.dto.carts.RemoveItemCartDTO;
import co.edu.uniquindio.unieventos.dto.misc.ResponseDTO;
import co.edu.uniquindio.unieventos.exceptions.ConflictException;
import co.edu.uniquindio.unieventos.exceptions.DocumentNotFoundException;
import co.edu.uniquindio.unieventos.exceptions.MaxCartsCreatedException;
import co.edu.uniquindio.unieventos.misc.validation.ValidObjectId;
import co.edu.uniquindio.unieventos.model.documents.Calendar;
import co.edu.uniquindio.unieventos.model.documents.Cart;
import co.edu.uniquindio.unieventos.model.vo.CartDetail;
import co.edu.uniquindio.unieventos.model.vo.Event;
import co.edu.uniquindio.unieventos.model.vo.Locality;
import co.edu.uniquindio.unieventos.repositories.CalendarRepository;
import co.edu.uniquindio.unieventos.repositories.CartRepository;
import co.edu.uniquindio.unieventos.services.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private CalendarRepository calendarRepository;

	@Override
	public ResponseDTO<CartDTO> createCart(String userId) throws Exception {
		if (cartRepository.findByUserId(userId).size() >= 3)
			throw new MaxCartsCreatedException("La cantidad máxima de carritos es de 3");
		Cart cart = Cart.builder()
				.date(LocalDateTime.now())
				.userId(userId)
				.items(new ArrayList<CartDetail>())
				.build();
		cart = cartRepository.save(cart);
		return new ResponseDTO<CartDTO>("Carrito creado", buildCartDTO(cart));
	}

	@Override
	public ResponseDTO<CartDTO> getCartById(String id, String idCart) throws Exception {
		Cart cart = cartRepository.findByIdAndUserId(idCart, id)
				.orElseThrow(() -> new DocumentNotFoundException("El carrito no pudo ser obtenido"));
		return new ResponseDTO<CartDTO>("Carrito encontrado", buildCartDTO(cart));
	}

	private CartDTO buildCartDTO(Cart cart) {
		Map<String, Calendar> calendars = calendarRepository
				.findAllById(cart.getItems()
						.stream().distinct()
						.map(CartDetail::getCalendarId)
						.collect(Collectors.toList()))
				.stream()
				.collect(Collectors.toMap(Calendar::getId, c -> c));

		List<CartDetailDTO> itemsWithPrices = cart.getItems()
				.stream().map(item -> mapCartDetailToDTO(calendars, item))
				.filter(c -> c != null)
				.collect(Collectors.toList());

		CartDTO cartDTO = new CartDTO(
				cart.getId(),
				cart.getDate(),
				itemsWithPrices,
				cart.getUserId()
		);
		return cartDTO;
	}

	private CartDetailDTO mapCartDetailToDTO(Map<String, Calendar> calendars, CartDetail item) {
		Calendar calendar = calendars.get(item.getCalendarId());
		if (calendar == null)
			return null;

		Event event = null;
		for (Event e : calendar.getEvents()) {
			if (e.getName().equals(item.getEventName())) {
				event = e;
				break;
			}
		}
		if (event == null)
			return null;
		Locality locality = null;
		for (Locality loc : event.getLocalities()) {
			if (loc.getName().equals(item.getLocalityName())) {
				locality = loc;
				break;
			}
		}
		if (locality == null)
			return null;

		return new CartDetailDTO(
				item.getQuantity(),
				item.getCalendarId(),
				item.getEventName(),
				item.getLocalityName(),
				locality.getPrice(),
				calendar.getName(),
				event.getEventImage(),
				locality.getFreeTickets()
				);
	}

	@Override
	public ResponseDTO<List<CartDTO>> getCartsByUserId(String userId) {
		return new ResponseDTO<List<CartDTO>>("Carritos encontrados", 
				cartRepository.findByUserId(userId).stream()
						.map(c -> buildCartDTO(c)).collect(Collectors.toList()));
	}

	@Override
	public ResponseDTO<CartDTO> clearCart(String id, String userId) throws Exception {
		Cart cart = cartRepository.findByIdAndUserId(id, userId)
				.orElseThrow(() -> new DocumentNotFoundException("El carrito no pudo ser obtenido"));
		cart.setItems(List.of());
		return new ResponseDTO<CartDTO>("Carrito limpiado", buildCartDTO(cart));
	}

	@Override
	public void deleteCartById(String userId, String id) throws Exception {
		if (cartRepository.findByIdAndUserId(id, userId).isEmpty())
			throw new DocumentNotFoundException("El carrito no fue encontrado");
		cartRepository.deleteById(id);
	}

	@Override
	public ResponseDTO<Boolean> saveItemToCart(AddItemCartDTO dto, String id) throws Exception {
		Cart cart = cartRepository.findByIdAndUserId(dto.cartId(), id).orElseThrow(() -> new DocumentNotFoundException("El carrito no fue encontrado"));
		CartDetail found = null;
		int index = -1;
		List<CartDetail> items = cart.getItems();
		for (int i = 0; i < items.size(); i++) {
			CartDetail detail = items.get(i);
			if(detail.getCalendarId().equals(dto.calendarId())&&
					detail.getEventName().equals(dto.eventName())&&
					detail.getLocalityName().equals(dto.localityName())) {
				found = detail;
				index = i;
				break;
				
			}
		}
		Calendar calendar = calendarRepository.findById(dto.calendarId())
				.orElseThrow(() -> new DocumentNotFoundException("El calendario no fue encontrado"));
		Event event = findEvent(dto.eventName(), calendar.getEvents());
		Locality locality = findLocality(dto.localityName(), event.getLocalities());
		int freeTickets = locality.getFreeTickets();
		if (freeTickets < dto.quantity()) {
			throw new ConflictException("La localidad no tiene suficientes entradas");
		}
		if (found != null) {
			found.setQuantity(dto.quantity());
			cart.setItem(index, found);
		} else {
			CartDetail detail = CartDetail.builder()
					.calendarId(dto.calendarId())
					.eventName(dto.eventName())
					.localityName(dto.localityName())
					.quantity(dto.quantity()).build();
			cart.addItem(detail);
		}
		cartRepository.save(cart);
		return new ResponseDTO<>("Item guardado", true);
	}

	@Override
	public ResponseDTO<Boolean> validateExistsItem(ExistsCartItemDTO dto,
			@NotBlank
			@ValidObjectId
			@Valid
			String userId) throws Exception {

		Calendar calendar = calendarRepository.findById(dto.calendarId())
				.orElseThrow(() -> new DocumentNotFoundException("El calendario no fue encontrado"));
		Event event = findEvent(dto.eventName(), calendar.getEvents());
		findLocality(dto.localityName(), event.getLocalities());

		Cart cart = cartRepository.findByIdAndUserId(dto.cartId(), userId)
				.orElseThrow(() -> new DocumentNotFoundException("El carrito no fue encontrado"));
		List<CartDetail> items = cart.getItems();
		for (CartDetail item : items) {
			if (item.getCalendarId().equals(dto.calendarId()) && item.getEventName().equals(dto.eventName())
					&& item.getLocalityName().equals(dto.localityName())) {
				return new ResponseDTO<Boolean>("El item existe en el carrito", true);

			}
		}
		return new ResponseDTO<Boolean>("El item no existe en el carrito", false);
	}

	private Locality findLocality(String name, List<Locality> localities) throws DocumentNotFoundException {
		for (Locality locality : localities) {
			if(locality.getName().equals(name)) {
				return locality;
			}
		}
		throw new DocumentNotFoundException("La localidad no fue encontrada");
	}

	private Event findEvent(String name,List<Event> events) throws DocumentNotFoundException {
		for (Event e : events) {
			if (e.getName().equals(name))
				return e;
		}
		throw new DocumentNotFoundException("Evento no encontrado en el calendario");
	}

	@Override
	public ResponseDTO<Boolean> deleteItemFromCart(RemoveItemCartDTO dto, String userId) throws Exception {
		Cart cart = cartRepository.findByIdAndUserId(dto.cartId(), userId).orElseThrow(() -> new DocumentNotFoundException("El carrito no fue encontrado"));
		int index = -1;
		List<CartDetail> items = cart.getItems();
		for (int i = 0; i < items.size(); i++) {
			CartDetail detail = items.get(i);
			if(detail.getCalendarId().equals(dto.calendarId()) &&
					detail.getEventName().equals(dto.eventName()) &&
					detail.getLocalityName().equals(dto.localityName())) {
				index = i;
				break;
			}
		}
		Calendar calendar = calendarRepository.findById(dto.calendarId())
				.orElseThrow(() -> new DocumentNotFoundException("El calendario no fue encontrado"));
		Event event = findEvent(dto.eventName(), calendar.getEvents());
		findLocality(dto.localityName(), event.getLocalities());
		cart.removeItemIndex(index);
		cartRepository.save(cart);
		return new ResponseDTO<>("Item eliminado", true);
	}

}
