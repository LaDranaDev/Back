package mx.santander.h2h.monitoreoapi.service;

import lombok.RequiredArgsConstructor;
import mx.santander.h2h.monitoreoapi.model.response.CatalogosResponse;
import mx.santander.h2h.monitoreoapi.repository.IDivisaRepository;
import mx.santander.h2h.monitoreoapi.repository.IStatusOperRepository;
import mx.santander.h2h.monitoreoapi.repository.ITipoOperacionRepository;
import mx.santander.h2h.monitoreoapi.repository.ITipoPagoRepository;
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
