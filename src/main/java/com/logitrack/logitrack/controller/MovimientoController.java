package com.logitrack.logitrack.controller;

import com.logitrack.logitrack.DTO.request.MovimientoRequestDTO;
import com.logitrack.logitrack.DTO.response.MovimientoResponseDTO;
import com.logitrack.logitrack.service.impl.MovimientoServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/movimiento")
@RequiredArgsConstructor
@Tag(name = "MOVIMIENTOS DE MERCANCÍA", description = "Aquí registras entradas, salidas y transferencias de inventario.")
public class MovimientoController {

    // lógica de negocio
    private final MovimientoServiceImpl movimientoService;

    // Tipo POST /api/movimiento (URL), registra un nuevo movimiento
    @Operation(summary = "Registrar movimiento", description = "Mueve productos. Recuerda: Si es entrada externa, no pongas bodega de origen. Si es salida, no pongas destino.")
    @PostMapping
    public ResponseEntity<MovimientoResponseDTO> registrar(@Valid @RequestBody MovimientoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(movimientoService.registrar(dto, dto.emailUsuario()));
    }

    // Tipo GET /api/movimiento (URL), devuelve todos los movimientos
    @Operation(summary = "Ver historial de movimientos", description = "Lista cronológicamente todo lo que ha entrado y salido.")
    @GetMapping
    public ResponseEntity<List<MovimientoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(movimientoService.buscarTodos());
    }

    // Tipo GET /api/movimiento/1 (URL), devuelve el movimiento con id 1
    @Operation(summary = "Ver detalle del movimiento", description = "Mira exactamente qué productos se movieron en esta operación específica.")
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(movimientoService.buscarPorId(id));
    }

    // Tipo GET /api/movimiento/fechas?inicio=2026-01-01T00:00:00&fin=2026-03-01T00:00:00 (URL)
    // Devuelve los movimientos en ese rango de fechas
    @Operation(summary = "Filtrar por fechas", description = "Busca movimientos ocurridos en un periodo de tiempo específico.")
    @GetMapping("/fechas")
    public ResponseEntity<List<MovimientoResponseDTO>> buscarPorFechas(
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fin) {
        return ResponseEntity.ok(movimientoService.buscarPorRangoDeFechas(inicio, fin));
    }


    // Tipo GetMapping para obtener la lista que esperamos
    @GetMapping("/recientes")
    public ResponseEntity<List<MovimientoResponseDTO>> listarRecientes() {
        return ResponseEntity.ok(movimientoService.listarRecientes());
    }

}