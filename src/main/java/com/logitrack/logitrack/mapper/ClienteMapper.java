package com.logitrack.logitrack.mapper;

import com.logitrack.logitrack.DTO.request.ClienteRequestDTO;
import com.logitrack.logitrack.DTO.response.ClienteResponseDTO;
import com.logitrack.logitrack.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    // TRADUCCIÓN DE ENTIDAD A DTO
    // OBTECIÓN DE DATOS
    public ClienteResponseDTO entidadADTO(Cliente cliente) {
        if (cliente == null) return null;
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNombreCompleto(),
                cliente.getTipo(),
                cliente.getNumeroDocumento(),
                cliente.getEmailContacto(),
                cliente.getTelefonoContacto(),
                cliente.getDireccionPrincipal(),
                cliente.getEstaActivo()
        );
    }

    // TRADUCCIÓN DE DTO A ENTIDAD
    // CREACIÓN
    public Cliente DTOAEntidad(ClienteRequestDTO dto) {
        if (dto == null) return null;
        Cliente cliente = new Cliente();
        cliente.setNombreCompleto(dto.nombreCompleto());
        cliente.setTipo(dto.tipo());
        cliente.setNumeroDocumento(dto.numeroDocumento());
        cliente.setEmailContacto(dto.emailContacto());
        cliente.setTelefonoContacto(dto.telefonoContacto());
        cliente.setDireccionPrincipal(dto.direccionPrincipal());
        return cliente;
    }

    // ACTUALIZACIÓN
    public void actualizarEntidadDesdeDTO(Cliente cliente, ClienteRequestDTO dto) {
        if (cliente == null || dto == null) return;
        cliente.setNombreCompleto(dto.nombreCompleto());
        cliente.setTipo(dto.tipo());
        cliente.setNumeroDocumento(dto.numeroDocumento());
        cliente.setEmailContacto(dto.emailContacto());
        cliente.setTelefonoContacto(dto.telefonoContacto());
        cliente.setDireccionPrincipal(dto.direccionPrincipal());
    }
}