ENTREGABLE - PURGADO CON PARAMETRIA
====================================
Fecha: 2026-01-23

CONTENIDO DE ESTA CARPETA:
--------------------------
1. SNTDR_DHI_BP_MOVIMIENTOS_TRY.bpml
   -> BP con 6 servicios de purgado (3 COUNT + 3 DELETE)

2. dhi_properties_pag.properties
   -> Properties con 6 queries (3 COUNT + 3 DELETE)

3. INSTRUCCIONES.sql
   -> 3 INSERTs para DHI_MX_PRC_CONFIG + checklist


QUE SE AGREGO/MODIFICO AL BP:
-----------------------------
PURGADO 1 - DHI_MX_AUX_MOVS_PAGOS_CNF_GBL (EL ORIGINAL, AHORA CON PARAMETRIA)
- Count_Purgado_MOVS         -> Cuenta registros a purgar
- Purgado_Tabla_Auxiliar     -> Ejecuta DELETE por FH_OPER

PURGADO 2 - DHI_MX_AUX_PAGOS_CNF_GBL (NUEVO)
- Count_Purgado_AUX_PAGOS    -> Cuenta registros a purgar
- Purgado_AUX_PAGOS_CNF_GBL  -> Ejecuta DELETE por FCH_REG

PURGADO 3 - DHI_MX_AUX_RESP_PAGOS_CNF_GBL (NUEVO)
- Count_Purgado_RESP_PAGOS       -> Cuenta registros a purgar
- Purgado_AUX_RESP_PAGOS_CNF_GBL -> Ejecuta DELETE por FCH_RESP


DONDE VER SI HABIA REGISTROS:
-----------------------------
En ProcessData despues de ejecutar:
- /ProcessData/PurgadoInfo/MOVS_Registros        (0 = no habia nada)
- /ProcessData/PurgadoInfo/AUX_PAGOS_Registros   (0 = no habia nada)
- /ProcessData/PurgadoInfo/RESP_PAGOS_Registros  (0 = no habia nada)


3 PARAMETROS EN DHI_MX_PRC_CONFIG:
----------------------------------
TXT_NAME                              | TXT_PARAM
--------------------------------------|----------
DHI_CNF_PAG_TREA_PURGADO_MOVS         | 90
DHI_CNF_PAG_TREA_PURGADO_AUX_PAGOS    | 90
DHI_CNF_PAG_TREA_PURGADO_RESP_PAGOS   | 90


ORDEN DE EJECUCION:
-------------------
1. Ejecutar 3 INSERTs en BD (INSTRUCCIONES.sql)
2. Actualizar properties en servidor
3. Importar BP en Sterling
4. Probar y revisar ProcessData para ver conteos
