package co.edu.uniquindio.unieventos.services;

import java.util.List;

import co.edu.uniquindio.unieventos.dto.CouponDTO;
import co.edu.uniquindio.unieventos.model.Coupon;

public interface CouponService {

	public Coupon saveCoupon(CouponDTO couponDTO) throws Exception;

	public List<Coupon> getActiveCoupons() throws Exception;
	
	public Coupon getCouponByCode(String code) throws Exception;
	
	public boolean isCouponActive(String code) throws Exception;
}
