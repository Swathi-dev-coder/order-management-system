package com.oms.order_service.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUtil jwtUtil;
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		// Allow unprotected endpoints (optional)
		String path = request.getRequestURI();
		return path.startsWith("/api/auth");  // adjust as needed
	}
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		System.out.println("JwtRequestFilter: incoming -> " + request.getMethod() + " " + request.getRequestURI());
		final String authHeader = request.getHeader("Authorization");
		System.out.println("JwtRequestFilter: Authorization header = " + authHeader);

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String jwtToken = authHeader.substring(7); 
			System.out.println("JwtRequestFilter: token (first 20 chars) = " + (jwtToken.length()>20?jwtToken.substring(0,20):jwtToken));

			if (jwtUtil.validateToken(jwtToken)) {
				String usernameFromToken = jwtUtil.getUsernameFromToken(jwtToken);
				String roleFromToken = jwtUtil.getRoleFromToken(jwtToken);
			    String emailFromToken = jwtUtil.getEmailFromToken(jwtToken);

				System.out.println("Role from token: " + roleFromToken);
				List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + roleFromToken));
				System.out.println("JwtRequestFilter: username and role from validator = " + usernameFromToken+" ,"+ roleFromToken);
				UsernamePasswordAuthenticationToken authToken =
						new UsernamePasswordAuthenticationToken(emailFromToken, null, authorities);
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			} else {
				// invalid token
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("Invalid token");
				System.out.println("JwtRequestFilter: token invalid -> returning 401");
				return;
			} 
		}else {
			System.out.println("JwtRequestFilter: No Authorization header or not Bearer; continuing.");
		}

		filterChain.doFilter(request, response);
	}

}
