package mx.santander.monitoreoapi.model.entity.embed;

import java.io.Serial;
import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

/**
 * Componente embebido <Intermediario>.
 * <p>Separa la entidad legacy en componentes de valor para reducir
 * complejidad y cumplir con Sonar (<=10 campos por clase principal).</p>
 * <p>Mapea 1:1 a columnas existentes; no cambia la tabla.</p>
 */
@Getter @Setter
@Embeddable
public class Intermediario implements Serializable {
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
    @Column(name = "TXT_NOMBRE_INTER") private String nombre;
    @Column(name = "TXT_CD_INTER")     private String ciudad;
    @Column(name = "CLV_PAIS_INTER")   private String pais;
    @Column(name = "TXT_BIC_INTER")    private String bic;
}
