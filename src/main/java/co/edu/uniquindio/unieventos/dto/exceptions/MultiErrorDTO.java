package co.edu.uniquindio.unieventos.dto.exceptions;

import java.util.List;

public record MultiErrorDTO(int code, String message, List<String> errors) {}
