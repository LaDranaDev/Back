package mx.santander.h2h.monitoreoapi.model.entity.embed;

import java.io.Serial;
import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
/**
 * Componente embebido <Identificadores>.
 * <p>Separa la entidad legacy en componentes de valor para reducir
 * complejidad y cumplir con Sonar (<=10 campos por clase principal).</p>
 * <p>Mapea 1:1 a columnas existentes; no cambia la tabla.</p>
 */
@Getter @Setter
@Embeddable
public class Identificadores implements Serializable {
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
    @Column(name = "ID_REFE_UNICO") private String referenciaUnica;
    @Column(name = "TXT_CANAL")     private String canal;
    @Column(name = "ID_TIPO_PAGO")  private String tipoPago;
    @Column(name = "ID_TIPO_OPER")  private String tipoOperacion;
    @Column(name = "ID_DIV")        private String divisa;
    @Column(name = "NUM_REFER")     private Integer transactionId;
    @Column(name = "TXT_TIPO_PRODUCT") private String tipoProducto;
}

