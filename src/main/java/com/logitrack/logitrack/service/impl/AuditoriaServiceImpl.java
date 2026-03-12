package com.logitrack.logitrack.service.impl;

import com.logitrack.logitrack.DTO.response.AuditoriaResponseDTO;
import com.logitrack.logitrack.mapper.AuditoriaMapper;
import com.logitrack.logitrack.model.Auditoria;
import com.logitrack.logitrack.model.Usuario;
import com.logitrack.logitrack.repository.AuditoriaRepository;
import com.logitrack.logitrack.repository.UsuarioRepository;
import com.logitrack.logitrack.service.AuditoriaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditoriaServiceImpl implements AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final AuditoriaMapper auditoriaMapper;

    @Override
    public AuditoriaResponseDTO buscarPorId(Long id) {
        Auditoria auditoria = auditoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Auditoría no encontrada con id: " + id));
        return auditoriaMapper.entidadADTO(auditoria);
    }

    @Override
    public List<AuditoriaResponseDTO> buscarTodos() {
        return auditoriaRepository.findAll()
                .stream()
                .map(auditoriaMapper::entidadADTO)
                .toList();
    }

    @Override
    public List<AuditoriaResponseDTO> buscarPorUsuario(Long usuarioId) {
        return auditoriaRepository.findByUsuarioEjecutorId(usuarioId)
                .stream()
                .map(auditoriaMapper::entidadADTO)
                .toList();
    }

    @Override
    public List<AuditoriaResponseDTO> buscarPorTipoOperacion(String tipoOperacion) {
        return auditoriaRepository.findByTipoOperacion(tipoOperacion)
                .stream()
                .map(auditoriaMapper::entidadADTO)
                .toList();
    }

    @Override
    public void registrar(String tipoOperacion, String entidad, Long idRegistro,
                          String valoresAnteriores, String valoresNuevos, Long usuarioId) {
        Auditoria auditoria = new Auditoria();
        auditoria.setTipoOperacion(tipoOperacion);
        auditoria.setNombreEntidadAfectada(entidad);
        auditoria.setIdRegistroAfectado(idRegistro);
        auditoria.setValoresAnteriores(valoresAnteriores);
        auditoria.setValoresNuevos(valoresNuevos);
        auditoria.setFechaHoraExacta(LocalDateTime.now());

        if (usuarioId != null) {
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + usuarioId));
            auditoria.setUsuarioEjecutor(usuario);
        }

        auditoriaRepository.save(auditoria);
    }
}