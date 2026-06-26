package com.minimarket.security.config;

import com.minimarket.security.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**", "/h2-console/**").permitAll()

                        // Producto: solo administrador puede crear, modificar o eliminar productos.
                        .requestMatchers(HttpMethod.POST, "/api/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasRole("ADMIN")

                        // Inventario: solo administrador puede registrar, actualizar o eliminar movimientos.
                        .requestMatchers(HttpMethod.POST, "/api/inventario/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/inventario/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/inventario/**").hasRole("ADMIN")

                        // Venta: solo cajero puede generar ventas.
                        .requestMatchers(HttpMethod.POST, "/api/ventas/**").hasRole("CAJERO")

                        // Usuarios: solo administrador puede gestionar usuarios.
                        .requestMatchers("/api/usuarios/**").hasRole("ADMIN")

                        // Consultas generales: usuario autenticado.
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .logout(logout -> logout.permitAll())
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
