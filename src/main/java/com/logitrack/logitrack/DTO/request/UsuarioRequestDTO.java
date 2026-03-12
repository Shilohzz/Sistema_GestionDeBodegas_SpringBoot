package com.logitrack.logitrack.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
        @NotBlank(message = "El nombre completo no puede estar vacío.")
        @Size(min = 2, max = 150, message = "El nombre debe tener entre 2 y 150 caracteres.")
        String nombreCompleto,

        @NotBlank(message = "El email no puede estar vacío.")
        String emailInstitucional,

        @NotBlank(message = "La contraseña no puede estar vacía.")
        @Size(min = 6, message = "La contraseña debe tener mínimo 6 caracteres.")
        String contrasena,

        @NotBlank(message = "El rol no puede estar vacío.")
        String rol
) {}
