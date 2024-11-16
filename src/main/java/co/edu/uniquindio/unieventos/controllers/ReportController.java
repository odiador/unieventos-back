package co.edu.uniquindio.unieventos.controllers;

import org.springframework.http.ResponseEntity;

import co.edu.uniquindio.unieventos.dto.misc.ResponseDTO;
import co.edu.uniquindio.unieventos.dto.reports.EventReportDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface ReportController {

	ResponseEntity<ResponseDTO<EventReportDTO>> generateReport(String eventId, String calendarId, HttpServletRequest request) throws Exception;

}
