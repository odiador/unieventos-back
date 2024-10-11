package co.edu.uniquindio.unieventos.model.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import co.edu.uniquindio.unieventos.model.vo.CartDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

	@Id
	private String id;
	private LocalDateTime date;
	private List<CartDetail> items;
	private String userId;

	public boolean isEmpty() {
		return items == null || items.isEmpty();
	}

	public void addItem(CartDetail cartDetail) {
		items.add(cartDetail);
	}

	public void setItem(int index, CartDetail detail) {
		items.set(index, detail);
	}

	public void removeItemIndex(int index) {
		items.remove(index);
	}

}
