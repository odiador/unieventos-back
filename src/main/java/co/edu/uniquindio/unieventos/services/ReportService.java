package co.edu.uniquindio.unieventos.services;

import co.edu.uniquindio.unieventos.dto.reports.EventReportDTO;

public interface ReportService {

	EventReportDTO generateEventReport(String mail, String eventId, String calendarId) throws Exception;

}
