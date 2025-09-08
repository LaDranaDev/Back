package mx.santander.monitoreoapi.util;

/**
 * Utilería para sanitizar logs en las respuestas.
 * Ayuda a prevenir log injection y enmascarar datos sensibles.
 *
 * @author rr
 * @version 1.0
 */
public final class LogSanitizer {

    private LogSanitizer() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Remueve CR/LF/TAB para evitar log forging.
     * @param s string a limpiar
     * @return string limpio o null si input era null
     */
    public static String clean(String s) {
        return s == null ? null : s.replaceAll("[\\r\\n\\t]", "_");
    }

    /**
     * Enmascara dejando últimos N visibles.
     * @param s string a enmascarar
     * @param visible número de caracteres visibles al final
     * @return string enmascarado
     */
    public static String maskTail(String s, int visible) {
        // Si viene null, vacío, o visible negativo -> ocultar odo
        if (s == null || s.isEmpty() || visible < 0) {
            return "***";
        }

        // CLAVE: Si length <= visible, ocultar odo por seguridad
        if (s.length() <= visible) {
            return "***";
        }

        // Solo aquí mostramos los últimos caracteres
        // Utilería de seguridad para enmascarar datos necesaria para logs
        return "***" + s.substring(s.length() - visible);
    }
}
