package com.logitrack.logitrack.repository;

import com.logitrack.logitrack.model.MovimientoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovimientoDetalleRepository extends JpaRepository<MovimientoDetalle, Long> {
    List<MovimientoDetalle> findByMovimientoId(Long movimientoId);
}