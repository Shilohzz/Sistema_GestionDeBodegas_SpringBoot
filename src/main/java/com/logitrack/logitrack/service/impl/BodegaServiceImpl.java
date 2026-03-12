package com.logitrack.logitrack.service.impl;

import com.logitrack.logitrack.DTO.request.BodegaRequestDTO;
import com.logitrack.logitrack.DTO.response.BodegaResponseDTO;
import com.logitrack.logitrack.mapper.BodegaMapper;
import com.logitrack.logitrack.model.Bodega;
import com.logitrack.logitrack.model.Usuario;
import com.logitrack.logitrack.repository.BodegaRepository;
import com.logitrack.logitrack.repository.UsuarioRepository;
import com.logitrack.logitrack.service.BodegaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BodegaServiceImpl implements BodegaService {

    private final BodegaRepository bodegaRepository;
    private final UsuarioRepository usuarioRepository;
    private final BodegaMapper bodegaMapper;

    @Override
    public BodegaResponseDTO guardar(BodegaRequestDTO dto) {
        Usuario encargado = usuarioRepository.findById(dto.encargadoId()) // Me comunico con la base de datos y busco al encargado.
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + dto.encargadoId()));
        Bodega bodega = bodegaMapper.DTOAEntidad(dto, encargado); // creo el objeto
        Bodega guardada = bodegaRepository.save(bodega); // lo guardo en bd
        return bodegaMapper.entidadADTO(guardada); // devuelvo el resultado
    }

    @Override
    public BodegaResponseDTO actualizar(Long id, BodegaRequestDTO dto) {
        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bodega no encontrada con id: " + id));
        Usuario encargado = usuarioRepository.findById(dto.encargadoId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + dto.encargadoId()));
        bodegaMapper.actualizarEntidadDesdeDTO(bodega, dto, encargado);
        Bodega actualizada = bodegaRepository.save(bodega);
        return bodegaMapper.entidadADTO(actualizada);
    }

    @Override
    public BodegaResponseDTO buscarPorId(Long id) {
        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bodega no encontrada con id: " + id));
        return bodegaMapper.entidadADTO(bodega);
    }

    @Override
    public List<BodegaResponseDTO> buscarTodos() {
        return bodegaRepository.findAll()
                .stream()
                .map(bodegaMapper::entidadADTO)
                .toList();
    }

    @Override
    public void eliminar(Long id) {
        Bodega bodega = bodegaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bodega no encontrada con id: " + id));
        bodegaRepository.delete(bodega);
    }
}