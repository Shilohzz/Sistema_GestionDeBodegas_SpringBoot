package com.logitrack.logitrack.mapper;

import com.logitrack.logitrack.DTO.response.AuditoriaResponseDTO;
import com.logitrack.logitrack.model.Auditoria;
import org.springframework.stereotype.Component;

@Component
public class AuditoriaMapper {

    private final UsuarioMapper usuarioMapper;

    public AuditoriaMapper(UsuarioMapper usuarioMapper) {
        this.usuarioMapper = usuarioMapper;
    }

    public AuditoriaResponseDTO entidadADTO(Auditoria auditoria) {
        if (auditoria == null) return null;
        return new AuditoriaResponseDTO(
                auditoria.getId(),
                auditoria.getTipoOperacion(),
                auditoria.getFechaHoraExacta(),
                usuarioMapper.entidadADTO(auditoria.getUsuarioEjecutor()),
                auditoria.getNombreEntidadAfectada(),
                auditoria.getIdRegistroAfectado(),
                auditoria.getValoresAnteriores(),
                auditoria.getValoresNuevos()
        );
    }
}