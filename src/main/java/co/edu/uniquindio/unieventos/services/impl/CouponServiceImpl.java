package co.edu.uniquindio.unieventos.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import co.edu.uniquindio.unieventos.dto.coupons.CouponDTO;
import co.edu.uniquindio.unieventos.dto.coupons.CouponInfoDTO;
import co.edu.uniquindio.unieventos.dto.coupons.GetCouponsDTO;
import co.edu.uniquindio.unieventos.exceptions.DocumentNotFoundException;
import co.edu.uniquindio.unieventos.model.documents.Coupon;
import co.edu.uniquindio.unieventos.model.enums.CouponStatus;
import co.edu.uniquindio.unieventos.model.enums.CouponType;
import co.edu.uniquindio.unieventos.repositories.CouponRepository;
import co.edu.uniquindio.unieventos.services.CouponService;
import co.edu.uniquindio.unieventos.services.RandomCodesService;
import jakarta.validation.Valid;

@Service
public class CouponServiceImpl implements CouponService {

	@Autowired
	private CouponRepository couponRepository;

	@Autowired
	private RandomCodesService randomCodesService;

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
		Optional<Coupon> optCoupon = couponRepository.findByCode(code);
		System.out.println(optCoupon.orElse(null));
		return coupon.getCode();
	}

	@Override
	public Coupon getCouponByCode(String code) throws Exception {
		return couponRepository.findByCode(code)
				.orElseThrow(() -> new DocumentNotFoundException("El cupon con ese codigo no existe"));
	}

	@Override
	public Coupon getCouponById(String id) throws Exception {
		return couponRepository.findById(id)
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
			return new CouponInfoDTO(
					c.getDiscount(),
					c.getExpiryDate(),
					c.getCode(),
					c.getStatus(),
					c.getType(),
					c.getName()
					);
		};
	}

}
