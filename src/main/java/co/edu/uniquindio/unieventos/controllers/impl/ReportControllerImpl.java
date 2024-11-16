package co.edu.uniquindio.unieventos.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.unieventos.config.AuthUtils;
import co.edu.uniquindio.unieventos.controllers.ReportController;
import co.edu.uniquindio.unieventos.dto.misc.ResponseDTO;
import co.edu.uniquindio.unieventos.dto.reports.EventReportDTO;
import co.edu.uniquindio.unieventos.exceptions.UnauthorizedAccessException;
import co.edu.uniquindio.unieventos.services.ReportService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/api/reports")
public class ReportControllerImpl implements ReportController {

	@Autowired
	private AuthUtils authUtils;
	@Autowired
	private ReportService reportService;

	@Override
	@GetMapping("/generate")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<ResponseDTO<EventReportDTO>> generateReport(@RequestParam("eventId") String eventId,
			@RequestParam("calendarId") String calendarId, HttpServletRequest request) throws Exception {
		authUtils.verifyRoleAdmin(request);
		String mail = authUtils.getMail(request);
		if (mail == null)
			throw new UnauthorizedAccessException("No tienes permiso para realizar esta acción");
		EventReportDTO eventReport = reportService.generateEventReport(mail, eventId, calendarId);
		return ResponseEntity.status(201).body(new ResponseDTO<>("Reporte generado", eventReport));
	}

}
