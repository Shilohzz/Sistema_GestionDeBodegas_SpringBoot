package com.logitrack.logitrack.service.impl;

import com.logitrack.logitrack.DTO.request.MovimientoDetalleRequestDTO;
import com.logitrack.logitrack.DTO.request.MovimientoRequestDTO;
import com.logitrack.logitrack.DTO.response.MovimientoResponseDTO;
import com.logitrack.logitrack.exception.BusinessRuleException;
import com.logitrack.logitrack.mapper.MovimientoMapper;
import com.logitrack.logitrack.model.*;
import com.logitrack.logitrack.repository.*;
import com.logitrack.logitrack.service.MovimientoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final MovimientoDetalleRepository movimientoDetalleRepository;
    private final InventarioRepository inventarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final BodegaRepository bodegaRepository;
    private final ProductoRepository productoRepository;
    private final MovimientoMapper movimientoMapper;

    @Override
    @Transactional
    // Tengo como parámetro el email de un usuario para saber quién realiza el registro de movimiento
    public MovimientoResponseDTO registrar(MovimientoRequestDTO dto, String emailUsuario) {

        // Busco ese usuario por el email
        Usuario usuario = usuarioRepository.findByEmailInstitucional(emailUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + emailUsuario));

        // Defino este método como lo haría en un contrato de interface, y más abajo le estructuro el proceso de validación.
        // Tuve que definirlo aquí y no en la interface, ya que es algo que no aplicaré en ninguna otra clase, solo en esta.
        validarReglasDenegocio(dto);

        // Declaro estos dos objetos porque más adelante los necesitaré para buscar las bodegas
        Bodega bodegaOrigen = null;
        Bodega bodegaDestino = null;

        // Dependiendo del tipo de movimiento se activan estos dos IF.
        // No lanza un error ya que estos campos no son @NotBlank.
        // Porque, una entrada no viene de ninguna de nuestras bodegas y una salida no tiene como destino una de nuestras bodegas (de la empresa)
        if (dto.bodegaOrigenId() != null) {
            bodegaOrigen = bodegaRepository.findById(dto.bodegaOrigenId())
                    .orElseThrow(() -> new EntityNotFoundException("Bodega origen no encontrada con id: " + dto.bodegaOrigenId()));
        }
        if (dto.bodegaDestinoId() != null) {
            bodegaDestino = bodegaRepository.findById(dto.bodegaDestinoId())
                    .orElseThrow(() -> new EntityNotFoundException("Bodega destino no encontrada con id: " + dto.bodegaDestinoId()));
        }

        // Creo el movimiento y lo guardo
        Movimiento movimiento = new Movimiento();
        movimiento.setTipo(dto.tipo());
        movimiento.setFechaHora(LocalDateTime.now());
        movimiento.setUsuarioResponsable(usuario);
        movimiento.setBodegaOrigen(bodegaOrigen);
        movimiento.setBodegaDestino(bodegaDestino);
        movimiento.setObservacion(dto.observacion());
        Movimiento guardado = movimientoRepository.save(movimiento);

        // itero sobre cada producto del detalle y lo proceso
        List<MovimientoDetalle>  detalles = new ArrayList<>();
        for (MovimientoDetalleRequestDTO detalleDTO : dto.detalles()) {
            Producto producto = productoRepository.findById(detalleDTO.productoId())
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + detalleDTO.productoId()));

            // Por cada detalle busco el producto completo en la bd usando el id y lo actualizo
            actualizarInventario(dto.tipo(), bodegaOrigen, bodegaDestino, producto, detalleDTO.cantidadUnidades());

            // Aquí guardo el detalle del movimiento
            MovimientoDetalle detalle = new MovimientoDetalle();
            detalle.setMovimiento(guardado);
            detalle.setProducto(producto);
            detalle.setCantidadUnidades(detalleDTO.cantidadUnidades());
            detalles.add(movimientoDetalleRepository.save(detalle));
        }

        // Devuelvo el movimiento completo con sus detalles
        return movimientoMapper.entidadADTO(guardado, detalles);
    }

    @Override
    public MovimientoResponseDTO buscarPorId(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movimiento no encontrado con id: " + id));
        List<MovimientoDetalle> detalles = movimientoDetalleRepository.findByMovimientoId(id);
        return movimientoMapper.entidadADTO(movimiento, detalles);
    }

    @Override
    public List<MovimientoResponseDTO> buscarTodos() {
        return movimientoRepository.findAll()
                .stream()
                .map(m -> movimientoMapper.entidadADTO(m, movimientoDetalleRepository.findByMovimientoId(m.getId())))
                .toList();
    }

    @Override
    public List<MovimientoResponseDTO> buscarPorRangoDeFechas(LocalDateTime inicio, LocalDateTime fin) {
        return movimientoRepository.findByFechaHoraBetween(inicio, fin)
                .stream()
                .map(m -> movimientoMapper.entidadADTO(m, movimientoDetalleRepository.findByMovimientoId(m.getId())))
                .toList();
    }


    private void validarReglasDenegocio(MovimientoRequestDTO dto) {
        switch (dto.tipo()) {
            case "ENTRADA" -> {
                if (dto.bodegaDestinoId() == null)
                    throw new BusinessRuleException("Una ENTRADA debe tener bodega destino.");
                if (dto.bodegaOrigenId() != null)
                    throw new BusinessRuleException("Una ENTRADA no puede tener bodega origen.");
            }
            case "SALIDA" -> {
                if (dto.bodegaOrigenId() == null)
                    throw new BusinessRuleException("Una SALIDA debe tener bodega origen.");
                if (dto.bodegaDestinoId() != null)
                    throw new BusinessRuleException("Una SALIDA no puede tener bodega destino.");
            }
            case "TRANSFERENCIA" -> {
                if (dto.bodegaOrigenId() == null || dto.bodegaDestinoId() == null)
                    throw new BusinessRuleException("Una TRANSFERENCIA debe tener bodega origen y destino.");
            }
            default -> throw new BusinessRuleException("Tipo de movimiento inválido: " + dto.tipo());
        }
    }

    private void actualizarInventario(String tipo, Bodega origen, Bodega destino,
                                      Producto producto, Integer cantidad) {
        switch (tipo) {
            case "ENTRADA" -> {
                // Le suma stock en la bodega destino
                Inventario inv = obtenerOCrearInventario(destino, producto);
                inv.setStockActual(inv.getStockActual() + cantidad);
                inventarioRepository.save(inv);
            }
            case "SALIDA" -> {
                // Le resta stock en la bodega origen
                Inventario inv = obtenerOCrearInventario(origen, producto);
                if (inv.getStockActual() < cantidad)
                    throw new BusinessRuleException("Stock insuficiente para el producto id: " + producto.getId());
                inv.setStockActual(inv.getStockActual() - cantidad);
                inventarioRepository.save(inv);
            }
            case "TRANSFERENCIA" -> {
                // Le resta en origen y suma en destino
                Inventario invOrigen = obtenerOCrearInventario(origen, producto);
                if (invOrigen.getStockActual() < cantidad)
                    throw new BusinessRuleException("Stock insuficiente para el producto id: " + producto.getId());
                invOrigen.setStockActual(invOrigen.getStockActual() - cantidad);
                inventarioRepository.save(invOrigen);

                Inventario invDestino = obtenerOCrearInventario(destino, producto);
                invDestino.setStockActual(invDestino.getStockActual() + cantidad);
                inventarioRepository.save(invDestino);
            }
        }
    }

    private Inventario obtenerOCrearInventario(Bodega bodega, Producto producto) {
        return inventarioRepository
                .findByBodegaIdAndProductoId(bodega.getId(), producto.getId())
                .orElseGet(() -> {
                    Inventario nuevo = new Inventario();
                    nuevo.setBodega(bodega);
                    nuevo.setProducto(producto);
                    nuevo.setStockActual(0);
                    return nuevo;
                });
    }
}