package mx.santander.monitoreoapi.model.entity;

import java.io.Serial;
import java.io.Serializable;
/**
 * Catálogo
 *  de
 * tipos
 * de
 * operación 
 * (API_
 * MX_
 * PRC_
 * CAT
 * _TIPO
 * _OPER).
 */

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Catálogo
 *  de
 *  tipos
 *  de
 *  operación (API_MX_PRC_CAT_TIPO_OPER).
 */
@Entity
@Table(name = "API_MX_PRC_CAT_TIPO_OPER")
@Getter
@Setter
public class TipoOperacionEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Identificador del tipo de operación (PK). */
    @Id
    @Column(name = "ID_TIPO_OPER")
    private String idTipoOper;

    /** Descripción del tipo de operación. */
    @Column(name = "TXT_TIPO_OPER")
    private String txtTipoOper;
}
