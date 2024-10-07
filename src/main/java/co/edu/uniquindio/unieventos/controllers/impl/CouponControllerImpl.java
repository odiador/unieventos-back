package co.edu.uniquindio.unieventos.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.unieventos.controllers.CouponController;
import co.edu.uniquindio.unieventos.dto.coupons.CouponCodeDTO;
import co.edu.uniquindio.unieventos.dto.coupons.CouponDTO;
import co.edu.uniquindio.unieventos.services.CouponService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/coupons")
@CrossOrigin
public class CouponControllerImpl implements CouponController {

	@Autowired
	private CouponService service;

	@Override
	@PostMapping("/create")
	public ResponseEntity<?> createCoupon(@Valid CouponDTO dto) throws Exception {
		String code = service.saveCoupon(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(new CouponCodeDTO(code));
	}

	@Override
	@GetMapping("/findId/{id}")
	public ResponseEntity<?> findById(@PathVariable("id") String id) throws Exception {
		return ResponseEntity.status(HttpStatus.OK).body(service.getCouponById(id));
	}

	@Override
	@GetMapping("/find/{code}")
	public ResponseEntity<?> findByCode(@PathVariable("code") String code) throws Exception {
		return ResponseEntity.status(HttpStatus.OK).body(service.getCouponByCode(code));
	}

}
