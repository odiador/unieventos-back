package co.edu.uniquindio.unieventos.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.unieventos.config.AuthUtils;
import co.edu.uniquindio.unieventos.controllers.EventController;
import co.edu.uniquindio.unieventos.dto.event.CreateEventDTO;
import co.edu.uniquindio.unieventos.dto.event.EditEventDTO;
import co.edu.uniquindio.unieventos.dto.event.FindEventDTO;
import co.edu.uniquindio.unieventos.dto.event.SearchEventDTO;
import co.edu.uniquindio.unieventos.dto.misc.ResponseDTO;
import co.edu.uniquindio.unieventos.services.EventService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/events")
@CrossOrigin
@RequiredArgsConstructor
public class EventControllerImpl implements EventController {

	@Autowired
	private EventService eventService;

	@Autowired
	private AuthUtils authUtils;

	@Override
	@PostMapping("/create")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<?> createEvent(@Valid CreateEventDTO dto, HttpServletRequest request)
			throws Exception {
		authUtils.verifyRoleAdmin(request);
		return ResponseEntity.status(201).body(eventService.createEvent(dto));
	}

	@Override
	@PutMapping("/edit")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<?> editEvent(@RequestBody @Valid EditEventDTO dto, HttpServletRequest request)
			throws Exception {
		authUtils.verifyRoleAdmin(request);
		return ResponseEntity.ok(eventService.editEvent(dto));
	}

	@Override
	@PostMapping("/find")
	public ResponseEntity<?> findEvent(@RequestBody @NotNull FindEventDTO dto) throws Exception {
		return ResponseEntity.ok(
				new ResponseDTO<>(
						"El evento ha sido encontrado con éxito", 
						eventService.findEvent(dto)
				));
	}


	@Override
	@GetMapping("/list")
	public ResponseEntity<?> getEvents(@Valid @RequestBody SearchEventDTO dto) throws Exception {
		return ResponseEntity.ok(eventService.findEvents(dto));
	}
	@Override
	@DeleteMapping("/delete")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<?> deleteEvent(@RequestBody @Valid FindEventDTO dto, HttpServletRequest request) throws Exception {
		authUtils.verifyRoleAdmin(request);
		eventService.deleteEvent(dto);
		return ResponseEntity.ok().build();
	}

}
