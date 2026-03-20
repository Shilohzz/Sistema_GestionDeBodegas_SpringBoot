package com.logitrack.logitrack.controller;

import com.logitrack.logitrack.DTO.response.ReporteMovimientosDTO;
import com.logitrack.logitrack.repository.MovimientoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "REPORTES DE MOVIMIENTOS", description = "Aquí se puede generar reportes para filtrar información.")
@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final MovimientoRepository movimientoRepository;

    // GET /api/reportes/movimientos
    @Operation(summary = "")
    @GetMapping("/movimientos")
    public ResponseEntity<ReporteMovimientosDTO> reporteMovimientos() {
        ReporteMovimientosDTO reporte = new ReporteMovimientosDTO(
                movimientoRepository.count(), movimientoRepository.countByTipo("ENTRADA"), movimientoRepository.countByTipo("SALIDA"), movimientoRepository.countByTipo("TRANSFERENCIA")
        );
        return ResponseEntity.ok(reporte);
    }
}
