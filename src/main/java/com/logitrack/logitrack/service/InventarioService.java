package com.logitrack.logitrack.service;

import com.logitrack.logitrack.DTO.response.InventarioResponseDTO;
import java.util.List;

public interface InventarioService {
    InventarioResponseDTO buscarPorId(Long id);
    List<InventarioResponseDTO> buscarTodos();
    List<InventarioResponseDTO> buscarStockBajo();
}