-- ============================================================
-- INSTRUCCIONES PARA IMPLEMENTAR PURGADO CON PARAMETRIA
-- BP: SNTDR_DHI_BP_MOVIMIENTOS_TRY
-- Fecha: 2026-01-22
-- ============================================================

-- ============================================================
-- PASO 1: EJECUTAR INSERTS EN DHI_MX_PRC_CONFIG
-- ============================================================
-- Estos parametros definen los dias de purgado para cada tabla
-- El valor '90' se puede cambiar segun necesidades

-- Parametro para purgado de DHI_MX_AUX_MOVS_PAGOS_CNF_GBL (por FH_OPER) - EL ORIGINAL
INSERT INTO DHI_MX_PRC_CONFIG (ID_CONFIG, TXT_NAME, TXT_PARAM, FCH_REGISTRO, TXT_LOGIN, FCH_ACTUALIZA, TXT_ACTIVO)
VALUES (
    (SELECT MAX(ID_CONFIG)+1 FROM DHI_MX_PRC_CONFIG),
    'DHI_CNF_PAG_TREA_PURGADO_MOVS',
    '90',
    SYSDATE,
    'LOGIN',
    TRUNC(SYSDATE),
    '1'
);

-- Parametro para purgado de DHI_MX_AUX_PAGOS_CNF_GBL (por FCH_REG)
INSERT INTO DHI_MX_PRC_CONFIG (ID_CONFIG, TXT_NAME, TXT_PARAM, FCH_REGISTRO, TXT_LOGIN, FCH_ACTUALIZA, TXT_ACTIVO)
VALUES (
    (SELECT MAX(ID_CONFIG)+1 FROM DHI_MX_PRC_CONFIG),
    'DHI_CNF_PAG_TREA_PURGADO_AUX_PAGOS',
    '90',
    SYSDATE,
    'LOGIN',
    TRUNC(SYSDATE),
    '1'
);

-- Parametro para purgado de DHI_MX_AUX_RESP_PAGOS_CNF_GBL (por FCH_RESP)
INSERT INTO DHI_MX_PRC_CONFIG (ID_CONFIG, TXT_NAME, TXT_PARAM, FCH_REGISTRO, TXT_LOGIN, FCH_ACTUALIZA, TXT_ACTIVO)
VALUES (
    (SELECT MAX(ID_CONFIG)+1 FROM DHI_MX_PRC_CONFIG),
    'DHI_CNF_PAG_TREA_PURGADO_RESP_PAGOS',
    '90',
    SYSDATE,
    'LOGIN',
    TRUNC(SYSDATE),
    '1'
);

COMMIT;

-- ============================================================
-- PASO 2: VERIFICAR QUE SE INSERTARON
-- ============================================================
SELECT TXT_NAME, TXT_PARAM, TXT_ACTIVO
FROM DHI_MX_PRC_CONFIG
WHERE TXT_NAME LIKE 'DHI_CNF_PAG_TREA_PURGADO%';

-- Resultado esperado:
-- TXT_NAME                              | TXT_PARAM | TXT_ACTIVO
-- DHI_CNF_PAG_TREA_PURGADO_MOVS         | 90        | 1
-- DHI_CNF_PAG_TREA_PURGADO_AUX_PAGOS    | 90        | 1
-- DHI_CNF_PAG_TREA_PURGADO_RESP_PAGOS   | 90        | 1


-- ============================================================
-- PASO 3: ACTUALIZAR PROPERTIES FILE
-- ============================================================
-- Agregar/modificar estas 6 lineas al archivo dhi_properties_pag.properties:
--
-- SNTDR_DHI_BP_MOVIMIENTOS_TRY.Count_MOVS=SELECT COUNT(*) AS TOTAL FROM DHI_MX_AUX_MOVS_PAGOS_CNF_GBL WHERE to_date(FH_OPER,'YYYYMMDD') < TRUNC(SYSDATE) -
-- SNTDR_DHI_BP_MOVIMIENTOS_TRY.Purgado=DELETE FROM DHI_MX_AUX_MOVS_PAGOS_CNF_GBL WHERE to_date(FH_OPER,'YYYYMMDD') < TRUNC(SYSDATE) -
-- SNTDR_DHI_BP_MOVIMIENTOS_TRY.Count_AUX_PAGOS=SELECT COUNT(*) AS TOTAL FROM DHI_MX_AUX_PAGOS_CNF_GBL WHERE FCH_REG < TRUNC(SYSDATE) -
-- SNTDR_DHI_BP_MOVIMIENTOS_TRY.Purgado_AUX_PAGOS=DELETE FROM DHI_MX_AUX_PAGOS_CNF_GBL WHERE FCH_REG < TRUNC(SYSDATE) -
-- SNTDR_DHI_BP_MOVIMIENTOS_TRY.Count_RESP_PAGOS=SELECT COUNT(*) AS TOTAL FROM DHI_MX_AUX_RESP_PAGOS_CNF_GBL WHERE FCH_RESP < TRUNC(SYSDATE) -
-- SNTDR_DHI_BP_MOVIMIENTOS_TRY.Purgado_RESP_PAGOS=DELETE FROM DHI_MX_AUX_RESP_PAGOS_CNF_GBL WHERE FCH_RESP < TRUNC(SYSDATE) -
--
-- NOTA: Ya estan agregadas en el archivo dhi_properties_pag.properties de esta carpeta
-- IMPORTANTE: La propertie Purgado ya existia pero se le quito el 90 hardcodeado


