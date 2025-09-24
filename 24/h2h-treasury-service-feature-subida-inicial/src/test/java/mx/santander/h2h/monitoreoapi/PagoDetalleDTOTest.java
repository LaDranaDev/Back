package mx.santander.h2h.monitoreoapi;

import mx.santander.h2h.monitoreoapi.model.response.PagoDetalleDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas unitarias para {@link PagoDetalleDTO}.
 * Objetivo:
 * - Validar que los accesores generados por el record funcionan correctamente.
 * - Confirmar que los valores asignados en el constructor se mantienen.
 */
class PagoDetalleDTOTest {

    /**
     * Caso de prueba: creación de un record y validación de accesores.
     * Escenario:
     * - Se instancia un {@link PagoDetalleDTO} con datos de prueba.
     * - Se verifica que los accesores devuelven los valores esperados.
     */
    @Test
    void record_accesors() {
        // Construcción del record con datos de prueba
        var dto = new PagoDetalleDTO(
                "EN", "123", BigDecimal.ONE, "REF", "MXN",
                LocalDateTime.of(2025, 1, 1, 12, 0),
                "456", "SPEI", "PROCESADO", 99
        );

        // Verificación de valores
        assertThat(dto.operacion()).isEqualTo("EN");
        assertThat(dto.cuentaAbono()).isEqualTo("123");
        assertThat(dto.divisa()).isEqualTo("MXN");
        assertThat(dto.transactionId()).isEqualTo(99);
    }
}
