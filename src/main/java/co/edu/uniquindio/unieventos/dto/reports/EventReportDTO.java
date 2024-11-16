package co.edu.uniquindio.unieventos.dto.reports;

import java.util.List;

public record EventReportDTO(byte[] byteArray, String filename, String filetype, double sellPercentage,
			List<LocalityDataDTO> soldpercentageByLocality, double earnedTotal, int capabilityTotal, int ticketsSoldTotal) {}
