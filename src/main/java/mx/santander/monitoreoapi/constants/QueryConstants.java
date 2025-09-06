package mx.santander.monitoreoapi.constants;

/**
 * Constantes de consultas JPQL para operaciones de monitoreo y catálogos.
 *
 * <p>
 * Esta clase centraliza todas las consultas JPQL utilizadas en los repositorios
 * de la aplicación de monitoreo de APIs. Separar las consultas en constantes
 * facilita el mantenimiento, reutilización y testing de las queries de base de datos.
 * </p>
 *
 * <p>
 * Las consultas están organizadas por funcionalidad:
 * - Resúmenes y agregaciones de operaciones de pago
 * - Consultas de detalle con filtros dinámicos
 * - Catálogos para componentes de interfaz de usuario
 * - Consultas de totales y montos por divisa
 * </p>
 *
 * <p>
 * Todas las consultas utilizan parámetros nombrados para evitar inyección SQL
 * y permiten filtros opcionales mediante verificaciones de nulos en las cláusulas WHERE.
 * </p>
 *
 * @author Rodrigo RPM
 * @version 1.0
 * @since 1.0
 */
public final class QueryConstants {

    // ===== CONSTANTES DE CONFIGURACIÓN DE PAGINACIÓN =====

    /**
     * Tamaño de página por defecto para consultas paginadas.
     *
     * <p>
     * Define el número estándar de registros a mostrar por página cuando
     * el usuario no especifica un tamaño particular. Este valor optimiza
     * el balance entre performance y usabilidad en la interfaz.
     * </p>
     */
    public static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * Tamaño máximo permitido para páginas en consultas paginadas.
     *
     * <p>
     * Establece el límite superior de registros por página para prevenir
     * consultas excesivamente grandes que puedan impactar negativamente
     * en el rendimiento del sistema o la experiencia del usuario.
     * </p>
     */
    public static final int MAX_PAGE_SIZE = 100;

    // ===== CONSULTAS DE RESUMEN Y AGREGACIÓN =====

    /**
     * Consulta de resumen de operaciones agrupado por producto y estatus.
     *
     * <p>
     * Obtiene la cantidad de operaciones clasificadas por tipo de producto
     * y su estatus actual (pendiente, procesado, rechazado, etc.).
     * Utilizada para generar gráficas y tablas resumidas en el dashboard.
     * </p>
     *
     * <p>Parámetros opcionales: divisa, fechaInicio, fechaFin</p>
     */
    public static final String RESUMEN_PRODUCTO_ESTATUS = """
    SELECT new mx.santander.monitoreoapi.model.response.ResumenProductoEstatusDTO(
        p.identificadores.tipoProducto,
        p.statusOper.descripcion,
        COUNT(p)
    )
    FROM OperPagoEntity p
    WHERE (:divisa IS NULL OR p.identificadores.divisa = :divisa)
      AND (:fechaInicio IS NULL OR p.fechas.carga >= :fechaInicio)
      AND (:fechaFin IS NULL OR p.fechas.carga <= :fechaFin)
    GROUP BY p.identificadores.tipoProducto, p.statusOper.descripcion
    HAVING COUNT(p) > 0
""";


    /**
     * Consulta de conteo de pagos por divisa.
     *
     * <p>
     * Cuenta el número total de operaciones procesadas para cada divisa
     * (USD, MXN, EUR, etc.) dentro del rango de fechas especificado.
     * Útil para análisis de volumen por moneda.
     * </p>
     *
     * <p>Parámetros opcionales: fechaInicio, fechaFin</p>
     */
    public static final String CONTEO_PAGOS_POR_DIVISA = """
    SELECT p.identificadores.divisa, COUNT(p)
    FROM OperPagoEntity p
    WHERE (:fechaInicio IS NULL OR p.fechas.carga >= :fechaInicio)
      AND (:fechaFin IS NULL OR p.fechas.carga <= :fechaFin)
    GROUP BY p.identificadores.divisa
""";

    /**
     * Consulta de totales globales de operaciones.
     *
     * <p>
     * Calcula el total de operaciones y la suma de importes con filtros
     * opcionales por divisa y producto. Proporciona una vista consolidada
     * de la actividad operacional del sistema.
     * </p>
     *
     * <p>Parámetros opcionales: divisa, producto, fechaInicio, fechaFin</p>
     */
    public static final String TOTALES_GLOBALES = """
    SELECT new mx.santander.monitoreoapi.model.response.TotalGlobalOperacionesDTO(
        COUNT(p),
        COALESCE(SUM(p.montos.importe),0)
    )
    FROM OperPagoEntity p
    WHERE (:divisa IS NULL OR p.identificadores.divisa = :divisa)
      AND (:producto IS NULL OR p.identificadores.tipoProducto = :producto)
      AND (:fechaInicio IS NULL OR p.fechas.carga >= :fechaInicio)
      AND (:fechaFin IS NULL OR p.fechas.carga <= :fechaFin)
""";

