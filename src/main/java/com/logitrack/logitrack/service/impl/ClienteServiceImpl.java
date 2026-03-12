package com.logitrack.logitrack.service.impl;

import com.logitrack.logitrack.DTO.request.ClienteRequestDTO;
import com.logitrack.logitrack.DTO.response.ClienteResponseDTO;
import com.logitrack.logitrack.mapper.ClienteMapper;
import com.logitrack.logitrack.model.Cliente;
import com.logitrack.logitrack.repository.ClienteRepository;
import com.logitrack.logitrack.service.ClienteService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    @Override
    public ClienteResponseDTO guardar(ClienteRequestDTO dto) {
        Cliente cliente = clienteMapper.DTOAEntidad(dto);
        Cliente guardado = clienteRepository.save(cliente);
        return clienteMapper.entidadADTO(guardado);
    }

    @Override
    public ClienteResponseDTO actualizar(Long id, ClienteRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + id));
        clienteMapper.actualizarEntidadDesdeDTO(cliente, dto);
        Cliente actualizado = clienteRepository.save(cliente);
        return clienteMapper.entidadADTO(actualizado);
    }

    @Override
    public ClienteResponseDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + id));
        return clienteMapper.entidadADTO(cliente);
    }

    @Override
    public List<ClienteResponseDTO> buscarTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(clienteMapper::entidadADTO)
                .toList();
    }

    @Override
    public void eliminar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + id));
        clienteRepository.delete(cliente);
    }
}