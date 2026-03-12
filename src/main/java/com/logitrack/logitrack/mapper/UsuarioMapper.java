package com.logitrack.logitrack.mapper;

import com.logitrack.logitrack.DTO.request.UsuarioRequestDTO;
import com.logitrack.logitrack.DTO.response.UsuarioResponseDTO;
import com.logitrack.logitrack.model.Usuario;
import org.springframework.stereotype.Component;


@Component
public class UsuarioMapper {

    public UsuarioResponseDTO entidadADTO(Usuario usuario) {
        if (usuario == null) return null;
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombreCompleto(),
                usuario.getEmailInstitucional(),
                usuario.getRol(),
                usuario.getEstaActivo()
        );
    }

    /* Aquí le paso un segundo parámetro para la contraseña
    *  Ya que quiero que la contraseña quede encriptada en la base de datos (por seguridad)
    *  Entonces, al registrar un usuario nuevo el DTO recibe todos los datos de la persona junto a la contraseña,
    *  Pero, no los guarda de inmediato en la base de datos.
    *  Primero se los pasa al Service quien ENCRIPTA la contraseña recibida y se la devuelve en el parámetro al mapper.
    *  De esta manera, el mapper procede a guardar esa contraseña en la base de datos.
    *  FLUJO: DTO -> SERVICE -> MAPPER -> BD.
    * */
    public Usuario DTOAEntidad(UsuarioRequestDTO dto, String contrasenaEncriptada) {
        if (dto == null) return null;
        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(dto.nombreCompleto());
        usuario.setEmailInstitucional(dto.emailInstitucional());
        usuario.setContrasenaHash(contrasenaEncriptada);
        usuario.setRol(dto.rol());
        return usuario;
    }
}