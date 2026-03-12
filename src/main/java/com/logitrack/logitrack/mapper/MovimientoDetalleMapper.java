package com.logitrack.logitrack.mapper;

import com.logitrack.logitrack.DTO.response.MovimientoDetalleResponseDTO;
import com.logitrack.logitrack.model.MovimientoDetalle;
import org.springframework.stereotype.Component;

@Component
public class MovimientoDetalleMapper {

    private final ProductoMapper productoMapper;

    public MovimientoDetalleMapper(ProductoMapper productoMapper) {
        this.productoMapper = productoMapper;
    }

    public MovimientoDetalleResponseDTO entidadADTO(MovimientoDetalle detalle) {
        if (detalle == null) return null;
        return new MovimientoDetalleResponseDTO(
                detalle.getId(),
                productoMapper.entidadADTO(detalle.getProducto()),
                detalle.getCantidadUnidades()
        );
    }
}