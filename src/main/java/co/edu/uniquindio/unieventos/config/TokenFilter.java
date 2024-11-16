package co.edu.uniquindio.unieventos.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.uniquindio.unieventos.dto.exceptions.ErrorDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TokenFilter extends OncePerRequestFilter {

	@Autowired
	private JWTUtils jwtUtils;
	@Autowired
	private AuthUtils authUtils;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// Configuración de cabeceras para CORS
		String origin = request.getHeader("Origin");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Origin", origin);
		request.setAttribute("origin", origin);
		response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, Content-Type, Authorization");
		if (request.getMethod().equals("OPTIONS")) {
			response.setStatus(HttpServletResponse.SC_OK);
			return;
		}
		boolean error = false;
		try {
			String requestURI = request.getRequestURI();

			addAtributes(request);

			if (requestURI.startsWith("/api/clients") && authUtils.validateRoleMinClient(request)) {
				error = crearRespuestaError("No tiene permisos para acceder a este recurso",
						HttpServletResponse.SC_FORBIDDEN, response);
			}
		} catch (MalformedJwtException | SignatureException e) {
			error = crearRespuestaError("El token es incorrecto", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response);
		} catch (ExpiredJwtException e) {
			error = crearRespuestaError("El token está vencido", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response);
		} catch (Exception e) {
			error = crearRespuestaError(e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response);
		}

		if (!error) {
			filterChain.doFilter(request, response);
		}

	}

	private void addAtributes(HttpServletRequest request) {
		try {
			String token = jwtUtils.getToken(request);
			if (token != null) {
				Jws<Claims> jws = jwtUtils.parseJwt(token);
				Claims payload = jws.getPayload();
				request.setAttribute("email", payload.getSubject());
				request.setAttribute("id", payload.get("id"));
				request.setAttribute("name", payload.get("name"));
				request.setAttribute("role", payload.get("role"));
			}
		} catch (Exception e) {
		}
	}

	private boolean crearRespuestaError(String message, int code, HttpServletResponse response) throws IOException {
		ErrorDTO dto = new ErrorDTO(code, message);

		response.setContentType("application/json");
		response.setStatus(code);
		response.getWriter().write(new ObjectMapper().writeValueAsString(dto));
		response.getWriter().flush();
		response.getWriter().close();
		return true;
	}

}
