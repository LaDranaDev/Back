package mx.santander.h2h.monitoreoapi.model.entity.embed;

import java.io.Serial;
import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

/**
 * Componente embebido <Ordenante>.
 * <p>Separa la entidad legacy en componentes de valor para reducir
 * complejidad y cumplir con Sonar (<=10 campos por clase principal).</p>
 * <p>Mapea 1:1 a columnas existentes; no cambia la tabla.</p>
 */
@Getter @Setter
@Embeddable
public class Ordenante  implements Serializable {
   /* en
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
    @Column(name = "TXT_CTA_ORDEN")     private String cuenta;
    @Column(name = "TXT_NOMBRE_ORDEN")  private String nombre;
    @Column(name = "TXT_DIV_ORDEN")     private String divisa;
    @Column(name = "TXT_BIC_ORDEN")     private String bic;
    @Column(name = "TXT_BUC_CTE")       private String bucCliente;
}
