package co.edu.uniquindio.unieventos.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniquindio.unieventos.dto.CouponDTO;
import co.edu.uniquindio.unieventos.model.Coupon;
import co.edu.uniquindio.unieventos.model.CouponStatus;
import co.edu.uniquindio.unieventos.repositories.CouponRepository;
import co.edu.uniquindio.unieventos.services.CouponService;

@Service
public class CouponServiceImpl implements CouponService {

	@Autowired
	private CouponRepository couponRepository;

	@Override
	public Coupon saveCoupon(CouponDTO couponDTO) throws Exception {
		Coupon coupon = new Coupon();
		coupon.setDiscount(couponDTO.discount());
		coupon.setExpiryDate(couponDTO.expiryDate());
		coupon.setCode(couponDTO.code());
		coupon.setStatus(couponDTO.status());
		coupon.setType(couponDTO.type());
		coupon.setName(couponDTO.name());

		return couponRepository.save(coupon);
	}

	@Override
	public List<Coupon> getActiveCoupons() throws Exception {
		return couponRepository.findByStatusAndExpiryDateAfter(CouponStatus.AVAILABLE, LocalDateTime.now());
	}

	@Override
	public Coupon getCouponByCode(String code) throws Exception {
		return couponRepository.findByCode(code).orElseThrow(() -> new Exception("El cupon con ese codigo no existe"));
	}

	@Override
	public boolean isCouponActive(String code) throws Exception {
		return couponRepository.existsByCodeAndStatus(code, CouponStatus.AVAILABLE);
	}
}
