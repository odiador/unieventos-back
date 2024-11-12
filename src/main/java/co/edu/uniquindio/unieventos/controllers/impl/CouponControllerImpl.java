package co.edu.uniquindio.unieventos.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.unieventos.config.AuthUtils;
import co.edu.uniquindio.unieventos.controllers.CouponController;
import co.edu.uniquindio.unieventos.dto.coupons.CouponCodeDTO;
import co.edu.uniquindio.unieventos.dto.coupons.CouponDTO;
import co.edu.uniquindio.unieventos.dto.misc.ResponseDTO;
import co.edu.uniquindio.unieventos.model.documents.Coupon;
import co.edu.uniquindio.unieventos.services.CouponService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/coupons")
@CrossOrigin
public class CouponControllerImpl implements CouponController {

	@Autowired
	private CouponService service;
	@Autowired
	private AuthUtils authUtils;

	@Override
	@PostMapping("/create")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<ResponseDTO<CouponCodeDTO>> createCoupon(@RequestBody @Valid CouponDTO dto, HttpServletRequest request) throws Exception {
		authUtils.verifyRoleAdmin(request);
		String code = service.saveCoupon(dto);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ResponseDTO<CouponCodeDTO>("Cupón creado con éxito", new CouponCodeDTO(code)));
	}

	@Override
	@GetMapping("/findId")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<ResponseDTO<Coupon>> findById(@RequestParam("id") @NotNull String id, HttpServletRequest request) throws Exception {
		authUtils.verifyMinRoleClient(request);
		ResponseDTO<Coupon> dto = new ResponseDTO<>("Cupón encontrado", service.getCouponById(id));
		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}

	@Override
	@GetMapping("/find")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<ResponseDTO<Coupon>> findByCode(@RequestParam("code") @NotNull String code, HttpServletRequest request)
			throws Exception {
		authUtils.verifyMinRoleClient(request);
		ResponseDTO<Coupon> dto = new ResponseDTO<>("Cupón encontrado", service.getCouponByCode(code));
		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}

}
