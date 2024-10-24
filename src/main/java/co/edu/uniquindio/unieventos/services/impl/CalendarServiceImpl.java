package co.edu.uniquindio.unieventos.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import co.edu.uniquindio.unieventos.config.Mappers;
import co.edu.uniquindio.unieventos.dto.calendar.CalendarDTO;
import co.edu.uniquindio.unieventos.dto.calendar.CreateCalendarDTO;
import co.edu.uniquindio.unieventos.dto.calendar.EditCalendarDTO;
import co.edu.uniquindio.unieventos.dto.calendar.SearchPageDTO;
import co.edu.uniquindio.unieventos.exceptions.ConflictException;
import co.edu.uniquindio.unieventos.exceptions.DocumentFoundException;
import co.edu.uniquindio.unieventos.exceptions.DocumentNotFoundException;
import co.edu.uniquindio.unieventos.model.documents.Calendar;
import co.edu.uniquindio.unieventos.model.vo.Event;
import co.edu.uniquindio.unieventos.repositories.CalendarRepository;
import co.edu.uniquindio.unieventos.services.CalendarService;
import co.edu.uniquindio.unieventos.services.ImagesService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Service
public class CalendarServiceImpl implements CalendarService {

	@Autowired
	private CalendarRepository calendarRepository;
	@Autowired
	private ImagesService imagesService;
	@Autowired
	private Mappers mappers;

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

	@Override
	public CalendarDTO editCalendar(@Valid EditCalendarDTO dto) throws Exception {
		Calendar calendar = calendarRepository.findById(dto.getId()).orElseThrow(()-> new DocumentNotFoundException("El calendario no fue encontrado"));
		if(!calendar.getName().equals(dto.getName())) {
			Calendar calendarFound = calendarRepository.findByName(dto.getName()).orElse(null);
			if (calendarFound != null)
				new ConflictException("Ya hay un calendario con ese evento");
			calendar.setName(dto.getName());
		}
		calendar.setDescription(dto.getDescription());

		if (dto.getImage() != null) {
			String image = imagesService.uploadImage(dto.getImage());
			calendar.setImage(image);
		}
		if (dto.getBannerImage() != null) {
			String bannerImage = imagesService.uploadImage(dto.getBannerImage());
			calendar.setBannerImage(bannerImage);
		}
		calendarRepository.save(calendar);
		return mappers.getCalendarMapper().apply(calendar);
	}

	@Override
	public void deleteCalendar(@NotNull String id) {
		calendarRepository.deleteById(id);
	}

	@Override
	public List<CalendarDTO> searchDalendars(@Valid SearchPageDTO dto) {
		Sort sort = Sort.by("name");
		if (dto.asc())
			sort = sort.ascending();
		else
			sort = sort.descending();
		Pageable pageable = PageRequest.of(dto.page(), dto.size(), sort);
		return calendarRepository.findAll(pageable)
				.stream()
				.map(mappers.getCalendarMapper())
				.collect(Collectors.toList());
	}

}