    /**
     * Consulta de resumen por producto, divisa y estatus.
     *
     * <p>
     * Genera combinaciones detalladas de tipoPago, tipoOperacion, divisa
     * y estatus con sus respectivos conteos. Permite análisis granular
     * de la distribución de operaciones por múltiples dimensiones.
     * </p>
     *
     * <p>Parámetros opcionales: divisa, tipoPago, tipoOperacion, fechaInicio, fechaFin</p>
     */
    public static final String RESUMEN_PRODUCTO_DIVISA_ESTATUS = """
    SELECT new mx.santander.monitoreoapi.model.response.ResumenProductoDivisaEstatusDTO(
        CONCAT(p.identificadores.tipoPago, ' ', p.identificadores.tipoOperacion),
        p.identificadores.divisa,
        p.statusOper.descripcion,
        COUNT(p)
    )
    FROM OperPagoEntity p
    WHERE (:divisa IS NULL OR p.identificadores.divisa = :divisa)
      AND (:tipoPago IS NULL OR p.identificadores.tipoPago = :tipoPago)
      AND (:tipoOperacion IS NULL OR p.identificadores.tipoOperacion = :tipoOperacion)
      AND (:fechaInicio IS NULL OR p.fechas.carga >= :fechaInicio)
      AND (:fechaFin IS NULL OR p.fechas.carga <= :fechaFin)
    GROUP BY p.identificadores.tipoPago, p.identificadores.tipoOperacion,
             p.identificadores.divisa, p.statusOper.descripcion
""";

    /**
     * Consulta de resumen por estatus de operación.
     *
     * <p>
     * Cuenta las operaciones agrupadas por estatus (pendiente, procesado,
     * rechazado, etc.) para mostrar la distribución del estado de las
     * transacciones en el sistema de monitoreo.
     * </p>
     *
     * <p>Parámetros opcionales: divisa, fechaInicio, fechaFin</p>
     */
    public static final String RESUMEN_POR_ESTATUS = """
    SELECT new mx.santander.monitoreoapi.model.response.ResumenEstatusGlobalDTO(
        p.statusOper.descripcion,
        COUNT(p)
    )
    FROM OperPagoEntity p
    WHERE (:divisa IS NULL OR p.identificadores.divisa = :divisa)
      AND (:fechaInicio IS NULL OR p.fechas.carga >= :fechaInicio)
      AND (:fechaFin IS NULL OR p.fechas.carga <= :fechaFin)
    GROUP BY p.statusOper.descripcion
    HAVING COUNT(p) > 0
""";
    /**
     * Consulta de monto total por divisa.
     *
     * <p>
     * Suma el importe total de operaciones agrupado por divisa para
     * análisis financiero y reporting de volúmenes monetarios procesados
     * en el sistema durante el período especificado.
     * </p>
     *
     * <p>Parámetros opcionales: fechaInicio, fechaFin</p>
     */
    public static final String MONTO_TOTAL_POR_DIVISA = """
    SELECT new mx.santander.monitoreoapi.model.response.MontoTotalDivisaDTO(
        p.identificadores.divisa,
        COALESCE(SUM(p.montos.importe),0)
    )
    FROM OperPagoEntity p
    WHERE (:fechaInicio IS NULL OR p.fechas.carga >= :fechaInicio)
      AND (:fechaFin IS NULL OR p.fechas.carga <= :fechaFin)
    GROUP BY p.identificadores.divisa
""";

    // ===== CONSULTAS DE DETALLE CON FILTROS DINÁMICOS =====

