package mx.santander.monitoreoapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import mx.santander.monitoreoapi.model.request.PagoDetalleFiltroDTO;
import mx.santander.monitoreoapi.model.request.PagoRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas de serialización y comportamiento de DTOs.
 * Objetivo:
 * - Validar que los objetos de request pueden serializarse y deserializarse correctamente en JSON.
 * - Comprobar que los setters y getters de los DTO funcionan como se espera.
 */
class DtoSerializationTest {

    /**
     * Configuración del ObjectMapper:
     * - Registra automáticamente módulos como JavaTimeModule.
     * - Deshabilita la escritura de fechas como timestamps para legibilidad.
     */
    private final ObjectMapper mapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    /**
     * Caso de prueba: serializar y deserializar {@link PagoRequest}.
     * Escenario:
     * - Se construye un objeto con datos de prueba en sus campos.
     * - Se convierte a JSON y se vuelve a leer en un nuevo objeto.
     * - Se validan valores clave para confirmar que la transformación es consistente.
     */
    @Test
    void pagoRequest_serializeDeserialize_ok() throws Exception {
        // Construcción del request
        var req = new PagoRequest();
        req.setOperacion("EN");
        req.setDivisa("MXN");
        req.setTipoPago("SPEI");
        req.setTipoOperacion("PAGO");
        req.setReferenciaCanal("REF-001");

        // Asignación de cuentas
        var cuentas = new PagoRequest.Cuentas();
        cuentas.setCuentaAbono("123");
        cuentas.setCuentaCargo("456");
        req.setCuentas(cuentas);

        // Asignación de identificadores
        var ids = new PagoRequest.Identificadores();
        ids.setCanal("H2H");
        ids.setTransactionId("99");
        req.setIdentificadores(ids);

        // Asignación de rango de fechas
        var fechas = new PagoRequest.RangoFechas();
        fechas.setFechaInicio(LocalDate.now().minusDays(1));
        fechas.setFechaFin(LocalDate.now());
        req.setRangoFechas(fechas);

        // Serialización a JSON
        String json = mapper.writeValueAsString(req);
        // Deserialización de regreso a objeto
        var back = mapper.readValue(json, PagoRequest.class);

        // Verificaciones de consistencia
        assertThat(back.getOperacion()).isEqualTo("EN");
        assertThat(back.getDivisa()).isEqualTo("MXN");
        assertThat(back.getCuentas().getCuentaAbono()).isEqualTo("123");
        assertThat(back.getIdentificadores().getCanal()).isEqualTo("H2H");
    }

    /**
     * Caso de prueba: uso de setters en {@link PagoDetalleFiltroDTO}.
     * Escenario:
     * - Se crea una instancia del DTO y se establecen valores con los setters.
     * - Se define un rango de fechas.
     * - Se validan los valores mediante los getters.
     */
    @Test
    void pagoDetalleFiltroDTO_setters_ok() {
        // Construcción del filtro
        var f = new PagoDetalleFiltroDTO();
        f.setOperacion("EN");
        f.setCuentaAbono("123");
        f.setDivisa("MXN");
        f.setEstatus("PROCESADO");
        f.setTransactionId(77L);

        // Rango de fechas
        var r = new PagoDetalleFiltroDTO.RangoFechas(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now()
        );
        f.setRangoFechas(r);

        // Verificaciones de los valores
        assertThat(f.getOperacion()).isEqualTo("EN");
        assertThat(f.getCuentaAbono()).isEqualTo("123");
        assertThat(f.getRangoFechas().getFechaFin()).isNotNull();
        assertThat(f.getTransactionId()).isEqualTo(77L);
    }
}
