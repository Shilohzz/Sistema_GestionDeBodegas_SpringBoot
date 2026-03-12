package com.logitrack.logitrack.service;

import com.logitrack.logitrack.DTO.request.ClienteRequestDTO;
import com.logitrack.logitrack.DTO.response.ClienteResponseDTO;
import java.util.List;

public interface ClienteService {
    ClienteResponseDTO guardar(ClienteRequestDTO dto);
    ClienteResponseDTO actualizar(Long id, ClienteRequestDTO dto);
    ClienteResponseDTO buscarPorId(Long id);
    List<ClienteResponseDTO> buscarTodos();
    void eliminar(Long id);
}