package com.minimarket.notificaciones.security;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
@Component
public class ApiKeyFilter extends OncePerRequestFilter {
 private final String expected;
 public ApiKeyFilter(@Value("${internal.api-key}") String expected) { this.expected = expected; }
 @Override protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
  if (req.getRequestURI().equals("/api/notificaciones") && HttpMethod.POST.matches(req.getMethod())
      && !expected.equals(req.getHeader("X-API-KEY"))) { res.sendError(401, "API key interna inválida"); return; }
  chain.doFilter(req,res);
 }
}
