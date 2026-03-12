package com.logitrack.logitrack.service;

import com.logitrack.logitrack.DTO.request.BodegaRequestDTO;
import com.logitrack.logitrack.DTO.response.BodegaResponseDTO;
import java.util.List;

public interface BodegaService {
    BodegaResponseDTO guardar(BodegaRequestDTO dto);
    BodegaResponseDTO actualizar(Long id, BodegaRequestDTO dto);
    BodegaResponseDTO buscarPorId(Long id);
    List<BodegaResponseDTO> buscarTodos();
    void eliminar(Long id);
}