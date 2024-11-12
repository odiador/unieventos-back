package co.edu.uniquindio.unieventos.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniquindio.unieventos.model.documents.Coupon;
import co.edu.uniquindio.unieventos.model.enums.CouponStatus;

@Repository
public interface CouponRepository extends MongoRepository<Coupon, String> {

	Optional<Coupon> findByCode(String code);

	Optional<Coupon> findByIdAndStatus(String id, CouponStatus status);

	/**
	 * Busca los que tengan un estado específico y no estén vencidos
	 * 
	 * @param status
	 * @param currentDate
	 * @return
	 */
	List<Coupon> findByStatusAndExpiryDateAfter(CouponStatus status, LocalDateTime currentDate);

	List<Coupon> findByStatus(CouponStatus status, Pageable pageable);

	boolean existsByCodeAndStatus(String code, CouponStatus status);
}
