package mx.santander.monitoreoapi.model.entity.embed;

import java.io.Serial;
import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

/**
 * Componente embebido <Fechas>.
 * <p>Separa la entidad legacy en componentes de valor para reducir
 * complejidad y cumplir con Sonar (<=10 campos por clase principal).</p>
 * <p>Mapea 1:1 a columnas existentes; no cambia la tabla.</p>
 */
@Getter @Setter
@Embeddable
public class Fechas implements Serializable {

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
    @Column(name = "FCH_ENVIO")     private java.time.LocalDateTime envio;
    @Column(name = "FCH_RESP_APP")  private java.time.LocalDateTime respApp;
    @Column(name = "FCH_ULT_MOD")   private java.time.LocalDateTime ultimaMod;
    @Column(name = "FCH_CARGA")     private java.time.LocalDateTime carga;
}
