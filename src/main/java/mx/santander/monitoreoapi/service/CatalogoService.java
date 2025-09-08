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
 * Implementación
 *  de {@link ICatalogoService}.
 * Orquesta
 *  la 
 *  obtención
 *   de
 *    catálogos
 *     desde 
 *     repositorios 
 *     JPA.
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
    	 * Autor 
    	 * RRPM
    	 * V.10
    	 * Proyecto 
    	 * de 
    	 * apis
    	 * 9
    	 *  de
    	 *   septiembre
    	 *    del
    	 *     2025*/

        return new CatalogosResponse(
                tipoOperacionRepo.findAllAsOpcion(),
                divisaRepo.findAllAsOpcion(),
                tipoPagoRepo.findAllAsOpcion(),
                statusOperRepo.findAllAsOpcion()
               /*Catalogo
                * Response
                * con metodos
                * diversos
                * de retorno
                * 
                */
        );
    }
}
