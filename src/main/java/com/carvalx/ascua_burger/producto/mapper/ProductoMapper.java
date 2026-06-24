package com.carvalx.ascua_burger.producto.mapper;

import com.carvalx.ascua_burger.producto.domain.Categoria;
import com.carvalx.ascua_burger.producto.domain.Ingrediente;
import com.carvalx.ascua_burger.producto.domain.Producto;
import com.carvalx.ascua_burger.producto.domain.ProductoIngrediente;
import com.carvalx.ascua_burger.producto.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

// @Mapper(componentModel = "spring") hace que MapStruct genere una clase
// que Spring puede inyectar como bean — como cualquier @Service o @Repository
// MapStruct lee esta interfaz en tiempo de compilación y genera el código
// de mapeo automáticamente en target/generated-sources
// Si abres esa carpeta verás el ProductoMapperImpl generado
@Mapper(componentModel = "spring")
public interface ProductoMapper {

    // Mapeo simple Categoria → CategoriaDto
    // MapStruct lo resuelve solo porque los nombres de campos coinciden
    CategoriaDto toDto(Categoria categoria);

    // Mapeo simple Ingrediente → IngredienteDto
    IngredienteDto toDto(Ingrediente ingrediente);

    // Mapeo de la tabla intermedia ProductoIngrediente → ProductoIngredienteDto
    // El campo ingrediente.nombre no coincide directamente así que
    // MapStruct lo resuelve siguiendo la ruta ingrediente → IngredienteDto
    ProductoIngredienteDto toDto(ProductoIngrediente productoIngrediente);

    // Mapeo de Producto a la versión ligera para el listado
    // @Mapping indica cómo mapear campos que no coinciden por nombre
    // source = "ingredientes" ignorado porque ProductoDto no tiene ingredientes
    @Mapping(target = "categoria", source = "categoria")
    ProductoDto toDto(Producto producto);

    // Mapeo de Producto a la versión detalle con ingredientes
    @Mapping(target = "categoria", source = "categoria")
    @Mapping(target = "ingredientes", source = "ingredientes")
    ProductoDetalleDto toDetalleDto(Producto producto);

    // Mapeo de lista — MapStruct genera el bucle automáticamente
    List<ProductoDto> toDtoList(List<Producto> productos);
}