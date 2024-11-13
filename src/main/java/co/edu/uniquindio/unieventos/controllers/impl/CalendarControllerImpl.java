package co.edu.uniquindio.unieventos.controllers.impl;

import java.util.List;

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
import co.edu.uniquindio.unieventos.dto.calendar.CalendarDTO;
import co.edu.uniquindio.unieventos.dto.calendar.CreateCalendarDTO;
import co.edu.uniquindio.unieventos.dto.calendar.EditCalendarDTO;
import co.edu.uniquindio.unieventos.dto.calendar.OnlyCalendarDTO;
import co.edu.uniquindio.unieventos.dto.calendar.SearchPageDTO;
import co.edu.uniquindio.unieventos.dto.misc.ResponseDTO;
import co.edu.uniquindio.unieventos.misc.validation.ValidObjectId;
import co.edu.uniquindio.unieventos.services.CalendarService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/calendars")
@CrossOrigin
public class CalendarControllerImpl implements CalendarController {

	@Autowired
	private CalendarService calendarService;
	@Autowired
	private AuthUtils authUtils;

	@Override
	@SecurityRequirement(name = "bearerAuth")
	@PostMapping("/create")
	public ResponseEntity<ResponseDTO<CalendarDTO>> createCalendar(@Valid CreateCalendarDTO dto, HttpServletRequest request) throws Exception {
		authUtils.verifyRoleAdmin(request);
		return ResponseEntity.ok(new ResponseDTO<>("Calendario creado con éxito", calendarService.createCalendar(dto)));
	}

	@Override
	@SecurityRequirement(name = "bearerAuth")
	@PostMapping("/edit")
	public ResponseEntity<ResponseDTO<CalendarDTO>> editCalendar(@Valid EditCalendarDTO dto, HttpServletRequest request) throws Exception {
		authUtils.verifyRoleAdmin(request);
		return ResponseEntity.ok(new ResponseDTO<>("Calendario editado con éxito", calendarService.editCalendar(dto)));
	}

	@Override
	@SecurityRequirement(name = "bearerAuth")
	@PostMapping("/delete")
	public ResponseEntity<ResponseDTO<Void>> deleteCalendar(@ValidObjectId @NotNull @RequestParam("id") String id, HttpServletRequest request) throws Exception {
		authUtils.verifyRoleAdmin(request);
		calendarService.deleteCalendar(id);
		return ResponseEntity.ok(new ResponseDTO<>("Calendario eliminado con éxito", null));
	}

	@Override
	@PostMapping("/findByPage")
	public ResponseEntity<ResponseDTO<List<OnlyCalendarDTO>>> searchCalendars(@Valid @RequestBody SearchPageDTO dto) throws Exception {
		return ResponseEntity.ok(new ResponseDTO<>("Calendarios encontrados:", calendarService.searchDalendars(dto)));
	}

	@Override
	@PostMapping("/findById")
	public ResponseEntity<ResponseDTO<OnlyCalendarDTO>> findCalendar(@Valid @RequestBody String id) throws Exception {
		return ResponseEntity.ok(new ResponseDTO<>("El calendario fue encontrado", calendarService.findOnlyCalendar(id)));
	}

}
