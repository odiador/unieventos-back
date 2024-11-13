package co.edu.uniquindio.unieventos.config;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import co.edu.uniquindio.unieventos.dto.calendar.CalendarDTO;
import co.edu.uniquindio.unieventos.dto.carts.AddItemCartDTO;
import co.edu.uniquindio.unieventos.dto.client.UserDataDTO;
import co.edu.uniquindio.unieventos.dto.event.EventDTO;
import co.edu.uniquindio.unieventos.dto.event.EventTagDTO;
import co.edu.uniquindio.unieventos.dto.event.EventWCalIdDTO;
import co.edu.uniquindio.unieventos.dto.event.ReturnLocalityDTO;
import co.edu.uniquindio.unieventos.dto.orders.OrderDTO;
import co.edu.uniquindio.unieventos.dto.orders.OrderDetailDTO;
import co.edu.uniquindio.unieventos.dto.orders.PurchaseDTO;
import co.edu.uniquindio.unieventos.model.documents.Calendar;
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
					event.getId(),
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
									locality.getId(),
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
	private final Function<SimpleEntry<String, Event>, EventWCalIdDTO> eventCalIdToDTOMapper = (entry) -> {
		Event event = entry.getValue();
		return new EventWCalIdDTO(
				entry.getKey(),
				event.getId(),
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
								locality.getId(),
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
		return new OrderDTO(e.getId(), e.getClientId(), e.getTimestamp().toString(), e.getPayment(), items, e.getStatus().name(), e.getTotal(), e.getCouponId());
	};
	
	private final Function<CartDetail, OrderDetail> cartToOrderMapper = c -> {
		return OrderDetail.builder()
				.calendarId(c.getCalendarId())
				.eventId(c.getEventId())
				.quantity(c.getQuantity())
				.localityId(c.getLocalityId())
				.build();
	};
	
	private final Function<AddItemCartDTO, CartDetail> cartDetailMapper = (detail)-> {
		return CartDetail.builder()
				.eventId(detail.eventId())
				.localityId(detail.localityId())
				.quantity(detail.quantity())
				.build();
	};

	private final Function<CartDetail, OrderDetail> mapper = e -> {
		return OrderDetail.builder()
				.calendarId(e.getCalendarId())
				.eventId(e.getEventId())
				.localityId(e.getLocalityId())
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
		return new OrderDetailDTO(e.getCalendarId(), e.getEventId(), e.getLocalityId(), e.getPrice(),
				e.getQuantity());
	};

}
