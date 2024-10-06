package co.edu.uniquindio.unieventos.dto.misc;

public record MessageDTO<T>(
		boolean error,
		T respuesta) {}
