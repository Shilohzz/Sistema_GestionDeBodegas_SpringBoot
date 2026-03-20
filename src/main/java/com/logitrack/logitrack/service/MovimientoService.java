package com.logitrack.logitrack.service;

import com.logitrack.logitrack.DTO.request.MovimientoRequestDTO;
import com.logitrack.logitrack.DTO.response.MovimientoResponseDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoService {
    MovimientoResponseDTO registrar(MovimientoRequestDTO dto, String emailUsuario);
    MovimientoResponseDTO buscarPorId(Long id);
    List<MovimientoResponseDTO> buscarTodos();
    List<MovimientoResponseDTO> buscarPorRangoDeFechas(LocalDateTime inicio, LocalDateTime fin);
    List<MovimientoResponseDTO> listarRecientes();
}