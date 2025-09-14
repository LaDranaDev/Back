// src/test/java/mx/santander/monitoreoapi/model/response/DetalleConImporteGlobalDTOTest.java
package mx.santander.h2h.monitoreoapi.model.response;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DetalleConImporteGlobalDTOTest {

    @Test
    void from_mapeaCamposCorrectamente() {
        Page<String> page = new PageImpl<>(
                List.of("A", "B"),
                PageRequest.of(2, 5),
                12   // totalElements coherente con 2 páginas llenas (5+5) y 2 en la última
        );

        List<ImporteTotalDivisaDTO> totales =
                List.of(new ImporteTotalDivisaDTO("MXN", new BigDecimal("100.00")));

        var dto = DetalleConImporteGlobalDTO.from(page, totales);

        // content
        assertThat(dto.getContent()).containsExactly("A", "B");

        // page info
        assertThat(dto.getPage().getPageNumber()).isEqualTo(2);
        assertThat(dto.getPage().getPageSize()).isEqualTo(5);
        assertThat(dto.getPage().getTotalElements()).isEqualTo(12); // corregido
        assertThat(dto.getPage().getTotalPages()).isEqualTo(3);
        assertThat(dto.getPage().isLast()).isTrue();

        // importes
        assertThat(dto.getImportesTotales()).hasSize(1);
        assertThat(dto.getImportesTotales().get(0).divisa()).isEqualTo("MXN");
        assertThat(dto.getImportesTotales().get(0).importeTotal()).isEqualByComparingTo("100.00");
    }

    @Test
    void from_noUltimaPagina_casoComplementario() {
        Page<String> page = new PageImpl<>(
                List.of("X"),
                PageRequest.of(0, 5),
                13   // ahora el total da 3 páginas, no es la última
        );

        var dto = DetalleConImporteGlobalDTO.from(page, List.of());

        assertThat(dto.getPage().getPageNumber()).isZero();
        assertThat(dto.getPage().getTotalPages()).isEqualTo(3);
        assertThat(dto.getPage().isLast()).isFalse();
        assertThat(dto.getImportesTotales()).isEmpty();
    }
}
