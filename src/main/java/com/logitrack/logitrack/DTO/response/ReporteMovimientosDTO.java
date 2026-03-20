package com.logitrack.logitrack.DTO.response;

public record ReporteMovimientosDTO(
        long totalMovimientos,
        long totalEntradas,
        long totalSalidas,
        long totalTransferencias
) {}