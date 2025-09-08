package mx.santander.monitoreoapi.entity.embed;

import mx.santander.monitoreoapi.model.entity.OperPagoEntity;
import mx.santander.monitoreoapi.model.entity.embed.Fechas;
import mx.santander.monitoreoapi.model.entity.embed.Identificadores;
import mx.santander.monitoreoapi.model.entity.embed.Intermediario;
import mx.santander.monitoreoapi.model.entity.embed.Montos;
import mx.santander.monitoreoapi.model.entity.embed.Ordenante;
import mx.santander.monitoreoapi.model.entity.embed.Receptor;
import mx.santander.monitoreoapi.model.entity.embed.RespuestaApp;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OperPagoEntityToStringTest {

    @Test
    void toString_noDebeIncluirTxtContratoJson_yNoFallaConNulos() {
        // given
        OperPagoEntity e = new OperPagoEntity();
        e.setIdPagoPk(1L);
        // Contenido sensible que NO debe aparecer en el toString por el @ToString(exclude = "txtContratoJson")
        e.setTxtContratoJson("{\"secret\":\"1234\",\"token\":\"abc\"}");

        // Relaciones/embebidos nulos a propósito: no debe explotar el toString
        e.setStatusOper(null);
        e.setIdentificadores(null);
        e.setOrdenante(null);
        e.setReceptor(null);
        e.setIntermediario(null);
        e.setFechas(null);
        e.setRespuestaApp(null);
        e.setMontos(null);

        // when
        String ts = e.toString();

        // then
        assertThat(ts).isNotBlank();
        assertThat(ts).contains("OperPagoEntity");
        // Lombok imprime el nombre del campo; aprovechamos para verificar el id
        assertThat(ts).contains("idPagoPk=1");

        // Lo sensible NO debe estar
        assertThat(ts).doesNotContain("secret")
                .doesNotContain("1234")
                .doesNotContain("token")
                .doesNotContain("abc")
                .doesNotContain("TXT_CONTRATO_JSON"); // por si acaso alguien concatena el nombre de columna
    }

    @Test
    void toString_conEmbebidosInicializados_noIncluyeTxtContratoJson() {
        // given
        OperPagoEntity e = new OperPagoEntity();
        e.setIdPagoPk(2L);
        e.setTxtContratoJson("{\"otra\":\"clave\"}");

        // Inicializamos embebidos para asegurar que el toString sigue siendo seguro
        e.setIdentificadores(new Identificadores());
        e.setOrdenante(new Ordenante());
        e.setReceptor(new Receptor());
        e.setIntermediario(new Intermediario());
        e.setFechas(new Fechas());
        e.setRespuestaApp(new RespuestaApp());
        e.setMontos(new Montos());

        // when
        String ts = e.toString();

        // then
        assertThat(ts).contains("OperPagoEntity")
                .contains("idPagoPk=2");
        assertThat(ts).doesNotContain("otra")
                .doesNotContain("clave");
    }
}
