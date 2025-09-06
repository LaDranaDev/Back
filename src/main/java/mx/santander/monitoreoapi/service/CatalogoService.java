package mx.santander.monitoreoapi.service;

import lombok.RequiredArgsConstructor;
import mx.santander.monitoreoapi.model.response.CatalogosResponse;
import mx.santander.monitoreoapi.repository.IDivisaRepository;
import mx.santander.monitoreoapi.repository.IStatusOperRepository;
import mx.santander.monitoreoapi.repository.ITipoOperacionRepository;
import mx.santander.monitoreoapi.repository.ITipoPagoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación de {@link ICatalogoService}.
 * Orquesta la obtención de catálogos desde repositorios JPA.
 */
@Service
@RequiredArgsConstructor
public class CatalogoService implements ICatalogoService {

    private final ITipoOperacionRepository tipoOperacionRepo;
    private final IDivisaRepository        divisaRepo;
    private final ITipoPagoRepository      tipoPagoRepo;
    private final IStatusOperRepository    statusOperRepo;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public CatalogosResponse obtenerCatalogosFiltros() {
        /*
         * vamos
         * a
         * agregar
         * mas
         * comentarios
         * en
         * esta
         * parte*/
        return new CatalogosResponse(
                tipoOperacionRepo.findAllAsOpcion(),
                divisaRepo.findAllAsOpcion(),
                tipoPagoRepo.findAllAsOpcion(),
                statusOperRepo.findAllAsOpcion()
                /*
                * vamos
                * a
                * agregar
                * mas
                * comentarios
                * en
                * esta
                * parte*/
        );
    }
}
