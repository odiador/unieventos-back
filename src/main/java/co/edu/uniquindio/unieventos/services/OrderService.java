package co.edu.uniquindio.unieventos.services;

import java.util.Map;

import com.mercadopago.resources.preference.Preference;

import co.edu.uniquindio.unieventos.exceptions.PaymentException;
import co.edu.uniquindio.unieventos.model.documents.Order;

public interface OrderService {

	Preference realizarPago(String idOrden) throws Exception;

	void receiveMercadoPagoNotification(Map<String, Object> request) throws PaymentException;

	Order getOrder(String idOrden);
}
