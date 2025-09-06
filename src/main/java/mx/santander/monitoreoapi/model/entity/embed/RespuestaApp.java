package mx.santander.monitoreoapi.model.entity.embed;

import java.io.Serial;
import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

/**
 * Componente embebido <RespuestaApp>.
 * <p>Separa la entidad legacy en componentes de valor para reducir
 * complejidad y cumplir con Sonar (<=10 campos por clase principal).</p>
 * <p>Mapea 1:1 a columnas existentes; no cambia la tabla.</p>
 */
@Getter @Setter
@Embeddable
public class RespuestaApp implements Serializable {
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
    @Column(name = "TXT_DESC_RESP") private String descripcion;
    @Column(name = "TXT_COD_RESP")  private String codigo;
    @Column(name = "FLG_ENVIO")     private String flagEnvio;
    @Column(name = "TXT_IP")        private String ipCanal;
}
