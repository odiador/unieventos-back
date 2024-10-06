package co.edu.uniquindio.unieventos.dto.exceptions;

import java.util.List;

public record InvalidFieldsDTO(int code, List<InvalidFieldDTO> errors) {}
