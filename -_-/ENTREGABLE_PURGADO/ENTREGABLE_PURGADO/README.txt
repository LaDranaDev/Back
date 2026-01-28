ENTREGABLE - PURGADO CON PARAMETRIA
====================================
Fecha: 2026-01-22

CONTENIDO DE ESTA CARPETA:
--------------------------
1. SNTDR_DHI_BP_MOVIMIENTOS_TRY.bpml
   -> BP con 4 nuevos servicios (2 COUNT + 2 DELETE)

2. dhi_properties_pag.properties
   -> Properties con 4 nuevas queries

3. INSTRUCCIONES.sql
   -> INSERTs para DHI_MX_PRC_CONFIG + checklist


QUE SE AGREGO AL BP:
--------------------
- Count_Purgado_AUX_PAGOS
  Cuenta cuantos registros se van a purgar de DHI_MX_AUX_PAGOS_CNF_GBL

- Purgado_AUX_PAGOS_CNF_GBL
  Purga DHI_MX_AUX_PAGOS_CNF_GBL por campo FCH_REG

- Count_Purgado_RESP_PAGOS
  Cuenta cuantos registros se van a purgar de DHI_MX_AUX_RESP_PAGOS_CNF_GBL

- Purgado_AUX_RESP_PAGOS_CNF_GBL
  Purga DHI_MX_AUX_RESP_PAGOS_CNF_GBL por campo FCH_RESP


DONDE VER SI HABIA REGISTROS:
-----------------------------
En ProcessData despues de ejecutar:
- /ProcessData/PurgadoInfo/AUX_PAGOS_Registros   (0 = no habia nada)
- /ProcessData/PurgadoInfo/RESP_PAGOS_Registros  (0 = no habia nada)


PARAMETROS EN DHI_MX_PRC_CONFIG:
--------------------------------
TXT_NAME                              | TXT_PARAM
--------------------------------------|----------
DHI_CNF_PAG_TREA_PURGADO_AUX_PAGOS    | 90
DHI_CNF_PAG_TREA_PURGADO_RESP_PAGOS   | 90


ORDEN DE EJECUCION:
-------------------
1. Ejecutar INSERTs en BD (INSTRUCCIONES.sql)
2. Actualizar properties en servidor
3. Importar BP en Sterling
4. Probar y revisar ProcessData para ver conteos
