package mx.santander.h2h.monitoreoapi.model.response;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PagedDataDTOTest {

    @Test
    void shouldCreatePagedDataDTOFromPage() {
        // Given
        List<String> content = List.of("item1", "item2", "item3");
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<String> page = new PageImpl<>(content, pageRequest, 15);

        // When
        PagedDataDTO<String> result = new PagedDataDTO<>(page);

        // Then
        assertEquals(content, result.getContent());
        assertEquals(0, result.getNumber());
        assertEquals(10, result.getSize());
        assertEquals(15, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        assertTrue(result.isFirst());
        assertFalse(result.isLast());
        assertEquals(3, result.getNumberOfElements());
        assertFalse(result.isEmpty());
    }

    @Test
    void shouldCreatePagedDataDTOFromEmptyPage() {
        // Given
        Page<String> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);

        // When
        PagedDataDTO<String> result = new PagedDataDTO<>(emptyPage);

        // Then
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getNumber());
        assertEquals(10, result.getSize());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
        assertTrue(result.isFirst());
        assertTrue(result.isLast());
        assertEquals(0, result.getNumberOfElements());
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenPageIsNull() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new PagedDataDTO<String>(null)
        );
        assertEquals("Page cannot be null", exception.getMessage());
    }

    @Test
    void shouldCreateWithNoArgsConstructor() {
        // When
        PagedDataDTO<String> result = new PagedDataDTO<>();

        // Then
        assertNotNull(result);
        assertNull(result.getContent());
        assertEquals(0, result.getNumber());
        assertEquals(0, result.getSize());
    }
}