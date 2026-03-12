package com.logitrack.logitrack.mapper;

import com.logitrack.logitrack.DTO.response.MovimientoResponseDTO;
import com.logitrack.logitrack.model.Movimiento;
import com.logitrack.logitrack.model.MovimientoDetalle;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class MovimientoMapper {

    private final UsuarioMapper usuarioMapper;
    private final BodegaMapper bodegaMapper;
    private final MovimientoDetalleMapper detalleMapper;

    public MovimientoMapper(UsuarioMapper usuarioMapper, BodegaMapper bodegaMapper,
                            MovimientoDetalleMapper detalleMapper) {
        this.usuarioMapper = usuarioMapper;
        this.bodegaMapper = bodegaMapper;
        this.detalleMapper = detalleMapper;
    }

    public MovimientoResponseDTO entidadADTO(Movimiento movimiento, List<MovimientoDetalle> detalles) {
        if (movimiento == null) return null;
        return new MovimientoResponseDTO(
                movimiento.getId(),
                movimiento.getFechaHora(),
                movimiento.getTipo(),
                usuarioMapper.entidadADTO(movimiento.getUsuarioResponsable()),
                bodegaMapper.entidadADTO(movimiento.getBodegaOrigen()),
                bodegaMapper.entidadADTO(movimiento.getBodegaDestino()),
                movimiento.getObservacion(),
                detalles.stream().map(detalleMapper::entidadADTO).toList()
        );
    }
}