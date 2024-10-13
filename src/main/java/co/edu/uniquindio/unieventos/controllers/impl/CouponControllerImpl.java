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
import co.edu.uniquindio.unieventos.services.CouponService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/coupons")
@CrossOrigin
@RequiredArgsConstructor
public class CouponControllerImpl implements CouponController {

	@Autowired
	private CouponService service;
	private final AuthUtils authUtils;

	@Override
	@PostMapping("/create")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<?> createCoupon(@RequestBody CouponDTO dto, HttpServletRequest request) throws Exception {
		authUtils.verifyRoleAdmin(request);
		String code = service.saveCoupon(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(new CouponCodeDTO(code));
	}

	@Override
	@GetMapping("/findId")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<?> findById(@RequestParam("id") String id, HttpServletRequest request) throws Exception {
		authUtils.verifyMinRoleClient(request);
		return ResponseEntity.status(HttpStatus.OK).body(service.getCouponById(id));
	}

	@Override
	@GetMapping("/find")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<?> findByCode(@RequestParam("code") String code, HttpServletRequest request)
			throws Exception {
		authUtils.verifyMinRoleClient(request);
		return ResponseEntity.status(HttpStatus.OK).body(service.getCouponByCode(code));
	}

}
