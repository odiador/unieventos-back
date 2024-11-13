package co.edu.uniquindio.unieventos.services;

import java.util.List;
import java.util.Map;

import com.mercadopago.resources.preference.Preference;

import co.edu.uniquindio.unieventos.dto.orders.CreateOrderDTO;
import co.edu.uniquindio.unieventos.dto.orders.FindOrderDTO;
import co.edu.uniquindio.unieventos.dto.orders.MercadoPagoURLDTO;
import co.edu.uniquindio.unieventos.dto.orders.OrderDTO;
import co.edu.uniquindio.unieventos.dto.orders.PurchaseDTO;
import co.edu.uniquindio.unieventos.exceptions.DocumentNotFoundException;
import co.edu.uniquindio.unieventos.exceptions.PaymentException;
import co.edu.uniquindio.unieventos.model.documents.Order;

public interface OrderService {

	MercadoPagoURLDTO realizarPago(String idOrden, String userId, String origin) throws Exception;

	void receiveMercadoPagoNotification(Map<String, Object> request) throws PaymentException;

	Order getOrder(String idOrden) throws Exception;

	FindOrderDTO getOrderDTO(String idOrden, String mail) throws Exception;

	List<PurchaseDTO> getPurchaseHistory(String mail) throws Exception;

	OrderDTO createOrder(CreateOrderDTO dto) throws Exception;

	List<FindOrderDTO> getOrdersDTO(String mail) throws DocumentNotFoundException;

}
