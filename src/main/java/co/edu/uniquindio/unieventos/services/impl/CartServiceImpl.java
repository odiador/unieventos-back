package co.edu.uniquindio.unieventos.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniquindio.unieventos.config.Mappers;
import co.edu.uniquindio.unieventos.dto.carts.AddItemCartDTO;
import co.edu.uniquindio.unieventos.dto.carts.CartDTO;
import co.edu.uniquindio.unieventos.dto.carts.RemoveItemCartDTO;
import co.edu.uniquindio.unieventos.exceptions.ConflictException;
import co.edu.uniquindio.unieventos.exceptions.DocumentNotFoundException;
import co.edu.uniquindio.unieventos.exceptions.MaxCartsCreatedException;
import co.edu.uniquindio.unieventos.model.documents.Calendar;
import co.edu.uniquindio.unieventos.model.documents.Cart;
import co.edu.uniquindio.unieventos.model.vo.CartDetail;
import co.edu.uniquindio.unieventos.model.vo.Event;
import co.edu.uniquindio.unieventos.model.vo.Locality;
import co.edu.uniquindio.unieventos.repositories.CalendarRepository;
import co.edu.uniquindio.unieventos.repositories.CartRepository;
import co.edu.uniquindio.unieventos.services.CartService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private CalendarRepository calendarRepository;
	
	@Autowired
	private final Mappers mappers;

	@Override
	public CartDTO createCart(String userId) throws Exception {
		if (cartRepository.findByUserId(userId).size() >= 3)
			throw new MaxCartsCreatedException("La cantidad máxima de carritos es de 3");
		Cart cart = Cart.builder()
				.date(LocalDateTime.now())
				.userId(userId)
				.items(new ArrayList<CartDetail>())
				.build();
		return mappers.getCartToDTOMapper().apply(cartRepository.save(cart));
	}

	@Override
	public Cart getCartById(String id, String idCart) throws Exception {
		return cartRepository.findByIdAndUserId(idCart, id).orElseThrow(() -> new DocumentNotFoundException("El carrito no pudo ser obtenido"));
	}

	@Override
	public List<CartDTO> getCartsByUserId(String userId) {
		return cartRepository.findByUserId(userId).stream().map(mappers.getCartToDTOMapper()).collect(Collectors.toList());
	}

	@Override
	public CartDTO clearCart(String id, String userId) throws Exception {
		Cart cart = getCartById(userId, id);
		cart.setItems(List.of());
		return mappers.getCartToDTOMapper().apply(cartRepository.save(cart));
	}

	@Override
	public void deleteCartById(String userId, String id) throws Exception {
		if (cartRepository.findByIdAndUserId(id, userId).isEmpty())
			throw new DocumentNotFoundException("El carrito no fue encontrado");
		cartRepository.deleteById(id);
	}

	@Override
	public CartDTO saveItemToCart(AddItemCartDTO dto, String id) throws Exception {
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
			cart.setItem(index, found);
		} else {
			CartDetail detail = CartDetail.builder()
					.calendarId(dto.calendarId())
					.eventName(dto.eventName())
					.localityName(dto.localityName())
					.quantity(dto.quantity()).build();
			cart.addItem(detail);
		}
		return mappers.getCartToDTOMapper().apply(cartRepository.save(cart));
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
	public CartDTO deleteItemFromCart(RemoveItemCartDTO dto, String userId) throws Exception {
		Cart cart = cartRepository.findByIdAndUserId(dto.cartId(), dto.cartId()).orElseThrow(() -> new DocumentNotFoundException("El carrito no fue encontrado"));
		int index = -1;
		List<CartDetail> items = cart.getItems();
		for (int i = 0; i < items.size(); i++) {
			CartDetail detail = items.get(i);
			if(detail.getCalendarId().equals(dto.calendarId())&&
					detail.getEventName().equals(dto.eventName())&&
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
		return mappers.getCartToDTOMapper().apply(cartRepository.save(cart));
	}

}
