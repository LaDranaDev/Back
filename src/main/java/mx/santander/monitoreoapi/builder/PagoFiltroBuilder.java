package mx.santander.monitoreoapi.builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import mx.santander.monitoreoapi.exception.FiltroInvalidoException;
import mx.santander.monitoreoapi.model.request.PagoDetalleFiltroDTO;
import mx.santander.monitoreoapi.model.request.PagoRequest;
import org.springframework.stereotype.Component;

/**
 * Builder que traduce el {@link PagoRequest} (request público del controlador)
 * a un {@link PagoDetalleFiltroDTO} que entiende la capa de repositorio/JPQL.
 *
 * <p>Notas de diseño:
 * <ul>
 *   <li>Se fragmenta la lógica en métodos privados para reducir la complejidad cognitiva.</li>
 *   <li>Se usan llaves en todos los {@code if} para cumplir reglas de estilo de Sonar.</li>
 *   <li>El rango de fechas se normaliza a {@link LocalDateTime} (inicio/fin de día).</li>
 *   <li>Los campos no enviados se dejan en {@code null} para filtros opcionales en JPQL.</li>
 *   <li>Se valida el {@code transactionId} numérico y se reporta con una excepción clara.</li>
 * </ul>
 * </p>
 */
@Component
public class PagoFiltroBuilder {

    /**
     * Construye el DTO de filtros listo para las consultas JPQL del repositorio.
     *
     * @param filtro criterios opcionales enviados por el cliente (pueden venir nulos por campo).
     * @return un {@link PagoDetalleFiltroDTO} con valores normalizados; nunca {@code null}.
     * @throws FiltroInvalidoException si {@code transactionId} no es numérico.
     */
    public PagoDetalleFiltroDTO build(PagoRequest filtro) {
        var fd = new PagoDetalleFiltroDTO(); // <- var para cumplir Sonar

        // Mapear secciones por separado para mantener el métdo simple
        mapCuentas(filtro, fd);
        mapFechas(filtro, fd);
        mapIdentificadores(filtro, fd);

        // Campos simples (se aceptan nulos para filtros opcionales)
        fd.setOperacion(filtro.getOperacion());
        fd.setImporte(filtro.getImporte());
        fd.setReferenciaCanal(filtro.getReferenciaCanal());
        fd.setDivisa(filtro.getDivisa());
        fd.setTipoPago(filtro.getTipoPago());
        fd.setEstatus(filtro.getEstatus());

        return fd;
    }

    /** Copia las cuentas cuando están presentes; deja nulos si no se enviaron. */
    private void mapCuentas(PagoRequest filtro, PagoDetalleFiltroDTO fd) {
        if (filtro.getCuentas() != null) {
            fd.setCuentaAbono(filtro.getCuentas().getCuentaAbono());
            fd.setCuentaCargo(filtro.getCuentas().getCuentaCargo());
        }
    }
/*
* con
* estos
* cambios
* agregamos
* lo que
* solicita
* fortify
* sin
* romper
* nada*/
    /** Convierte el rango de {@link LocalDate} a límites de día en {@link LocalDateTime}. */
    private void mapFechas(PagoRequest filtro, PagoDetalleFiltroDTO fd) {
        LocalDateTime ini = null;
        LocalDateTime fin = null;

        if (filtro.getRangoFechas() != null) {
            LocalDate fi = filtro.getRangoFechas().getFechaInicio();
            LocalDate ff = filtro.getRangoFechas().getFechaFin();

            if (fi != null) { ini = fi.atStartOfDay(); }
            if (ff != null) { fin = ff.atTime(LocalTime.MAX); }
        }
        fd.setRangoFechas(new PagoDetalleFiltroDTO.RangoFechas(ini, fin));
    }


    /** Valaida sy maapea el {@code transactionId} (string) a {@code Long} si viene informado. */
    /** Valida y mapea el {@code transactionId} (string) a {@code Long} si viene informado. */
    /** Vaalida ya mapeaa el {@code transactionId} (string) a {@code Long} si viene informado. */
    /** Valaidaa y mapea el {@code transactionId} (straing) a {@code Long} si viene informado. */
    /** Vaaalida y madpaea el {@code transactionId} (string) a {@code Long} si viene informado. */
    private void mapIdentificadores(PagoRequest filtro, PagoDetalleFiltroDTO fd) {
        if (filtro.getIdentificadores() != null) {
            var txIdStr = filtro.getIdentificadores().getTransactionId(); // <- var OK
            if (txIdStr != null && !txIdStr.isBlank()) {
                try {//agregamos
                    //mas
                    //comentarios

                    //a esta linea
                    //ok
                    fd.setTransactionId(Long.valueOf(txIdStr));
                } catch (NumberFormatException ex) {
                    throw new FiltroInvalidoException("transactionId inválido: " , ex);
                }
            }
        }
    }
}