package mx.santander.monitoreoapi.request;

import mx.santander.monitoreoapi.model.request.PagoRequest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PagoRequestIdentificadoresTest {

    // Casos: (canalIn, txnIn) -> fragmentos esperados en toString()
    static Stream<Arguments> casos() {
        return Stream.of(
                // canal = null  -> "canal=null"
                // txn = null    -> "transactionId=***"
                Arguments.of(null, null, "canal=null", "transactionId=***"),

                // canal con \r\n -> "canal=CANAL"
                // txn < 3        -> "***"
                Arguments.of("CANAL\r\n", "12", "canal=CANAL", "transactionId=***"),

                // canal normal   -> "canal=BACK"
                // txn >= 3       -> "***" + últimos 3
                Arguments.of("BACK", "123456", "canal=BACK", "transactionId=***456"),

                // NUEVO CASO: txn exactamente 3 caracteres -> "***"
                Arguments.of("TEST", "123", "canal=TEST", "transactionId=***"),

                // NUEVO CASO: txn con 1 caracter -> "***"
                Arguments.of("API", "1", "canal=API", "transactionId=***")
        );
    }

    @ParameterizedTest(name = "[{index}] canal=\"{0}\" txn=\"{1}\"")
    @MethodSource("casos")
    void toString_parametrizado(String canalIn, String txnIn,
                                String esperadoCanal, String esperadoTxn) {
        PagoRequest.Identificadores id = new PagoRequest.Identificadores();
        id.setCanal(canalIn);
        id.setTransactionId(txnIn);

        String s = id.toString();

        // Un solo assert encadenado para satisfacer el linter
        assertThat(s).contains(esperadoCanal, esperadoTxn);
    }
}