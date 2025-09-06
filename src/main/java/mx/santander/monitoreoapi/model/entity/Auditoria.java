package mx.santander.monitoreoapi.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Metadatos de auditoría de la fila (usuario y fechas).
 * Se extrae para reducir el número de fields en la entidad principal.
 */
@Embeddable
@Getter @Setter @ToString
public class Auditoria implements Serializable {
    @Serial private static final long serialVersionUID = 1L;

    /** Usuario de última modificación. */
    @Column(name = "ID_USR_ULM_MOD")
    private String usuarioUltimaMod;
/**
 * Metadatos de auditoría de la fila (usuario y fechas).
 * Se extrae para reducir el número de fields en la entidad principal.
 */
    /** Fecha de última modificación. */
    @Column(name = "FCH_ULT_MOD")
    private LocalDate fechaUltMod;
/**
 * Metadatos de auditoría de la fila (usuario y fechas).
 * Se extrae para reducir el número de fields en la entidad principal.
 */
    /** Fecha de carga del registro. */
    @Column(name = "FCH_CARGA")
    private LocalDate fechaCarga;
}
