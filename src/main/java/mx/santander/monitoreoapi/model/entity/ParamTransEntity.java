package mx.santander.monitoreoapi.model.entity;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Parametrización de tipos de operación (API_MX_PRC_CAT_PARAM_TRANS).
 * <p>
 * Mapea 1:1 la tabla operacional; por eso conserva varios campos.
 * Los metadatos de auditoría se agrupan en {@link Auditoria} para
 * reducir el número total de atributos expuestos.
 * </p>
 */
@Entity
@Table(name = "API_MX_PRC_CAT_PARAM_TRANS")
@Getter @Setter @ToString
public class ParamTransEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /** Identificador único del parámetro (PK). */
    @Id
    @Column(name = "ID_PARAM_PK", nullable = false)
    private Long idParam;

    /** Centro de costos del canal. */
    @Column(name = "TXT_CNTRO_COSTO")
    private Integer centroCosto;

    /** Tipo de pago (SPEI, SPID, INTERNACIONAL, B2B). */
    @Column(name = "ID_TIPO_PAGO")
    private String idTipoPago;

    /** Tipo de operación (ENTRE CUENTAS, TERCERO-TERCERO, etc.). */
    @Column(name = "ID_TIPO_OPER")
    private String idTipoOper;

    /** Clave de la empresa. */
    @Column(name = "TXT_EMPRESA")
    private String empresa;

    /** Clave del punto de venta. */
    @Column(name = "TXT_PNTO_VENTA")
    private String puntoVenta;

    /** Clave del medio de entrega. */
    @Column(name = "TXT_CLV_MED_ENTRE")
    private String claveMedioEntrega;

    /** Clave de transferencia. */
    @Column(name = "TXT_CLV_TRANS")
    private String claveTransferencia;

    /** Clave de operación. */
    @Column(name = "TXT_CLV_OPER")
    private String claveOperacion;

    /** Metadatos de auditoría (usuario y fechas). */
    @Embedded
    private Auditoria auditoria;
}
