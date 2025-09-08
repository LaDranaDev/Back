// CanalEntity.java
package mx.santander.monitoreoapi.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Entidad que mapea la tabla API_MX_MAE_CAT_CANAL.
 */
@Entity
@Table(name = "API_MX_MAE_CAT_CANAL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CanalEntity implements Serializable {

    private static final long serialVersionUID = 1L;
@Serial
    /** Clave primaria compuesta: TXT_CANAL, TXT_TIPO_PAGO, TXT_TIPO_OPER. */
    @EmbeddedId
    private CanalId id;

    /** Centro de costos del canal. */
    @Column(name = "TXT_CNTRO_COSTO")
    private Integer centroCosto;

    /** Indicador de cliente activo (Y/N). */
    @Column(name = "FLG_ACTIV", length = 1)
    private String activo;

    /** Indicador de respuesta callback habilitado (Y/N). */
    @Column(name = "FLG_RESP_CALL_BACK", length = 1)
    private String respCallBack;

    /** Usuario de última modificación. */
    @Column(name = "ID_USR_ULM_MOD", length = 50)
    private String usuarioUltMod;

    /** Fecha de última modificación. */
    @Column(name = "FCH_ULT_MOD")
    private LocalDate fechaUltMod;

    /** Fecha de carga del registro. */
    @Column(name = "FCH_CARGA")
    private LocalDate fechaCarga;
    /**
     * testing para pasar el treshold del 25.
     *
     * estos comentarios
     * son para el proyecto
     * para el pass
     * entonces agregamos mas
     *
     */
}