-- ============================================================
-- PASO 4: IMPORTAR BP EN STERLING
-- ============================================================
-- El archivo SNTDR_DHI_BP_MOVIMIENTOS_TRY.bpml tiene 6 servicios
-- de purgado (3 COUNT + 3 DELETE):
--   - Count_Purgado_MOVS             (cuenta registros de DHI_MX_AUX_MOVS_PAGOS_CNF_GBL)
--   - Purgado_Tabla_Auxiliar         (ejecuta DELETE de DHI_MX_AUX_MOVS_PAGOS_CNF_GBL)
--   - Count_Purgado_AUX_PAGOS        (cuenta registros de DHI_MX_AUX_PAGOS_CNF_GBL)
--   - Purgado_AUX_PAGOS_CNF_GBL      (ejecuta DELETE de DHI_MX_AUX_PAGOS_CNF_GBL)
--   - Count_Purgado_RESP_PAGOS       (cuenta registros de DHI_MX_AUX_RESP_PAGOS_CNF_GBL)
--   - Purgado_AUX_RESP_PAGOS_CNF_GBL (ejecuta DELETE de DHI_MX_AUX_RESP_PAGOS_CNF_GBL)


-- ============================================================
-- COMO FUNCIONA
-- ============================================================
-- 1. El BP ejecuta la operacion "Parametros" que carga todos los
--    parametros con prefijo DHI_CNF_PAG_TREA_% en ProcessData
--
-- 2. Antes de cada DELETE, se ejecuta un COUNT que guarda en ProcessData
--    cuantos registros se van a purgar:
--    /ProcessData/PurgadoInfo/MOVS_Registros
--    /ProcessData/PurgadoInfo/AUX_PAGOS_Registros
--    /ProcessData/PurgadoInfo/RESP_PAGOS_Registros
--
-- 3. Los servicios de purgado usan concat() para armar el SQL:
--    concat(sci-get-property('dhi_properties_pag','...Purgado'),
--           ' ',
--           /ProcessData/Info/Params/Param[NAME='DHI_CNF_PAG_TREA_PURGADO_MOVS']/VAL/text())
--
-- 4. Resultado final del SQL:
--    DELETE FROM DHI_MX_AUX_MOVS_PAGOS_CNF_GBL WHERE to_date(FH_OPER,'YYYYMMDD') < TRUNC(SYSDATE) - 90
--
-- 5. Si quieres cambiar los dias, solo modifica TXT_PARAM en la tabla:
--    UPDATE DHI_MX_PRC_CONFIG SET TXT_PARAM = '60'
--    WHERE TXT_NAME = 'DHI_CNF_PAG_TREA_PURGADO_MOVS';


-- ============================================================
-- QUE VER EN PROCESSDATA DESPUES DE EJECUTAR
-- ============================================================
-- /ProcessData/PurgadoInfo/MOVS_Registros       -> Registros purgados de DHI_MX_AUX_MOVS_PAGOS_CNF_GBL
-- /ProcessData/PurgadoInfo/AUX_PAGOS_Registros  -> Registros purgados de DHI_MX_AUX_PAGOS_CNF_GBL
-- /ProcessData/PurgadoInfo/RESP_PAGOS_Registros -> Registros purgados de DHI_MX_AUX_RESP_PAGOS_CNF_GBL
--
-- Si dice 0, no habia registros para purgar (mayor a 90 dias)
-- Si dice un numero, esos fueron los registros eliminados


-- ============================================================
-- CHECKLIST ANTES DE PROBAR
-- ============================================================
-- [ ] 3 INSERTs ejecutados en DHI_MX_PRC_CONFIG (MOVS, AUX_PAGOS, RESP_PAGOS)
-- [ ] Verificar con SELECT que existen los 3 parametros
-- [ ] Properties file actualizado con las 6 lineas (3 COUNT + 3 DELETE)
-- [ ] BP importado en Sterling
-- [ ] Probar ejecucion del BP
-- [ ] Verificar en ProcessData los 3 valores de PurgadoInfo
-- [ ] Verificar en ProcessData los valores de PurgadoInfo
