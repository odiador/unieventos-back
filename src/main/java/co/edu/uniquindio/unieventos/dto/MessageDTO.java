package co.edu.uniquindio.unieventos.dto;

public record MessageDTO<T>(
		boolean error,
		T respuesta) {}
