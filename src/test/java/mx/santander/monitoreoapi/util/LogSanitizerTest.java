package mx.santander.monitoreoapi.util;
/*author
 * rr
 * pm
 * v
 * 1
 * .0
 * 0
 * uteleria
 * para
 * sanitizar
 * logs
 * en
 * las
 * respuestas
 * cubriendo
 * odo
 *  lo
 * que
 * suponemos
 * s*/
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Pruebas para el sanitizador de logs - porque los hackers son creativos
 * y nosotros tenemos que ser más creativos que ellos
 */
class LogSanitizerTest {

    // Casos de prueba para clean() - probando que no se cuelen caracteres raros
    static Stream<Arguments> cleanCases() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("", ""),
                Arguments.of("normal text without special chars", "normal text without special chars"),
                Arguments.of("text with\rcarriage return", "text with_carriage return"),
                Arguments.of("text with\nnewline", "text with_newline"),
                Arguments.of("text with\ttab", "text with_tab"),
                Arguments.of("text\rwith\nall\tspecial", "text_with_all_special")
        );
    }

    @ParameterizedTest(name = "[{index}] clean(\"{0}\") -> \"{1}\"")
    @MethodSource("cleanCases")
    void clean_parametrized(String input, String expected) {
        String result = LogSanitizer.clean(input);
        assertThat(result).isEqualTo(expected);
    }

    // Casos para maskTail() - probando diferentes longitudes porque siempre hay edge cases
    // que nos hacen la vida imposible en producción

    static Stream<Arguments> maskTailCases() {
        return Stream.of(
                // (input, visible, expected)
                Arguments.of(null, 3, "***"),                    // null siempre es problemático
                Arguments.of("", 3, "***"),                      // string vacío también
                Arguments.of("12", 3, "***"),                    // muy corto, mejor ocultar tod
                Arguments.of("123", 3, "***"),                   // justo el límite, seguimos ocultando
                Arguments.of("1234", 3, "***234"),               // ahora sí podemos mostrar algo
                Arguments.of("123456789", 4, "***6789"),         // caso normal funcionando
                Arguments.of("a", 1, "***"),                     // un solo carácter en el límite
                Arguments.of("ab", 1, "***b"),                   // dos caracteres, uno visible
                Arguments.of("password123", 0, "***"),           // visible = 0, ocultar tod
                Arguments.of("short", 10, "***"),                // visible mayor que el string
                Arguments.of("exactly4", 4, "***tly4"),    // length > visible, muestra últimos 4
                Arguments.of("morethan4", 4, "***han4"),         // más largo, ya podemos mostrar
                Arguments.of("test", -1, "***")                  // números negativos no tienen sentido
        );
    }

    @ParameterizedTest(name = "[{index}] maskTail(\"{0}\", {1}) -> \"{2}\"")
    @MethodSource("maskTailCases")
    void maskTail_parametrized(String input, int visible, String expected) {
        String result = LogSanitizer.maskTail(input, visible);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void maskTail_withVeryLongString_shouldMaskCorrectly() {
        // Probando con el abecedario completo porque... ¿por qué no?
        String longString = "abcdefghijklmnopqrstuvwxyz";
        String result = LogSanitizer.maskTail(longString, 3);
        assertThat(result).isEqualTo("***xyz");
    }

    // Verificando que nadie pueda instanciar esta clase por error
    @Test
    void constructor_shouldBePrivate_andThrowException() throws Exception {
        Constructor<LogSanitizer> constructor = LogSanitizer.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        assertThatThrownBy(constructor::newInstance)
                .isInstanceOf(InvocationTargetException.class)
                .hasCauseInstanceOf(UnsupportedOperationException.class)
                .hasRootCauseMessage("Utility class");
    }

    // Test de integración - porque en la vida real los métodos se usan juntos
    @Test
    void integration_cleanThenMask() {
        String dirtyInput = "user\npassword123";  // simulando input sucio del usuario
        String cleaned = LogSanitizer.clean(dirtyInput);
        String masked = LogSanitizer.maskTail(cleaned, 3);

        assertThat(cleaned).isEqualTo("user_password123");  // caracteres raros limpiados
        assertThat(masked).isEqualTo("***123");             // y password enmascarado
    }
}