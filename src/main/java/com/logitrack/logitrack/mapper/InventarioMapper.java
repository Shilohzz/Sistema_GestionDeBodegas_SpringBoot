package com.logitrack.logitrack.mapper;

import com.logitrack.logitrack.DTO.response.InventarioResponseDTO;
import com.logitrack.logitrack.model.Inventario;
import org.springframework.stereotype.Component;

@Component
public class InventarioMapper {

    private final BodegaMapper bodegaMapper;
    private final ProductoMapper productoMapper;

    public InventarioMapper(BodegaMapper bodegaMapper, ProductoMapper productoMapper) {
        this.bodegaMapper = bodegaMapper;
        this.productoMapper = productoMapper;
    }

    public InventarioResponseDTO entidadADTO(Inventario inventario) {
        if (inventario == null) return null;
        return new InventarioResponseDTO(
                inventario.getId(),
                bodegaMapper.entidadADTO(inventario.getBodega()),
                productoMapper.entidadADTO(inventario.getProducto()),
                inventario.getStockActual(),
                inventario.getStockMinimoAlerta()
        );
    }
}