    /**
     * Consulta de detalle paginado de operaciones con filtros dinámicos.
     *
     * <p>
     * Obtiene el detalle completo de operaciones aplicando filtros opcionales
     * por cualquier campo. Utilizada en tablas paginadas donde el usuario
     * puede buscar y filtrar por múltiples criterios simultáneamente.
     * </p>
     *
     * <p>
     * Soporta filtros por: operación, cuentaAbono, importe, referenciaCanal,
     * divisa, fechas, cuentaCargo, tipoPago, estatus, transactionId
     * </p>
     */
    public static final String DETALLE_POR_FILTROS = """
SELECT new mx.santander.monitoreoapi.model.response.PagoDetalleDTO(
    p.identificadores.tipoOperacion,
    p.receptor.cuenta,
    p.montos.importe,
    p.identificadores.referenciaUnica,
    p.identificadores.divisa,
    p.fechas.carga,
    p.ordenante.cuenta,
    p.identificadores.tipoPago,
    p.statusOper.descripcion,
    p.identificadores.transactionId
)
FROM OperPagoEntity p
WHERE (:#{#filtro.operacion} IS NULL OR p.identificadores.tipoOperacion = :#{#filtro.operacion})
  AND (:#{#filtro.cuentaAbono} IS NULL OR p.receptor.cuenta = :#{#filtro.cuentaAbono})
  AND (:#{#filtro.importe} IS NULL OR p.montos.importe = :#{#filtro.importe})
  AND (:#{#filtro.referenciaCanal} IS NULL OR p.identificadores.referenciaUnica = :#{#filtro.referenciaCanal})
  AND (:#{#filtro.divisa} IS NULL OR p.identificadores.divisa = :#{#filtro.divisa})
  AND (:#{#filtro.rangoFechas.fechaInicio} IS NULL OR p.fechas.carga >= :#{#filtro.rangoFechas.fechaInicio})
  AND (:#{#filtro.rangoFechas.fechaFin}    IS NULL OR p.fechas.carga <= :#{#filtro.rangoFechas.fechaFin})
  AND (:#{#filtro.cuentaCargo} IS NULL OR p.ordenante.cuenta = :#{#filtro.cuentaCargo})
  AND (:#{#filtro.tipoPago} IS NULL OR p.identificadores.tipoPago = :#{#filtro.tipoPago})
  AND (:#{#filtro.estatus} IS NULL OR p.statusOper.id = :#{#filtro.estatus})
  AND (:#{#filtro.transactionId} IS NULL OR p.identificadores.transactionId = :#{#filtro.transactionId})
""";

    /**
     * Consulta de suma de importes por divisa en detalle filtrado.
     *
     * <p>
     * Calcula el total de importes por divisa aplicando los mismos filtros
     * que la consulta de detalle. Proporciona totales coherentes con los
     * datos mostrados en las tablas filtradas del frontend.
     * </p>
     *
     * <p>Utiliza el mismo objeto filtro que DETALLE_POR_FILTROS</p>
     */
    public static final String SUMA_IMPORTE_POR_DIVISA_DETALLE = """
SELECT new mx.santander.monitoreoapi.model.response.ImporteTotalDivisaDTO(
    p.identificadores.divisa,
    COALESCE(SUM(p.montos.importe),0)
)
FROM OperPagoEntity p
WHERE (:#{#filtro.operacion} IS NULL OR p.identificadores.tipoOperacion  = :#{#filtro.operacion})
  AND (:#{#filtro.cuentaAbono} IS NULL OR p.receptor.cuenta = :#{#filtro.cuentaAbono})
  AND (:#{#filtro.importe} IS NULL OR p.montos.importe = :#{#filtro.importe})
  AND (:#{#filtro.referenciaCanal} IS NULL OR p.identificadores.referenciaUnica = :#{#filtro.referenciaCanal})
  AND (:#{#filtro.divisa} IS NULL OR p.identificadores.divisa = :#{#filtro.divisa})
  AND (:#{#filtro.rangoFechas.fechaInicio} IS NULL OR p.fechas.carga >= :#{#filtro.rangoFechas.fechaInicio})
  AND (:#{#filtro.rangoFechas.fechaFin}    IS NULL OR p.fechas.carga <= :#{#filtro.rangoFechas.fechaFin})
  AND (:#{#filtro.cuentaCargo} IS NULL OR p.ordenante.cuenta = :#{#filtro.cuentaCargo})
  AND (:#{#filtro.tipoPago} IS NULL OR p.identificadores.tipoPago  = :#{#filtro.tipoPago})
  AND (:#{#filtro.estatus} IS NULL OR p.statusOper.id = :#{#filtro.estatus})
  AND (:#{#filtro.transactionId} IS NULL OR p.identificadores.transactionId = :#{#filtro.transactionId})
GROUP BY p.identificadores.divisa
""";


    // ===== CONSULTAS DE CATÁLOGOS PARA COMPONENTES UI =====

