package com.logitrack.logitrack.service.impl;

import com.logitrack.logitrack.DTO.request.CategoriaRequestDTO;
import com.logitrack.logitrack.DTO.response.CategoriaResponseDTO;
import com.logitrack.logitrack.mapper.CategoriaMapper;
import com.logitrack.logitrack.model.Categoria;
import com.logitrack.logitrack.repository.CategoriaRepository;
import com.logitrack.logitrack.service.AuditoriaService;
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
    private final AuditoriaService auditoriaService;

    @Override
    public CategoriaResponseDTO guardar(CategoriaRequestDTO dto) {
        Categoria categoria = categoriaMapper.DTOAEntidad(dto);
        Categoria guardada = categoriaRepository.save(categoria);

        // Registra auditoría del INSERT
        auditoriaService.registrar(
                "INSERT",
                "categoria",
                guardada.getId(),
                null,
                "nombre: " + guardada.getNombre() + ", descripcion: " + guardada.getDescripcion(),
                null
        );

        return categoriaMapper.entidadADTO(guardada);
    }

    @Override
    public CategoriaResponseDTO actualizar(Long id, CategoriaRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con id: " + id));

        // Captura valores ANTES de modificar
        String antes = "nombre: " + categoria.getNombre() + ", descripcion: " + categoria.getDescripcion();

        categoriaMapper.actualizarEntidadDesdeDTO(categoria, dto);
        Categoria actualizada = categoriaRepository.save(categoria);

        // Registra auditoría del UPDATE
        auditoriaService.registrar(
                "UPDATE",
                "categoria",
                actualizada.getId(),
                antes,
                "nombre: " + actualizada.getNombre() + ", descripcion: " + actualizada.getDescripcion(),
                null
        );

        return categoriaMapper.entidadADTO(actualizada);
    }

    @Override
    public CategoriaResponseDTO buscarPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con id: " + id));
        return categoriaMapper.entidadADTO(categoria);
    }

    @Override
    public List<CategoriaResponseDTO> buscarTodos() {
        return categoriaRepository.findAll()
                .stream()
                .map(categoriaMapper::entidadADTO)
                .toList();
    }

    @Override
    public void eliminar(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con id: " + id));

        // Captura valores ANTES de eliminar
        String antes = "nombre: " + categoria.getNombre() + ", descripcion: " + categoria.getDescripcion();

        categoriaRepository.delete(categoria);

        // Registra auditoría del DELETE
        auditoriaService.registrar(
                "DELETE",
                "categoria",
                id,
                antes,
                null,
                null
        );
    }
}