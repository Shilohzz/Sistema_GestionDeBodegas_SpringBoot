package com.logitrack.logitrack.controller;

import com.logitrack.logitrack.DTO.response.ReporteMovimientosDTO;
import com.logitrack.logitrack.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final MovimientoRepository movimientoRepository;

    // GET /api/reportes/movimientos
    @GetMapping("/movimientos")
    public ResponseEntity<ReporteMovimientosDTO> reporteMovimientos() {
        ReporteMovimientosDTO reporte = new ReporteMovimientosDTO(
                movimientoRepository.count(), movimientoRepository.countByTipo("ENTRADA"), movimientoRepository.countByTipo("SALIDA"), movimientoRepository.countByTipo("TRANSFERENCIA")
        );
        return ResponseEntity.ok(reporte);
    }
}
