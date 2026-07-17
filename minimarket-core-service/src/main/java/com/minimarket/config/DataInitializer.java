package com.minimarket.config;

import com.minimarket.entity.Categoria;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.CategoriaRepository;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.repository.RolRepository;
import com.minimarket.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner seedData(RolRepository roles, UsuarioRepository usuarios,
                               CategoriaRepository categorias, ProductoRepository productos,
                               PasswordEncoder encoder) {
        return args -> {
            Rol admin = roles.findByNombre("ROLE_ADMIN").orElseGet(() -> roles.save(new Rol("ROLE_ADMIN")));
            Rol cajero = roles.findByNombre("ROLE_CAJERO").orElseGet(() -> roles.save(new Rol("ROLE_CAJERO")));
            Rol cliente = roles.findByNombre("ROLE_CLIENTE").orElseGet(() -> roles.save(new Rol("ROLE_CLIENTE")));
            crearUsuarioSiNoExiste(usuarios, encoder, "admin", "Admin123!", Set.of(admin));
            crearUsuarioSiNoExiste(usuarios, encoder, "cajero", "Cajero123!", Set.of(cajero));
            crearUsuarioSiNoExiste(usuarios, encoder, "cliente", "Cliente123!", Set.of(cliente));

            if (categorias.count() == 0) {
                Categoria abarrotes = new Categoria(); abarrotes.setNombre("Abarrotes"); categorias.save(abarrotes);
                Producto arroz = new Producto();
                arroz.setNombre("Arroz 1 kg"); arroz.setPrecio(1490.0); arroz.setStock(50); arroz.setCategoria(abarrotes);
                productos.save(arroz);
            }
        };
    }

    private void crearUsuarioSiNoExiste(UsuarioRepository repo, PasswordEncoder encoder,
                                         String username, String password, Set<Rol> roles) {
        if (!repo.existsByUsername(username)) {
            Usuario usuario = new Usuario();
            usuario.setUsername(username);
            usuario.setPassword(encoder.encode(password));
            usuario.setRoles(roles);
            repo.save(usuario);
        }
    }
}
