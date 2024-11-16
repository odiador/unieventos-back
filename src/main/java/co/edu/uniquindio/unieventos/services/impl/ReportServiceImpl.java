package co.edu.uniquindio.unieventos.services.impl;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import co.edu.uniquindio.unieventos.dto.reports.EventReportDTO;
import co.edu.uniquindio.unieventos.dto.reports.LocalityDataDTO;
import co.edu.uniquindio.unieventos.exceptions.DocumentNotFoundException;
import co.edu.uniquindio.unieventos.model.documents.Calendar;
import co.edu.uniquindio.unieventos.model.vo.Event;
import co.edu.uniquindio.unieventos.model.vo.Locality;
import co.edu.uniquindio.unieventos.repositories.CalendarRepository;
import co.edu.uniquindio.unieventos.services.EmailService;
import co.edu.uniquindio.unieventos.services.ReportService;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	private CalendarRepository calendarRepository;
	@Autowired
	private EmailService emailService;
	@Override
	public EventReportDTO generateEventReport(String mail, String eventId, String calendarId) throws Exception {
		Calendar calendar = calendarRepository.findById(calendarId).orElseThrow(()-> new DocumentNotFoundException("El calendario no fue encontrado"));
		List<Event> events = calendar.getEvents();
		Event event = getEvent(eventId, events);
		if (event == null)
			throw new DocumentNotFoundException("El evento no fue encontrado en el calendario");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter writer = new PdfWriter(baos);
		PdfDocument pdf = new PdfDocument(writer);
		Document document = new Document(pdf);

		document.add(new Paragraph("Event Report"));
		document.add(new Paragraph("Name: " + event.getName()));
		document.add(new Paragraph("Description: " + event.getDescription()));
		document.add(new Paragraph("Start time: " + event.getStartTime().toString()));
		document.add(new Paragraph("End time: " + event.getEndTime().toString()));
		document.add(new Paragraph("City: " + event.getCity()));

		double earnedTotal = 0;
		int ticketsSoldTotal = 0;
		int capabilityTotal = 0;
		double totalRetention = 0;

		document.add(new Paragraph("Localities"));
		Table table = new Table(new float[] { 4, 4, 4, 4 });

		table.addCell("Name");
		table.addCell("Price");
		table.addCell("Max. Capability");
		table.addCell("Tickets Sold");
		table.addCell("Retention");

		List<Locality> localities = event.getLocalities();
		for (Locality loc : localities) {
			int maxCapability = loc.getMaxCapability();
			int ticketsSold = loc.getTicketsSold();
			float price = loc.getPrice();
			int retention = loc.getRetention();
			table.addCell(new Cell().add(new Paragraph(loc.getName())));
			table.addCell(new Cell().add(new Paragraph(String.format("%.2f", price))));
			table.addCell(new Cell().add(new Paragraph(maxCapability + "")));
			table.addCell(new Cell().add(new Paragraph(ticketsSold + "")));
			table.addCell(new Cell().add(new Paragraph(retention + "")));

			capabilityTotal += maxCapability;
			ticketsSoldTotal += ticketsSold;
			earnedTotal += price * ticketsSold;
			totalRetention += retention;
		}
		document.add(table);
		double totalSoldPercentage = (ticketsSoldTotal / (double) capabilityTotal) * 100;

		document.add(new Paragraph("\nStatistics:"));
		document.add(new Paragraph(String.format("Total Sell Percentage: %.2f%%", totalSoldPercentage)));
		document.add(new Paragraph(String.format("Total earned from sales: $%.2f", earnedTotal)));
		document.add(new Paragraph(String.format("Total tickets in retention: $%.2f", totalRetention)));

		document.add(new Paragraph("\nSell percentage by locality:"));
		List<LocalityDataDTO> soldpercentage = new ArrayList<>();
		for (Locality loc : localities) {
			float soldPercentageLoc = (loc.getTicketsSold() / (float) loc.getMaxCapability()) * 100;
			soldpercentage.add(new LocalityDataDTO(loc, soldPercentageLoc));
			document.add(new Paragraph(String.format("%s: %.2f%%", loc.getName(), soldPercentageLoc)));
		}
		document.close();

		byte[] byteArray = baos.toByteArray();
		emailService.sendMailWPDFAttachment(mail, "Reporte de evento | AmaTickets",
				String.format("Aqui tienes tu reporte del evento \"%s\" del calendario \"%s\"", event.getName(),
						calendar.getName()),
				byteArray, "EventReport.pdf");
		return new EventReportDTO(byteArray, "EventReport.pdf", "application/pdf", totalSoldPercentage,
				soldpercentage, earnedTotal, capabilityTotal, ticketsSoldTotal);
	}

	private Event getEvent(String eventId, List<Event> events) {
		for (Event e : events)
			if (e.getId().equals(eventId))
				return e;
		return null;
	}

}
