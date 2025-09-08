package mx.santander.monitoreoapi.response;

import mx.santander.monitoreoapi.model.response.PagoDetalleDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PagoDetalleDTOTest {

    @Test
    void maskedFields_shouldMaskSensitiveData() {
        // given
        PagoDetalleDTO dto = new PagoDetalleDTO(
                "SPEI",          // operacion
                "1234567890",    // cuentaAbono
                BigDecimal.TEN,  // importe
                "REF001",        // referenciaCanal
                "MXN",           // divisa
                LocalDateTime.now(), // fechaCarga
                "9876543210",    // cuentaCargo
                "PAGO",          // tipoPago
                "EN",            // estatus
                123456           // transactionId
        );

        // when
        String abonoMasked = dto.cuentaAbonoMasked();
        String cargoMasked = dto.cuentaCargoMasked();
        String txnMasked = dto.transactionIdMasked();

        // then
        assertThat(abonoMasked).isEqualTo("****7890");
        assertThat(cargoMasked).isEqualTo("****3210");
        assertThat(txnMasked).isEqualTo("***456");
    }

    @Test
    void maskedFields_shouldHandleNullsAndShortValues() {
        // given
        PagoDetalleDTO dto = new PagoDetalleDTO(
                "SPEI", null, BigDecimal.ZERO, "R", "MXN",
                LocalDateTime.now(), "12", "PAGO", "EN", null
        );

        // then
        assertThat(dto.cuentaAbonoMasked()).isEqualTo("****");
        assertThat(dto.cuentaCargoMasked()).isEqualTo("****"); // very short
        assertThat(dto.transactionIdMasked()).isEqualTo("***");
    }

    // NUEVOS TESTS PARA CUBRIR LA RAMA FALTANTE
    @Test
    void transactionIdMasked_shouldHandleShortTransactionIds() {
        // given - transactionId con 1 dígito
        PagoDetalleDTO dto1 = new PagoDetalleDTO(
                "SPEI", "1234567890", BigDecimal.TEN, "REF001", "MXN",
                LocalDateTime.now(), "9876543210", "PAGO", "EN", 1
        );

        // given - transactionId con 2 dígitos
        PagoDetalleDTO dto2 = new PagoDetalleDTO(
                "SPEI", "1234567890", BigDecimal.TEN, "REF001", "MXN",
                LocalDateTime.now(), "9876543210", "PAGO", "EN", 12
        );

        // given - transactionId con exactamente 3 dígitos
        PagoDetalleDTO dto3 = new PagoDetalleDTO(
                "SPEI", "1234567890", BigDecimal.TEN, "REF001", "MXN",
                LocalDateTime.now(), "9876543210", "PAGO", "EN", 123
        );

        // then - todos deben retornar "***" porque length <= 3
        assertThat(dto1.transactionIdMasked()).isEqualTo("***");
        assertThat(dto2.transactionIdMasked()).isEqualTo("***");
        assertThat(dto3.transactionIdMasked()).isEqualTo("***");
    }

    @Test
    void transactionIdMasked_shouldHandleLongTransactionIds() {
        // given - transactionId con más de 3 dígitos
        PagoDetalleDTO dto = new PagoDetalleDTO(
                "SPEI", "1234567890", BigDecimal.TEN, "REF001", "MXN",
                LocalDateTime.now(), "9876543210", "PAGO", "EN", 9876543
        );

        // then - debe retornar "***" + últimos 3 dígitos
        assertThat(dto.transactionIdMasked()).isEqualTo("***543");
    }
}