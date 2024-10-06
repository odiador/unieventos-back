package co.edu.uniquindio.unieventos.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "eventtags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventTag {
	private String name, color, textColor;
}
