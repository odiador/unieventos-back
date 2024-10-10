package co.edu.uniquindio.unieventos.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.unieventos.config.AuthUtils;
import co.edu.uniquindio.unieventos.controllers.CalendarController;
import co.edu.uniquindio.unieventos.dto.calendar.CreateCalendarDTO;
import co.edu.uniquindio.unieventos.services.CalendarService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/calendars")
@CrossOrigin
@RequiredArgsConstructor
public class CalendarControllerImpl implements CalendarController {
	
	@Autowired
	private CalendarService calendarService;
	private final AuthUtils authUtils;
	

	@Override
	@PostMapping("/create")
	public ResponseEntity<?> createCalendar(@Valid CreateCalendarDTO dto, HttpServletRequest request) throws Exception {
		authUtils.verifyRoleAdmin(request);
		return ResponseEntity.ok(calendarService.createCalendar(dto));
	}

}
