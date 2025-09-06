package mx.santander.monitoreoapi;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Prueba rápida ("smoke test") para entidades JPA.
 * Objetivo:
 * - Verificar que todas las entidades del paquete model.entity
 *   cuenten con un constructor sin argumentos accesible.
 * - Asegurar que la invocación de toString() no provoque errores
 *   (validación mínima de generación de métodos).
 * Esta prueba no valida reglas de negocio ni persistencia en base de datos.
 * Únicamente asegura que las clases cumplen los requisitos mínimos
 * para ser utilizadas por JPA y otras capas de la aplicación.
 */
class EntitySmokeTest {

    /**
     * Recorre todas las entidades y valida:
     * - Que se pueda instanciar usando el constructor por defecto.
     * - Que la llamada a toString() devuelva un valor no nulo.
     */
    @Test
    void jpa_entities_have_noArgConstructor() throws Exception {
        String base = "mx.santander.monitoreoapi.model.entity.";
        String[] entities = new String[] {
                base + "Auditoria",
                base + "CanalEntity",
                base + "CanalId",
                base + "DivisaEntity",
                base + "OperPagoEntity",
                base + "ParamTransEntity",
                base + "StatusOperEntity",
                base + "TipoOperacionEntity",
                base + "TipoPagoEntity"
        };

        for (String fqcn : entities) {
            Class<?> c = Class.forName(fqcn);
            Object instance = c.getDeclaredConstructor().newInstance();

            // Validación de instanciación exitosa
            assertThat(instance).isNotNull();

            // Validación mínima de implementación de toString()
            assertThat(instance.toString()).isNotNull();
        }
    }
}
