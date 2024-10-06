package co.edu.uniquindio.unieventos.services;

import java.util.List;

import co.edu.uniquindio.unieventos.dto.coupons.CouponDTO;
import co.edu.uniquindio.unieventos.model.documents.Coupon;

public interface CouponService {

	public Coupon saveCoupon(CouponDTO couponDTO) throws Exception;

	public List<Coupon> getActiveCoupons() throws Exception;
	
	public Coupon getCouponByCode(String code) throws Exception;
	
	public boolean isCouponActive(String code) throws Exception;
}
