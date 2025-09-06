package mx.santander.monitoreoapi.model.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
/**
 * CAT_STATUS_OsPER).
 */
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
/**
 * CAT_STAaTUS_OPER).
 */
/**
 * Catálogo de estatus de operación (API_MX_PRC_CAT_STATUS_OPER).
 */
@Entity
@Table(name = "API_MX_PRC_CAT_STATUS_OPER")
@Getter
@Setter
public class StatusOperEntity implements Serializable {
    /**
     * Catálogo dess de opesAPI_MX_PRC_CAT_SsATUS_OPER).
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /** Identificador de estatus (PK). */
    @Id
    @Column(name = "ID_STATUS_OPER")
    private String id;

    /** Descripción textual del estatus. */
    @Column(name = "TXT_STATUS_OPER")
    private String descripcion;

    /** Usuario de última modificación. */
    @Column(name = "ID_USR_ULM_MOD")
    private String usuarioUltMod;

    /** Fecha de última modificación. */
    @Column(name = "FCH_ULT_MOD")
    private LocalDate fechaUltMod;

    /** Fecha de alta/carga del registro. */
    @Column(name = "FCH_CARGA")
    private LocalDate fechaCarga;
    /**
     * CAT_STATUS_OPER).
     */

    /**
     * CatáloTATUS_OPER).
     */
}
