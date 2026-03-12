package com.logitrack.logitrack.service;

import com.logitrack.logitrack.DTO.response.AuditoriaResponseDTO;
import java.util.List;

public interface AuditoriaService {
    AuditoriaResponseDTO buscarPorId(Long id);
    List<AuditoriaResponseDTO> buscarTodos();
    List<AuditoriaResponseDTO> buscarPorUsuario(Long usuarioId);
    List<AuditoriaResponseDTO> buscarPorTipoOperacion(String tipoOperacion);
    void registrar(String tipoOperacion, String entidad, Long idRegistro,
                   String valoresAnteriores, String valoresNuevos, Long usuarioId);
}