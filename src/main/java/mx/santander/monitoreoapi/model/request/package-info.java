/*
 * Proyecto: Monitoreo API
 * Archivo: PagoController.java
 * Descripción: Comentarios añadidos para documentar el propósito y funcionamiento del componente.
 * Autor: rrpm
 * Versión: 1.0
 * Fecha: 2025-09-02
 */

/**
 * Objetos de transferencia de datos (DTO) para peticiones entrantes al API.
 * <p>
 * Representan la estructura de los datos enviados por el cliente en los
 * endpoints del módulo de monitoreo de pagos.
 * Incluyen validaciones básicas mediante anotaciones y se usan en:
 * <ul>
 *   <li>Filtros de consulta de dashboard</li>
 *   <li>Parámetros de búsqueda de detalle paginado</li>
 *   <li>Solicitudes de cálculo de totales por divisa</li>
 * </ul>
 * </p>
 */
package mx.santander.monitoreoapi.model.request;
