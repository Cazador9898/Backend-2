package com.minimarket.security;

import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.UsuarioRepository;
import com.minimarket.security.model.CustomUserDetails;
import com.minimarket.security.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsername_usuarioValido_retornaUserDetailsConRol() {
        Usuario usuario = new Usuario();
        usuario.setUsername("admin");
        usuario.setPassword("passwordEncriptada");
        usuario.setRoles(Set.of(new Rol("ADMIN")));

        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(usuario));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("admin");

        assertEquals("admin", userDetails.getUsername());
        assertEquals("passwordEncriptada", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void loadUserByUsername_usuarioNoExiste_lanzaUsernameNotFoundException() {
        when(usuarioRepository.findByUsername("desconocido")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("desconocido"));
    }

    @Test
    void customUserDetails_rolConPrefijo_noDuplicaRole() {
        Usuario usuario = new Usuario();
        usuario.setUsername("cajero");
        usuario.setPassword("1234");
        usuario.setRoles(Set.of(new Rol("ROLE_CAJERO")));

        CustomUserDetails userDetails = new CustomUserDetails(usuario);

        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_CAJERO")));
        assertFalse(userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ROLE_CAJERO")));
    }
}
