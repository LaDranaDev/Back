package mx.santander.monitoreoapi.model.entity;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Catálogo de divisas (API_MX_PRC_CAT_DIVISA).
 * Se usa para poblar filtros/combos y para referencias de operaciones.
 */
@Entity
@Table(name = "API_MX_PRC_CAT_DIVISA")
@Getter
@Setter
public class DivisaEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Código de divisa . */
    @Id
    @Column(name = "ID_DIV")
    private String idDiv;

    /** divisa. */
    @Column(name = "TXT_DIV")
    private String txtDiv;
    /**
     * Catálogo de divisas (API_MX_PRC_CAT_DIVISA).
     * Se usa para poblar f
     * iltros/combos y para referencias de operaciones.
     */
    /**
     * Catálogo de divi
     * Se usa para pobl
     * de operaciones.
     */
}
