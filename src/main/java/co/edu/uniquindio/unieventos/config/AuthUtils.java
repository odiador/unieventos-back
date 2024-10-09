package co.edu.uniquindio.unieventos.config;

import org.springframework.stereotype.Component;

import co.edu.uniquindio.unieventos.exceptions.UnauthorizedAccessException;
import co.edu.uniquindio.unieventos.model.enums.Role;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class AuthUtils {

	public boolean validateRole(HttpServletRequest request, Role role) {
		Object attribute = request.getAttribute("role");
		if (attribute == null)
			return true;
		return !role.name().equals(attribute.toString());
	}

	public boolean validateRoleMinClient(HttpServletRequest request) {
		Object attribute = request.getAttribute("role");
		if (attribute == null)
			return true;
		Role role = Role.valueOf(attribute.toString());
		switch (role) {
		case CLIENT:
		case ADMINISTRATOR:
			return false;
		default:
			return true;
		}
	}

	public void verifyMail(String email, HttpServletRequest request) throws UnauthorizedAccessException {
		if (!email.equals(request.getAttribute("email")))
			throw new UnauthorizedAccessException("No tienes permiso para editar esta cuenta.");
	}

	public void verifyMinRoleClient(HttpServletRequest request) throws UnauthorizedAccessException {
		Object roleAttr = request.getAttribute("role");
		if (roleAttr == null)
			throw new UnauthorizedAccessException("No tienes permiso para realizar esta acción.");
		Role role = Role.valueOf(roleAttr.toString());
		switch (role) {
		case CLIENT:
		case ADMINISTRATOR:

			break;
		default:
			throw new UnauthorizedAccessException("No tienes permiso para realizar esta acción.");
		}
	}

	public void verifyRoleAdmin(HttpServletRequest request) throws UnauthorizedAccessException {
		Object roleAttr = request.getAttribute("role");
		if (roleAttr == null || !roleAttr.toString().equals(Role.ADMINISTRATOR.name()))
			throw new UnauthorizedAccessException("No tienes permiso para realizar esta acción.");
	}
}
