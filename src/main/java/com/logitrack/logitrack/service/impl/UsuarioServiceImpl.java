package com.logitrack.logitrack.service.impl;

import com.logitrack.logitrack.DTO.request.UsuarioRequestDTO;
import com.logitrack.logitrack.DTO.response.UsuarioResponseDTO;
import com.logitrack.logitrack.mapper.UsuarioMapper;
import com.logitrack.logitrack.model.Usuario;
import com.logitrack.logitrack.repository.UsuarioRepository;
import com.logitrack.logitrack.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder; // Importación necesaria
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UsuarioResponseDTO guardar(UsuarioRequestDTO dto) {
        // Encripto la contrasenha
        String passwordEncriptada = passwordEncoder.encode(dto.contrasena());

        Usuario usuario = usuarioMapper.DTOAEntidad(dto, passwordEncriptada);
        Usuario guardado = usuarioRepository.save(usuario);
        return usuarioMapper.entidadADTO(guardado);
    }

    @Override
    public UsuarioResponseDTO actualizar(Long id, UsuarioRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));

        usuario.setNombreCompleto(dto.nombreCompleto());
        usuario.setEmailInstitucional(dto.emailInstitucional());

        // encripto la contrasenha
        usuario.setContrasenaHash(passwordEncoder.encode(dto.contrasena()));

        usuario.setRol(dto.rol());
        Usuario actualizado = usuarioRepository.save(usuario);
        return usuarioMapper.entidadADTO(actualizado);
    }

    @Override
    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));
        return usuarioMapper.entidadADTO(usuario);
    }

    @Override
    public List<UsuarioResponseDTO> buscarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::entidadADTO)
                .toList();
    }

    @Override
    public void eliminar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + id));
        usuarioRepository.delete(usuario);
    }
}