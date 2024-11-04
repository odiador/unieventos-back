package co.edu.uniquindio.unieventos.config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import co.edu.uniquindio.unieventos.dto.calendar.CalendarDTO;
import co.edu.uniquindio.unieventos.dto.carts.AddItemCartDTO;
import co.edu.uniquindio.unieventos.dto.carts.CartDTO;
import co.edu.uniquindio.unieventos.dto.carts.CartDetailDTO;
import co.edu.uniquindio.unieventos.dto.client.UserDataDTO;
import co.edu.uniquindio.unieventos.dto.event.EventDTO;
import co.edu.uniquindio.unieventos.dto.event.EventTagDTO;
import co.edu.uniquindio.unieventos.dto.event.ReturnLocalityDTO;
import co.edu.uniquindio.unieventos.dto.orders.OrderDTO;
import co.edu.uniquindio.unieventos.dto.orders.OrderDetailDTO;
import co.edu.uniquindio.unieventos.dto.orders.PurchaseDTO;
import co.edu.uniquindio.unieventos.model.documents.Calendar;
import co.edu.uniquindio.unieventos.model.documents.Cart;
import co.edu.uniquindio.unieventos.model.documents.Order;
import co.edu.uniquindio.unieventos.model.vo.CartDetail;
import co.edu.uniquindio.unieventos.model.vo.Event;
import co.edu.uniquindio.unieventos.model.vo.EventTag;
import co.edu.uniquindio.unieventos.model.vo.OrderDetail;
import co.edu.uniquindio.unieventos.model.vo.UserData;
import lombok.Getter;

@Component
@Getter
public class Mappers {

	private final Function<EventTag, EventTagDTO> tagToDtoMapper = 
			tag -> new EventTagDTO(
					tag.getName(), 
					tag.getColor(),
					tag.getTextColor());
			
			private final Function<EventTagDTO, EventTag> dtoToTagMapper = 
					tag -> new EventTag(
							tag.getName(), 
							tag.getColor(),
							tag.getTextColor());

	/**
	 * Maps {@link Event} to {@link EventDTO}
	 */
	private final Function<Event, EventDTO> eventMapper = event -> {
		return new EventDTO(
					event.getName(),
					event.getEventImage(),
					event.getLocalityImage(),
					event.getCity(),
					event.getDescription(),
					event.getAddress(),
					event.getStartTime().toString(),
					event.getEndTime().toString(),
					event.getLocalities() == null ? null
						: event.getLocalities()
							.stream()
							.map(locality -> new ReturnLocalityDTO(
									locality.getName(),
									locality.getPrice(),
									locality.getTicketsSold(),
									locality.getMaxCapability()))
							.collect(Collectors.toList()),
				event.getTags() == null ? null
						: event.getTags().stream()
							.map(tagToDtoMapper)
							.collect(Collectors.toList()),
					event.getStatus(),
					event.getType());
	};

	/**
	 * Maps {@link Calendar} to {@link CalendarDTO}
	 */
	private final Function<Calendar, CalendarDTO> calendarMapper = c -> new CalendarDTO(
			c.getId(),
			c.getName(),
			c.getDescription(),
			c.getEvents() == null ? null
					: c.getEvents().stream()
			.map(eventMapper)
			.collect(Collectors.toList()),
			c.getImage(),
			c.getBannerImage());

	private final Function<Order, OrderDTO> orderMapper = e -> {
		List<OrderDetailDTO> items = new ArrayList<>();
		if (e.getItems() == null)
			items = e.getItems().stream()
			.map(this.orderDetailToDto)
			.collect(Collectors.toList());
		return new OrderDTO(e.getId(), e.getClientId(), e.getTimestamp(), e.getPayment(), items, e.getStatus(), e.getTotal(), e.getCouponId());
	};
	
	private final Function<CartDetail, OrderDetail> cartToOrderMapper = c -> {
		return OrderDetail.builder()
				.calendarId(c.getCalendarId())
				.eventName(c.getEventName())
				.quantity(c.getQuantity())
				.localityName(c.getLocalityName())
				.build();
	};
	
	private final Function<AddItemCartDTO, CartDetail> cartDetailMapper = (detail)-> {
		return CartDetail.builder()
				.eventName(detail.eventName())
				.localityName(detail.localityName())
				.quantity(detail.quantity())
				.build();
	};
	private final Function<CartDetail, CartDetailDTO> cartDetailToDTOMapper = (detail) -> {
		return new CartDetailDTO(
				detail.getQuantity(), 
				detail.getCalendarId(), 
				detail.getEventName(),
				detail.getLocalityName());
	};

	private final Function<Cart, CartDTO> cartToDTOMapper = cart -> {
		List<CartDetailDTO> items = new ArrayList<>();
		if (cart.getItems() != null)
			items = cart.getItems().stream()
			.map(this.cartDetailToDTOMapper)
			.collect(Collectors.toList()); 
		return new CartDTO(cart.getId(), cart.getDate(), items, cart.getUserId());
	};

	private final Function<CartDetail, OrderDetail> mapper = e -> {
		return OrderDetail.builder()
				.calendarId(e.getCalendarId())
				.eventName(e.getEventName())
				.localityName(e.getLocalityName())
				.quantity(e.getQuantity())
				.build();
	};

	private final Function<Order, PurchaseDTO> orderToPurchaseMapper = e -> {
		List<OrderDetailDTO> items = new ArrayList<>();
		if (e.getItems() != null)
			items = e.getItems().stream()
			.map(this.orderDetailToDto)
			.collect(Collectors.toList());
		return new PurchaseDTO(
				e.getClientId().toString(),
				e.getTimestamp(),
				e.getPayment(),
				items,
				e.getTotal(),
				e.getCouponId());
	};
	
	private final Function<UserData, UserDataDTO> userDataToDto = e -> {
		return new UserDataDTO(
				e.getName(),
				e.getCedula(),
				e.getAdress(),
				e.getCity(),
				e.getPhone()
				);
	};

	private final Function<OrderDetail, OrderDetailDTO> orderDetailToDto = e -> {
		return new OrderDetailDTO(e.getCalendarId(), e.getEventName(), e.getLocalityName(), e.getPrice(),
				e.getQuantity());
	};

}
