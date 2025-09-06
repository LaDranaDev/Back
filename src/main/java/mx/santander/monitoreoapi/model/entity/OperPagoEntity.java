package mx.santander.monitoreoapi.model.entity;


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
import jakarta.persistence.Table;import lombok.Getter;
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
 *
 * <p>Notas:
 * <ul>
 *   <li>Implementa {@link Serializable} para cumplir con prácticas de beans/entidades
 *       (cache, sesión HTTP, etc.).</li>
 *   <li>La clase mantiene un mapeo 1:1 con la tabla LEGACY; por ello tiene
 *       más de 10 campos (ver supresión justificada).</li>
 * </ul>
 * </p>
 */
@Entity
@Table(name = "API_MX_MAE_OPER_PAGO")
@Getter @Setter
@ToString(exclude = "txtContratoJson")
public class OperPagoEntity  implements  Serializable{
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PAGO_PK")
    private Long idPagoPk;

    /*
    * se cambiaron a emmbebidos para cumplir la regla de sonar de no tener mas de 10 atributos p
    * ya que originalmente eran 68 pero con esto nos alineamos
    * a sonar
    * y los
    * alieneamientos
    * de el banco
    * ok
    * ok
    * */
    @Lob
    @Column(name = "TXT_CONTRATO_JSON")
    private String txtContratoJson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_STATUS_OPER_FK")
    private StatusOperEntity statusOper;

    // 6–7 componentes embebidos
    @Embedded
    private Identificadores identificadores;
    @Embedded private Ordenante ordenante;
    @Embedded private Receptor receptor;
    @Embedded private Intermediario intermediario;
    @Embedded private Fechas fechas;
    @Embedded private RespuestaApp respuestaApp;
    @Embedded private Montos montos;
}