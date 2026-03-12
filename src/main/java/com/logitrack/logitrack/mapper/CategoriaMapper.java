package com.logitrack.logitrack.mapper;

import com.logitrack.logitrack.DTO.request.CategoriaRequestDTO;
import com.logitrack.logitrack.DTO.response.CategoriaResponseDTO;
import com.logitrack.logitrack.model.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {

    // TRADUCCIÓN DE ENTIDAD A DTO
    // OBTECIÓN DE DATOS
    public CategoriaResponseDTO entidadADTO(Categoria categoria){
        if (categoria == null) return null;
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion()
        );
    }

    // TRADUCCIÓN DE DTO A ENTIDAD
    // CREACIÓN
    public Categoria DTOAEntidad(CategoriaRequestDTO dto){
        if(dto == null) return null;
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.nombre());
        categoria.setDescripcion(dto.descripcion());
        return categoria;
    }

    // ACTUALIZAR
    public void actualizarEntidadDesdeDTO(Categoria categoria, CategoriaRequestDTO dto) {
        if (categoria == null || dto == null) return;
        categoria.setNombre(dto.nombre());
        categoria.setDescripcion(dto.descripcion());
    }
}
