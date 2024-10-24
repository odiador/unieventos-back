package co.edu.uniquindio.unieventos;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.uniquindio.unieventos.controllers.impl.EventControllerImpl;
import co.edu.uniquindio.unieventos.dto.event.CreateEventDTO;
import co.edu.uniquindio.unieventos.dto.event.EventDTO;
import co.edu.uniquindio.unieventos.dto.event.FindEventDTO;
import co.edu.uniquindio.unieventos.model.enums.EventStatus;
import co.edu.uniquindio.unieventos.model.enums.EventType;
import co.edu.uniquindio.unieventos.services.EventService;

//@WebMvcTest(EventControllerImpl.class)
public class EventControllerTest {
//
//	@Autowired
//	private MockMvc mockMvc;
//
//	@MockBean
//	private EventService eventService;
//
//	@Test
//	public void testCreateEvent() throws Exception {
//		CreateEventDTO newEvent = new CreateEventDTO("calendar123", "New Event", null, null, "Armenia",
//				"Description de prueba", "Calle 1234", "2024-10-20T18:00:00", "2024-10-20T21:00:00", new ArrayList<>(),
//				new ArrayList<>(), "ACTIVE", "CONCERT");
//
//		EventDTO event = new EventDTO("New Event", null, null, "City", "Descripcion", "Calle 123",
//				"2024-10-20T18:00:00", "2024-10-20T21:00:00", new ArrayList<>(), new ArrayList<>(), EventStatus.ACTIVE,
//				EventType.CONCERT);
//		when(eventService.createEvent(any())).thenReturn(event);
//
//		mockMvc.perform(post("/api/events").contentType(MediaType.APPLICATION_JSON).content(asJsonString(newEvent)))
//				.andExpect(status().isCreated()).andExpect(jsonPath("$.name").value("New Event"));
//	}
//
//	@Test
//	public void testGetEventById() throws Exception {
//
//		EventDTO event = new EventDTO("New Event", null, null, "City", "Descripcion", "Calle 123",
//				"2024-10-20T18:00:00", "2024-10-20T21:00:00", new ArrayList<>(), new ArrayList<>(), EventStatus.ACTIVE,
//				EventType.CONCERT);
//
//		// Simular respuesta del servicio.
//		when(eventService.findEvent(new FindEventDTO(anyString(), anyString()))).thenReturn(event);
//
//		// Realizar petición GET.
//		mockMvc.perform(get("/api/events/find")).andExpect(status().isOk())
//				.andExpect(jsonPath("$.name").value(event.name()));
//	}
//
//	private static String asJsonString(final Object obj) {
//		try {
//			return new ObjectMapper().writeValueAsString(obj);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}

}