    /**
     * Consulta de tipos de operación activos disponibles.
     *
     * <p>
     * Obtiene todos los tipos de pago distintos que están activos (activo = 'Y')
     * en el sistema. Utilizada para poblar componentes select y combo boxes
     * en la interfaz de usuario con opciones válidas de tipo de operación.
     * </p>
     */
    public static final String OBTENER_OPERACIONES = """
        SELECT DISTINCT c.id.idTipoPago
        FROM CanalEntity c 
        WHERE c.activo = 'Y' 
        ORDER BY c.id.idTipoPago
    """;

    /**
     * Consulta de tipos de operación detallados.
     *
     * <p>
     * Obtiene los tipos de operación distintos (PARTICIPANTE-TERCERO,
     * ENTRE CUENTAS, etc.) que están activos. Utilizada para catálogos
     * de componentes de interfaz que requieren clasificación detallada.
     * </p>
     */
    public static final String OBTENER_TIPOS_OPERACION = """
        SELECT DISTINCT c.id.idTipoOper
        FROM CanalEntity c 
        WHERE c.activo = 'Y' 
        ORDER BY c.id.idTipoOper
    """;

    /**
     * Consulta de catálogo de divisas con formato para componentes UI.
     *
     * <p>
     * Obtiene todas las divisas activas formateadas como opciones de catálogo
     * con ID y descripción. Utilizada para llenar componentes select, combo
     * boxes y otros elementos de interfaz que requieren selección de divisa.
     * </p>
     */
    public static final String DIVISAS_OPCION_CATALOGO = """
        SELECT new mx.santander.monitoreoapi.model.response.OpcionCatalogo(
            d.idDiv,
            d.txtDiv
        )
        FROM DivisaEntity d
        ORDER BY d.idDiv
    """;

    /**
     * Consulta de divisas únicas presentes en operaciones.
     *
     * <p>
     * Obtiene todas las divisas distintas que aparecen en las operaciones
     * de pago registradas. Utilizada para crear filtros dinámicos y combos
     * basados únicamente en divisas que tienen operaciones asociadas.
     * </p>
     */
    public static final String OBTENER_DIVISAS_OPER = """
    SELECT DISTINCT o.identificadores.divisa
    FROM OperPagoEntity o
    ORDER BY o.identificadores.divisa
""";

    /**
     * Consulta de catálogo de estatus de operación.
     *
     * <p>
     * Obtiene todos los estatus posibles de operaciones formateados para
     * componentes de interfaz con ID y descripción. Utilizada en filtros,
     * combos y otros elementos UI que requieren selección de estatus.
     * </p>
     */
    public static final String STATUS_OPER_OPCION_CATALOGO = """
        SELECT new mx.santander.monitoreoapi.model.response.OpcionCatalogo(
            s.id,
            s.descripcion
        )
        FROM StatusOperEntity s
    """;

    /**
     * Consulta de catálogo de tipos de operación.
     *
     * <p>
     * Obtiene todos los tipos de operación disponibles formateados para
     * componentes de selección con ID y descripción textual ordenados
     * alfabéticamente para facilitar la navegación del usuario.
     * </p>
     */
    public static final String TIPO_OPERACION_OPCION_CATALOGO = """
        SELECT new mx.santander.monitoreoapi.model.response.OpcionCatalogo(
            t.idTipoOper,
            t.txtTipoOper
        )
        FROM TipoOperacionEntity t
        ORDER BY t.idTipoOper
    """;

    /**
     * Consulta de catálogo de tipos de pago.
     *
     * <p>
     * Obtiene todos los tipos de pago disponibles formateados para uso
     * en componentes de interfaz de usuario como select, combo boxes y
     * filtros, con ID y descripción ordenados alfabéticamente.
     * </p>
     */
    public static final String TIPO_PAGO_OPCION_CATALOGO = """
        SELECT new mx.santander.monitoreoapi.model.response.OpcionCatalogo(
            t.idTipoPago,
            t.txtTipoPago
        )
        FROM TipoPagoEntity t
        ORDER BY t.idTipoPago
    """;

    /**
     * Constructor privado para prevenir la instanciación de esta clase utilitaria.
     *
     * <p>
     * Esta clase contiene únicamente constantes estáticas y no debe ser instanciada.
     * El constructor privado garantiza que no se puedan crear objetos de esta clase,
     * siguiendo el patrón de clase utilitaria recomendado por las mejores prácticas Java.
     * </p>
     *
     * @throws IllegalStateException siempre, para prevenir cualquier intento de instanciación
     */
    private QueryConstants() {
        throw new IllegalStateException("Clase utilitaria de constantes - no debe ser instanciada");
    }
}