package co.edu.uniquindio.unieventos.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.unieventos.config.AuthUtils;
import co.edu.uniquindio.unieventos.controllers.CalendarController;
import co.edu.uniquindio.unieventos.dto.calendar.CreateCalendarDTO;
import co.edu.uniquindio.unieventos.dto.calendar.EditCalendarDTO;
import co.edu.uniquindio.unieventos.dto.calendar.SearchPageDTO;
import co.edu.uniquindio.unieventos.misc.validation.ValidObjectId;
import co.edu.uniquindio.unieventos.services.CalendarService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
	@SecurityRequirement(name = "bearerAuth")
	@PostMapping("/create")
	public ResponseEntity<?> createCalendar(@Valid CreateCalendarDTO dto, HttpServletRequest request) throws Exception {
		authUtils.verifyRoleAdmin(request);
		return ResponseEntity.ok(calendarService.createCalendar(dto));
	}

	@Override
	@SecurityRequirement(name = "bearerAuth")
	@PostMapping("/edit")
	public ResponseEntity<?> editCalendar(@Valid EditCalendarDTO dto, HttpServletRequest request) throws Exception {
		authUtils.verifyRoleAdmin(request);
		return ResponseEntity.ok(calendarService.editCalendar(dto));
	}
	
	@Override
	@SecurityRequirement(name = "bearerAuth")
	@PostMapping("/delete")
	public ResponseEntity<?> deleteCalendar(@ValidObjectId @NotNull @RequestParam("id") String id, HttpServletRequest request) throws Exception {
		authUtils.verifyRoleAdmin(request);
		calendarService.deleteCalendar(id);
		return ResponseEntity.ok("R");
	}
	
	@Override
	@PostMapping("/findByPage")
	public ResponseEntity<?> searchCalendars(@Valid @RequestBody SearchPageDTO dto) throws Exception {
		return ResponseEntity.ok(calendarService.searchDalendars(dto));
	}

}
