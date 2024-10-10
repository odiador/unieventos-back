package co.edu.uniquindio.unieventos.services.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniquindio.unieventos.config.Mappers;
import co.edu.uniquindio.unieventos.dto.calendar.CalendarDTO;
import co.edu.uniquindio.unieventos.dto.calendar.CreateCalendarDTO;
import co.edu.uniquindio.unieventos.exceptions.DocumentFoundException;
import co.edu.uniquindio.unieventos.exceptions.DocumentNotFoundException;
import co.edu.uniquindio.unieventos.model.documents.Calendar;
import co.edu.uniquindio.unieventos.model.vo.Event;
import co.edu.uniquindio.unieventos.repositories.CalendarRepository;
import co.edu.uniquindio.unieventos.services.CalendarService;
import co.edu.uniquindio.unieventos.services.ImagesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

	@Autowired
	private CalendarRepository calendarRepository;
	@Autowired
	private ImagesService imagesService;
	
	private final Mappers mappers;

	@Override
	public CalendarDTO createCalendar(@Valid CreateCalendarDTO dto) throws Exception {
		if (calendarRepository.findByName(dto.getName()).isPresent())
			throw new DocumentFoundException("Elige otro nombre para tu calendario");
		String image = imagesService.uploadImage(dto.getImage());
		String bannerImage = imagesService.uploadImage(dto.getBannerImage());
		Calendar calendar = Calendar.builder()
				.name(dto.getName())
				.description(dto.getDescription())
				.events(new ArrayList<Event>())
				.image(image)
				.bannerImage(bannerImage)
				.build();
		calendarRepository.save(calendar);
		return mappers.getCalendarMapper().apply(calendar);
	}

	@Override
	public CalendarDTO findCalendarById(String id) throws Exception {
		return mappers.getCalendarMapper()
				.apply(calendarRepository.findById(id)
				.orElseThrow(() -> new DocumentNotFoundException("El calendario con ese ID no fue encontrado")));
	}

}
