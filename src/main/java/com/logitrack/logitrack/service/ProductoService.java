package com.logitrack.logitrack.service;

import com.logitrack.logitrack.DTO.request.ProductoRequestDTO;
import com.logitrack.logitrack.DTO.response.ProductoResponseDTO;
import java.util.List;

public interface ProductoService {
    ProductoResponseDTO guardar(ProductoRequestDTO dto);
    ProductoResponseDTO actualizar(Long id, ProductoRequestDTO dto);
    ProductoResponseDTO buscarPorId(Long id);
    List<ProductoResponseDTO> buscarTodos();
    void eliminar(Long id);
}