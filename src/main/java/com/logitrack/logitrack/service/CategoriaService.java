package com.logitrack.logitrack.service;

import com.logitrack.logitrack.DTO.request.CategoriaRequestDTO;
import com.logitrack.logitrack.DTO.response.CategoriaResponseDTO;
import java.util.List;

public interface CategoriaService {
    // GUARDAR
    CategoriaResponseDTO guardar(CategoriaRequestDTO dto);
    // ACTUALIZAR
    CategoriaResponseDTO actualizar(Long id, CategoriaRequestDTO dto);
    // FILTRAR POR ID
    CategoriaResponseDTO buscarPorId(Long id);
    // LISTAR TODO
    List<CategoriaResponseDTO> buscarTodos();
    // ELIMINAR POR ID
    void eliminar(Long id);
}