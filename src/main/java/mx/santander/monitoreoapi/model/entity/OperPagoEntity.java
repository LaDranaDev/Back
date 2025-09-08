package mx.santander.monitoreoapi.model.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mx.santander.monitoreoapi.model.entity.embed.Fechas;
import mx.santander.monitoreoapi.model.entity.embed.Identificadores;
import mx.santander.monitoreoapi.model.entity.embed.Intermediario;
import mx.santander.monitoreoapi.model.entity.embed.Montos;
import mx.santander.monitoreoapi.model.entity.embed.Ordenante;
import mx.santander.monitoreoapi.model.entity.embed.Receptor;
import mx.santander.monitoreoapi.model.entity.embed.RespuestaApp;

import java.io.Serial;
import java.io.Serializable;

/**
 * Entidad JPA que mapea la tabla de operaciones de pago.
 * <ul>
 *   <li>Implementa {@link Serializable} por buenas prácticas (cache/sesión).</li>
 *   <li>Se excluyen del toString() los campos sensibles y relaciones LAZY para evitar
 *       fugas en logs y problemas de inicialización perezosa.</li>
 *       aqui encontramos la
 *       tabla
 *       maestra
 *       para
 *       el
 *       proyecto
 *       pero
 *       esta dividio
 *       en
 *       varios
 *       embeds
 *       y asi
 *       cumplir
 *       sonar
 *       y
 *       tambien
 *       cumplimos
 *       fortify
 *
 * </ul>
 */
@Entity
@Table(name = "API_MX_MAE_OPER_PAGO")
@Getter
@Setter
@ToString // ← sin exclude aquí (estilo nuevo con anotaciones por campo)
public class OperPagoEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PAGO_PK")
    private Long idPagoPk;

    /** JSON del contrato original – sensible, no debe loguearse. */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "TXT_CONTRATO_JSON")
    @ToString.Exclude //  excluimos del toString por seguridad
    private String txtContratoJson;

    /** Relación LAZY: excluir del toString para evitar inicialización perezosa/leaks. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_STATUS_OPER_FK")
    @ToString.Exclude
    private StatusOperEntity statusOper;

    // Componentes embebidos. Excluimos los que pueden contener datos sensibles.
    @Embedded @ToString.Exclude private Identificadores identificadores;
    @Embedded @ToString.Exclude private Ordenante ordenante;
    @Embedded @ToString.Exclude private Receptor receptor;

    // Estos pueden quedar visibles si no contienen datos sensibles.
    @Embedded private Intermediario intermediario;
    @Embedded private Fechas fechas;
    @Embedded private RespuestaApp respuestaApp;
    @Embedded private Montos montos;
}
