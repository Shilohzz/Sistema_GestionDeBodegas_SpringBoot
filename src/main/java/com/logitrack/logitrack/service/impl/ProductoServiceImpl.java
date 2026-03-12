package com.logitrack.logitrack.service.impl;

import com.logitrack.logitrack.DTO.request.ProductoRequestDTO;
import com.logitrack.logitrack.DTO.response.ProductoResponseDTO;
import com.logitrack.logitrack.mapper.ProductoMapper;
import com.logitrack.logitrack.model.Categoria;
import com.logitrack.logitrack.model.Cliente;
import com.logitrack.logitrack.model.Producto;
import com.logitrack.logitrack.repository.CategoriaRepository;
import com.logitrack.logitrack.repository.ClienteRepository;
import com.logitrack.logitrack.repository.ProductoRepository;
import com.logitrack.logitrack.service.ProductoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoMapper productoMapper;

    @Override
    public ProductoResponseDTO guardar(ProductoRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con id: " + dto.categoriaId()));
        Cliente cliente = clienteRepository.findById(dto.clientePropietarioId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + dto.clientePropietarioId()));
        Producto producto = productoMapper.DTOAEntidad(dto, categoria, cliente);
        Producto guardado = productoRepository.save(producto);
        return productoMapper.entidadADTO(guardado);
    }

    @Override
    public ProductoResponseDTO actualizar(Long id, ProductoRequestDTO dto) {
        Producto producto = productoRepository.findById(id) // busco el producto a actualizar
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + id));
        Categoria categoria = categoriaRepository.findById(dto.categoriaId()) // el DTO me trae la categoría que estoy buscando (categoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con id: " + dto.categoriaId()));
        Cliente cliente = clienteRepository.findById(dto.clientePropietarioId()) // El dto me trae el cliente relacionado con el producto (clienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + dto.clientePropietarioId()));
        productoMapper.actualizarEntidadDesdeDTO(producto, dto, categoria, cliente);// Aquí el mapper me sobreescribe los campos del objeto.
        Producto actualizado = productoRepository.save(producto); // Aquí le digo al repository que me guarde el producto actualizado en BD
        return productoMapper.entidadADTO(actualizado); // Aquí convierto la entidad en DTO para poderlo devolver
    }

    @Override
    public ProductoResponseDTO buscarPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + id));
        return productoMapper.entidadADTO(producto);
    }

    @Override
    public List<ProductoResponseDTO> buscarTodos() {
        return productoRepository.findAll()
                .stream()
                .map(productoMapper::entidadADTO)
                .toList();
    }

    @Override
    public void eliminar(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + id));
        productoRepository.delete(producto);
    }
}