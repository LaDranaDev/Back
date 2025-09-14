package mx.santander.h2h.monitoreoapi.constants;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class QueryConstantsTest {

    @Test
    void constantes_estanDefinidasYNoVacias() {
        assertThat(QueryConstants.RESUMEN_PRODUCTO_ESTATUS).contains("SELECT");
        assertThat(QueryConstants.CONTEO_PAGOS_POR_DIVISA).contains("GROUP BY");
        assertThat(QueryConstants.TOTALES_GLOBALES).contains("SUM");
        assertThat(QueryConstants.DETALLE_POR_FILTROS).contains("WHERE");
        assertThat(QueryConstants.SUMA_IMPORTE_POR_DIVISA_DETALLE).contains("GROUP BY");
        assertThat(QueryConstants.DIVISAS_OPCION_CATALOGO).contains("OpcionCatalogo");
        assertThat(QueryConstants.DEFAULT_PAGE_SIZE).isPositive();
        assertThat(QueryConstants.MAX_PAGE_SIZE)
                .isGreaterThanOrEqualTo(QueryConstants.DEFAULT_PAGE_SIZE);
    }

    @Test
    void constructorPrivado_lanzaIllegalStateException() throws Exception {
        var ctor = QueryConstants.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        assertThatThrownBy(ctor::newInstance)
                .hasCauseInstanceOf(IllegalStateException.class)
                .hasRootCauseMessage("Clase utilitaria de constantes - no debe ser instanciada");
    }
}
