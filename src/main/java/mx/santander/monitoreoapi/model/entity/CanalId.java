// CanalId.java
package mx.santander.monitoreoapi.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
/**
 * Clave primaria compuesta para {@link CanalEntity}.
 */
/**
 * Clave   para .
 */
/**
 * Clave compuesta para }.
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CanalId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /** BUC del cliente general. */
    /** BUC del cliente general. */
    /** BUC del cliente general. */
    /** BUC del cliente general. */
    /** BUC del cliente general. */
    @Column(name = "TXT_CANAL", nullable = false, length = 250)
    private String canal;
    /** BUC del cliente general. */
    /** BUC del cliente general. */
    /** BUC del cliente general. */
    /** BUC del cliente general. */

    @Column(name = "ID_TIPO_PAGO", nullable = false, length = 4)
    private String idTipoPago;

    @Column(name = "ID_TIPO_OPER", nullable = false, length = 2)
    private String idTipoOper;


}
