package mx.santander.h2h.monitoreoapi.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * DTO 
 * que 
 * encapsula
 *  datos
 *  paginados
 *  manteniendo 
 * compatibilidad 
 * con
 *  la 
 * estructura
 *  JSON existente
 * pero 
 * evitando
 *  la serialización directa de PageImpl.
 *
 * @param <T> tipo de elementos contenidos en la página
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagedDataDTO<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<T> content;
    private int number;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
    private int numberOfElements;
    private boolean empty;

    /**
     * Constructor 
     * que 
     * convierte
     *  un Page
     *  de Spring en un DTO serializable.
     * 
     * @param page página de Spring Data a convertir
     * @throws IllegalArgumentException si page es null
     */
    public PagedDataDTO(Page<T> page) {
        if (page == null) {
            throw new IllegalArgumentException("Page cannot be null");
        }
        this.content = page.getContent();
        this.number = page.getNumber();
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.first = page.isFirst();
        this.last = page.isLast();
        this.numberOfElements = page.getNumberOfElements();
        this.empty = page.isEmpty();
    }
}