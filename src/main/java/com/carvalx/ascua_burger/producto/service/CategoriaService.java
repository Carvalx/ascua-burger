package com.carvalx.ascua_burger.producto.service;

import com.carvalx.ascua_burger.producto.domain.Categoria;
import com.carvalx.ascua_burger.producto.dto.CategoriaDto;
import com.carvalx.ascua_burger.producto.mapper.ProductoMapper;
import com.carvalx.ascua_burger.producto.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProductoMapper productoMapper;

    // @Transactional(readOnly = true) le dice a Spring que esta operación
    // solo lee datos — Hibernate no rastrea cambios en las entidades
    // Esto mejora el rendimiento porque evita el dirty checking
    @Transactional(readOnly = true)
    public List<CategoriaDto> listarTodas() {
        return categoriaRepository.findAll()
                .stream()
                .map(productoMapper::toDto)
                .toList();
    }

    @Transactional
    public CategoriaDto crear(String nombre) {
        if (categoriaRepository.existsByNombre(nombre)) {
            throw new IllegalArgumentException("Ya existe una categoría con ese nombre");
        }

        Categoria categoria = Categoria.builder()
                .nombre(nombre)
                .build();

        return productoMapper.toDto(categoriaRepository.save(categoria));
    }
}