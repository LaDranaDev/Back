package mx.santander.monitoreoapi.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * Catálogo
 *  de
 *   tipos
 *    de
 *     pago
 *      (SPEI,
 *       SPID,
 *        INTERNACIONAL,
 *         etc.).
 * Entidad
 *  JPA
 *   mapeada
 *    a la tabla API_MX_PRC_CAT_TIPO_PAGO.
 *
 */
@Entity
@Table(name = "API_MX_PRC_CAT_TIPO_PAGO")
@Getter
@Setter
public class TipoPagoEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID_TIPO_PAGO", nullable = false, length = 10)
    private String idTipoPago;

    @Column(name = "TXT_TIPO_PAGO", nullable = false, length = 100)
    private String txtTipoPago;
}
