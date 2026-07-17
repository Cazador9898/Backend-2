package com.minimarket.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Público", description = "Endpoint público utilizado para validar que la API esté disponible.")
@RestController
public class HolaMundoController {

    @Operation(summary = "Saludo público", description = "Retorna un mensaje simple para verificar que el backend responde correctamente.")
    @ApiResponse(responseCode = "200", description = "API disponible")
    @GetMapping("/public/hola")
    public String holaMundo() {
        return "¡Hola Mundo!";
    }
}
