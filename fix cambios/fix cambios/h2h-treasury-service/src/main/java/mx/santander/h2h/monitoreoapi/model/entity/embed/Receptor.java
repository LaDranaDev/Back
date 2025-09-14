package mx.santander.h2h.monitoreoapi.model.entity.embed;

import java.io.Serial;
import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

/**
 * Componente embebido <Receptor>.
 * <p>Separa la entidad legacy en componentes de valor para reducir
 * complejidad y cumplir con Sonar (<=10 campos por clase principal).</p>
 * <p>Mapea 1:1 a columnas existentes; no cambia la tabla.</p>
 */
@Getter @Setter
@Embeddable
public class Receptor  implements Serializable {
    /*
    * en
    * esta clase
    * agregamos
    * el
    * mapeo
    * para
    * sonar
    * y la
    * regla
    * de 10 parametros*/

    @Serial
    private static final long serialVersionUID = 1L;
    @Column(name = "TXT_CTA_RECEP")     private String cuenta;
    @Column(name = "TXT_NOMBRE_RECEP")  private String nombre;
    @Column(name = "TXT_CTA_BANCO_REC") private String cuentaBanco;
    @Column(name = "TXT_NUM_CTA_REC")   private String numeroCuenta;
    @Column(name = "TXT_TIPO_CTA_REC")  private String tipoCuenta;
    @Column(name = "TXT_NOMBRE_BEN_REC")private String nombreBeneficiario;
    @Column(name = "TXT_CD_REC")        private String ciudad;
    @Column(name = "TXT_PAIS_REC")      private String pais;
    // dirección
    @Column(name = "TXT_CALLE_BEN_REC") private String calle;
    @Column(name = "TXT_CP_BEN_REC")    private String cp;
}
