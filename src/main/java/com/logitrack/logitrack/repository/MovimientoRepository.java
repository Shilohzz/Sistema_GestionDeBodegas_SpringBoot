package com.logitrack.logitrack.repository;

import com.logitrack.logitrack.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

    // Esta es la consulta JPA para los últimos 10 movimientos
    List<Movimiento> findTop10ByOrderByFechaHoraDesc();

    // Esta es la consulta JPA para filtrar con la condición de "contar por tipo"
    long countByTipo(String tipo);


}