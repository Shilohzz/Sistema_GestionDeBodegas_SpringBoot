package com.logitrack.logitrack.mapper;

import com.logitrack.logitrack.DTO.request.ProductoRequestDTO;
import com.logitrack.logitrack.DTO.response.ProductoResponseDTO;
import com.logitrack.logitrack.model.Categoria;
import com.logitrack.logitrack.model.Cliente;
import com.logitrack.logitrack.model.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    private final CategoriaMapper categoriaMapper;
    private final ClienteMapper clienteMapper;

    public ProductoMapper(CategoriaMapper categoriaMapper, ClienteMapper clienteMapper) {
        this.categoriaMapper = categoriaMapper;
        this.clienteMapper = clienteMapper;
    }

    public ProductoResponseDTO entidadADTO(Producto producto) {
        if (producto == null) return null;
        return new ProductoResponseDTO(
                producto.getId(),
                producto.getNombreComercial(),
                producto.getDescripcion(),
                categoriaMapper.entidadADTO(producto.getCategoria()),
                clienteMapper.entidadADTO(producto.getClientePropietario()),
                producto.getEstaActivo()
        );
    }

    public Producto DTOAEntidad(ProductoRequestDTO dto, Categoria categoria, Cliente cliente) {
        if (dto == null || categoria == null || cliente == null) return null;
        Producto producto = new Producto();
        producto.setNombreComercial(dto.nombreComercial());
        producto.setDescripcion(dto.descripcion());
        producto.setCategoria(categoria);
        producto.setClientePropietario(cliente);
        return producto;
    }

    public void actualizarEntidadDesdeDTO(Producto producto, ProductoRequestDTO dto, Categoria categoria, Cliente cliente) {
        if (producto == null || dto == null) return;
        producto.setNombreComercial(dto.nombreComercial());
        producto.setDescripcion(dto.descripcion());
        producto.setCategoria(categoria);
        producto.setClientePropietario(cliente);
    }
}