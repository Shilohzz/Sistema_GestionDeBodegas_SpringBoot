package com.logitrack.logitrack.mapper;

import com.logitrack.logitrack.DTO.request.BodegaRequestDTO;
import com.logitrack.logitrack.DTO.response.BodegaResponseDTO;
import com.logitrack.logitrack.model.Bodega;
import com.logitrack.logitrack.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class BodegaMapper {

    // Inyección por constructor
    private final UsuarioMapper usuarioMapper;

    // Le pido que me traiga el usuarioMapper porque lo voy a necesitar para los siguientes procesos.
    public BodegaMapper(UsuarioMapper usuarioMapper) {
        this.usuarioMapper = usuarioMapper;
    }

    // Aquí obtengo la información de la bodega
    public BodegaResponseDTO entidadADTO(Bodega bodega) {
        if (bodega == null) return null;
        return new BodegaResponseDTO(
                bodega.getId(),
                bodega.getNombreComercial(),
                bodega.getCiudadUbicacion(),
                bodega.getDireccionExacta(),
                bodega.getCapacidadMaximaUnidades(),
                usuarioMapper.entidadADTO(bodega.getEncargado()), // Como necesito devolver un ResponseDTO, primero convierto al usuario en un DTO usando su mismo Mapper.
                bodega.getEstaActiva()
        );
    }

    // Creación de bodega
    public Bodega DTOAEntidad(BodegaRequestDTO dto, Usuario encargado) {
        if (dto == null || encargado == null) return null;
        Bodega bodega = new Bodega();
        bodega.setNombreComercial(dto.nombreComercial());
        bodega.setCiudadUbicacion(dto.ciudadUbicacion());
        bodega.setDireccionExacta(dto.direccionExacta());
        bodega.setCapacidadMaximaUnidades(dto.capacidadMaximaUnidades());
        bodega.setEncargado(encargado);
        return bodega;
    }

    // Actualización de bodega
    public void actualizarEntidadDesdeDTO(Bodega bodega, BodegaRequestDTO dto, Usuario encargado) {
        if (bodega == null || dto == null) return;
        bodega.setNombreComercial(dto.nombreComercial());
        bodega.setCiudadUbicacion(dto.ciudadUbicacion());
        bodega.setDireccionExacta(dto.direccionExacta());
        bodega.setCapacidadMaximaUnidades(dto.capacidadMaximaUnidades());
        bodega.setEncargado(encargado);
    }
}