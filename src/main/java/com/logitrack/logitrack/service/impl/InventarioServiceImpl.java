package com.logitrack.logitrack.service.impl;

import com.logitrack.logitrack.DTO.response.InventarioResponseDTO;
import com.logitrack.logitrack.mapper.InventarioMapper;
import com.logitrack.logitrack.repository.InventarioRepository;
import com.logitrack.logitrack.service.InventarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;
    private final InventarioMapper inventarioMapper;

    @Override
    public InventarioResponseDTO buscarPorId(Long id) {
        return inventarioRepository.findById(id)
                .map(inventarioMapper::entidadADTO)
                .orElseThrow(() -> new EntityNotFoundException("Inventario no encontrado con id: " + id));
    }

    @Override
    public List<InventarioResponseDTO> buscarTodos() {
        return inventarioRepository.findAll()
                .stream()
                .map(inventarioMapper::entidadADTO)
                .toList();
    }

    @Override
    public List<InventarioResponseDTO> buscarStockBajo() {
        return inventarioRepository.findByStockActualLessThan(10) // <- menor a 10
                .stream()
                .map(inventarioMapper::entidadADTO)
                .toList();
    }
}