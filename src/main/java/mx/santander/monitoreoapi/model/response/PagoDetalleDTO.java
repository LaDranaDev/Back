package mx.santander.monitoreoapi.model.response;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO inmutable para el detalle de operaciones.
 * <p>Se utiliza como proyección en JPQL:</p>
 * {@code select new mx.santander.monitoreoapi.model.response.PagoDetalleDTO(...)}.
 */

/*cumplimos
* la
* regla
* de
* sonar*/
public record PagoDetalleDTO(
        String operacion,       // p.idTipoOper
        String cuentaAbono,     // p.txtCtaRecep
        BigDecimal importe,     // p.imp
        String referenciaCanal, // p.idRefeUnico
        String divisa,          // p.idDiv
        LocalDateTime fechaCarga, // p.fchCarga
        String cuentaCargo,     // p.txtCtaOrden
        String tipoPago,        // p.idTipoPago
        String estatus,         // p.statusOper.descripcion
        Integer transactionId   // p.numRefer
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
