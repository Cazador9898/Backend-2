package com.minimarket.notificaciones.config;
import com.minimarket.notificaciones.security.ApiKeyFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
public class SecurityConfig {
 @Bean SecurityFilterChain chain(HttpSecurity http, ApiKeyFilter filter) throws Exception {
  return http.csrf(c->c.disable()).authorizeHttpRequests(a->a.anyRequest().permitAll())
   .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class).build();
 }
}
