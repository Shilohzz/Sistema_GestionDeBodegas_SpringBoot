package com.logitrack.logitrack.service.impl;

import com.logitrack.logitrack.DTO.request.CategoriaRequestDTO;
import com.logitrack.logitrack.DTO.response.CategoriaResponseDTO;
import com.logitrack.logitrack.mapper.CategoriaMapper;
import com.logitrack.logitrack.model.Categoria;
import com.logitrack.logitrack.repository.CategoriaRepository;
import com.logitrack.logitrack.service.CategoriaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    // SERVICE PARA GUARDAR
    @Override
    public CategoriaResponseDTO guardar(CategoriaRequestDTO dto) {
        Categoria categoria = categoriaMapper.DTOAEntidad(dto);
        Categoria guardada = categoriaRepository.save(categoria);
        return categoriaMapper.entidadADTO(guardada);
    }

    // SERVICE PARA ACTUALIZAR
    @Override
    public CategoriaResponseDTO actualizar(Long id, CategoriaRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con id: " + id));
        categoriaMapper.actualizarEntidadDesdeDTO(categoria, dto);
        Categoria actualizada = categoriaRepository.save(categoria);
        return categoriaMapper.entidadADTO(actualizada);
    }

    // SERVICE PARA FILTRAR POR ID
    @Override
    public CategoriaResponseDTO buscarPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con id: " + id));
        return categoriaMapper.entidadADTO(categoria);
    }

    // SERVICE PARA LISTAR TODO
    @Override
    public List<CategoriaResponseDTO> buscarTodos() {
        return categoriaRepository.findAll()
                .stream()
                .map(categoriaMapper::entidadADTO) // Con map convierto cada elemento que encuentre, en un DTO pra luego pasarlo a una lista.
                .toList();
    }

    // SERVICE PARA ELIMINAR
    @Override
    public void eliminar(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con id: " + id));
        categoriaRepository.delete(categoria);
    }
}