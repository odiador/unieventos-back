package co.edu.uniquindio.unieventos.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import co.edu.uniquindio.unieventos.dto.coupons.AppliedCouponDTO;
import co.edu.uniquindio.unieventos.dto.coupons.CouponDTO;
import co.edu.uniquindio.unieventos.dto.coupons.CouponInfoDTO;
import co.edu.uniquindio.unieventos.dto.coupons.GetCouponsDTO;
import co.edu.uniquindio.unieventos.exceptions.DocumentNotFoundException;
import co.edu.uniquindio.unieventos.model.documents.Calendar;
import co.edu.uniquindio.unieventos.model.documents.Coupon;
import co.edu.uniquindio.unieventos.model.enums.CouponStatus;
import co.edu.uniquindio.unieventos.model.enums.CouponType;
import co.edu.uniquindio.unieventos.model.vo.Event;
import co.edu.uniquindio.unieventos.repositories.CalendarRepository;
import co.edu.uniquindio.unieventos.repositories.CouponRepository;
import co.edu.uniquindio.unieventos.services.CouponService;
import co.edu.uniquindio.unieventos.services.RandomCodesService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Service
public class CouponServiceImpl implements CouponService {

	@Autowired
	private CouponRepository couponRepository;
	@Autowired
	private RandomCodesService randomCodesService;
	@Autowired
	private CalendarRepository calendarRepository;

	@Override
	public String saveCoupon(@Valid CouponDTO couponDTO) throws Exception {
		String code = couponDTO.code() == null ? randomCodesService.getRandomCouponCode() : couponDTO.code();
		Coupon coupon = Coupon.builder()
				.discount(couponDTO.discount())
				.expiryDate(LocalDateTime.parse(couponDTO.expiryDate()))
				.code(code)
				.status(CouponStatus.valueOf(couponDTO.status()))
				.type(CouponType.valueOf(couponDTO.type()))
				.name(couponDTO.name())
				.build();
		couponRepository.save(coupon);
		return coupon.getCode();
	}

	@Override
	public Coupon getCouponByCode(String code) throws Exception {
		return couponRepository.findByCode(code)
				.orElseThrow(() -> new DocumentNotFoundException("El cupon con ese codigo no existe"));
	}

	@Override
	public Coupon getCouponById(String id) throws Exception {
		return couponRepository.findByIdAndStatus(id, CouponStatus.AVAILABLE)
				.orElseThrow(() -> new DocumentNotFoundException("El cupon con ese id no existe"));
	}
//
//	@Override
//	public boolean isCouponActive(String code) throws Exception {
//		return couponRepository.existsByCodeAndStatus(code, CouponStatus.AVAILABLE);
//	}

	@Override
	public List<CouponInfoDTO> getCouponsAdmin(@Valid GetCouponsDTO dto) throws Exception {
		CouponStatus status = CouponStatus.valueOf(dto.status());
		return couponRepository
				.findByStatus(status, PageRequest.of(dto.page(), dto.size()))
				.stream()
				.map(mapper())
				.collect(Collectors.toList());
	}

	private Function<Coupon, CouponInfoDTO> mapper() {
		return c -> {
			String calendarName = null;
			String eventName = null;
			if (c.isForSpecialEvent()) {
				Calendar calendar = calendarRepository.findById(c.getCalendarId()).orElse(null);
				if (calendar != null) {
					calendarName = calendar.getName();
					Event event = findEvent(calendar.getEvents(), c.getEventId());
					if (event != null)
						eventName = event.getName();
				}
			}
			return new CouponInfoDTO(
					c.getDiscount(),
					c.getExpiryDate().toString(),
					c.getCode(),
					c.getStatus().toString(),
					c.getType().toString(),
					c.getName(),
					c.getCalendarId(),
					calendarName,
					c.getEventId(),
					eventName
					);
		};
	}

	private Event findEvent(List<Event> events, String eventId) {
		return events.stream().filter(e -> e.getId().equals(eventId)).findAny().orElse(null);
	}

	@Override
	public AppliedCouponDTO applyCouponByCode(@NotNull String code) throws Exception {
		Coupon coupon = couponRepository.findByCode(code).orElseThrow(() -> new DocumentNotFoundException("Cupón no encontrado"));
		if (coupon.getStatus() == CouponStatus.DELETED)
			throw new DocumentNotFoundException("Cupón no encontrado");
		if (coupon.getStatus() == CouponStatus.UNAVAILABLE)
			throw new DocumentNotFoundException("Cupón no disponible");
		if(!coupon.getExpiryDate().isAfter(LocalDateTime.now()))
			throw new DocumentNotFoundException("Tu cupón ya está vencido");
		return new AppliedCouponDTO(coupon.getId(), code, coupon.getDiscount(), coupon.isForSpecialEvent(),
				coupon.getCalendarId(), coupon.getEventId(), coupon.getType() == CouponType.UNIQUE);
	}

	@Override
	public void changeCouponStatus(String code, CouponStatus cs) throws DocumentNotFoundException {
		Coupon coupon = couponRepository.findByCode(code).orElseThrow(() -> new DocumentNotFoundException("Cupón no encontrado"));
		coupon.setStatus(cs);
		couponRepository.save(coupon);
		
	}

}
