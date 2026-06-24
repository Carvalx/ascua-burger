package com.carvalx.ascua_burger.ia.service;

import com.carvalx.ascua_burger.ia.client.GroqApiClient;
import com.carvalx.ascua_burger.ia.dto.PreferenciasDto;
import com.carvalx.ascua_burger.ia.dto.RecomendacionDto;
import com.carvalx.ascua_burger.ia.dto.RespuestaRecomendacionDto;
import com.carvalx.ascua_burger.producto.domain.Producto;
import com.carvalx.ascua_burger.producto.repository.ProductoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecomendadorService {

    private final GroqApiClient groqApiClient;
    private final ProductoRepository productoRepository;
    private final ObjectMapper objectMapper;

    // @Cacheable: si dos usuarios piden lo mismo, devuelve el resultado
    // cacheado sin llamar a Groq — ahorra cuota y mejora rendimiento
    // La clave del cache es el hash de los tres parámetros
    @Cacheable(value = "recomendaciones",
            key = "#dto.presupuesto + '-' + #dto.comensales + '-' + #dto.preferencias")
    public RespuestaRecomendacionDto recomendar(PreferenciasDto dto) {

        // Cogemos los productos disponibles para incluirlos en el prompt
        List<Producto> productos = productoRepository
                .findByDisponibleTrue(PageRequest.of(0, 20))
                .getContent();

        String catalogoStr = productos.stream()
                .map(p -> String.format("- %s (%.2f€): %s",
                        p.getNombre(), p.getPrecioBase(), p.getDescripcion()))
                .collect(Collectors.joining("\n"));

        String prompt = String.format("""
                Catálogo de Ascua Burger:
                %s
                
                Cliente busca:
                - Presupuesto: %.2f€ para %d personas
                - Preferencias: %s
                
                Recomienda 2-3 hamburguesas. Responde SOLO con este JSON:
                {
                  "recomendaciones": [
                    {
                      "nombre": "nombre exacto del producto",
                      "descripcion": "descripcion del producto",
                      "precio": precio_numerico,
                      "razon": "por qué encaja con el cliente"
                    }
                  ],
                  "mensaje": "mensaje personalizado breve"
                }
                """,
                catalogoStr,
                dto.getPresupuesto(),
                dto.getComensales(),
                dto.getPreferencias() != null ? dto.getPreferencias() : "sin preferencias especiales"
        );

        try {
            String respuestaJson = groqApiClient.completar(prompt);
            log.debug("Respuesta Groq: {}", respuestaJson);

            // Limpiamos por si Groq añade backticks o texto extra
            respuestaJson = respuestaJson
                    .replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();

            RespuestaRecomendacionDto respuesta = objectMapper.readValue(
                    respuestaJson, RespuestaRecomendacionDto.class);

            // Enriquecemos con los IDs reales de los productos
            respuesta.getRecomendaciones().forEach(rec -> {
                productos.stream()
                        .filter(p -> p.getNombre().equalsIgnoreCase(rec.getNombre()))
                        .findFirst()
                        .ifPresent(p -> rec.setProductoId(p.getId()));
            });

            return respuesta;

        } catch (Exception e) {
            log.error("Error al procesar respuesta de IA: {}", e.getMessage());
            RespuestaRecomendacionDto fallback = new RespuestaRecomendacionDto();
            fallback.setMensaje("No pudimos generar recomendaciones en este momento. Por favor inténtalo de nuevo.");
            fallback.setRecomendaciones(List.of());
            return fallback;
        }
    }
}