# Configuracion BP: SNTDR_DHI_BP_MOVIMIENTOS_TRY

## Estado Actual del Cascaron

| Servicio | Estado | Notas |
|----------|--------|-------|
| Timestamp Utility | CONFIGURADO | Formato YYYYMMdd |
| Mailbox Extract Begin | VACIO | Falta ruta mailbox |
| Mailbox Query | VACIO | Falta busqueda archivo _fin |
| Choice (Valida_Reprocesamiento) | PARCIAL | Condicion es "1=1" |
| Parametria (JDBC) | VACIO | Falta query |
| Extra_Movs (JDBC) | VACIO | Falta query |
| Mapas Translation | CONFIGURADO | Nombres correctos |
| Assign_OUT_ADD_MAILBOX | CONFIGURADO | 90 dias retencion |
| Invoke BP | CONFIGURADO | SNTDR_DHI_BP_MAILBOX_ADD |
| Mailbox Delete | VACIO | Falta configurar |

---

## 1. Mailbox Extract Begin Service

**Proposito:** Extraer el archivo de movimientos del mailbox de entrada

```xml
<output>
  <Oper_Assign_Activity>
    <field>to</field>
    <value>MailboxPath</value>
    <field>constant</field>
    <value>/NODS/confirmingGBL/contabilidad/input</value>
  </Oper_Assign_Activity>
  <Oper_Assign_Activity>
    <field>to</field>
    <value>MessageNamePattern</value>
    <field>from</field>
    <value>concat('confglobal_movimientos_', /ProcessData/Rone, '.txt')</value>
  </Oper_Assign_Activity>
  <Oper_Assign_Activity>
    <field>to</field>
    <value>ExtractableCount</value>
    <field>constant</field>
    <value>1</value>
  </Oper_Assign_Activity>
</output>
```

**Nota:** `Rone` viene del Timestamp Utility con formato ddMMyyyy (verificar si necesitas otro formato)

---

## 2. Mailbox Query Service

**Proposito:** Verificar si ya existe el archivo _fin.txt (control de reprocesamiento)

```xml
<output>
  <Oper_Assign_Activity>
    <field>to</field>
    <value>MailboxPath</value>
    <field>constant</field>
    <value>/NODS/confirmingGBL/contabilidad/his</value>
  </Oper_Assign_Activity>
  <Oper_Assign_Activity>
    <field>to</field>
    <value>MessageNamePattern</value>
    <field>from</field>
    <value>concat('confglobal_movimientos_', /ProcessData/Rone, '_fin.txt')</value>
  </Oper_Assign_Activity>
</output>
```

**Resultado esperado:**
- Si encuentra archivo -> `/ProcessData/MailboxQueryResults/TotalMessages` > 0
- Si NO encuentra -> `/ProcessData/MailboxQueryResults/TotalMessages` = 0

---

## 3. Choice - Condicion de Reprocesamiento

**Condicion actual:** `1=1` (siempre true - NO SIRVE)

**Condicion correcta:**
```
number(/ProcessData/MailboxQueryResults/TotalMessages) = 0
```

**Logica:**
- Si TotalMessages = 0 (NO existe _fin.txt) -> Continua procesamiento
- Si TotalMessages > 0 (SI existe _fin.txt) -> Termina (ya se proceso)

---

## 4. Parametria - JDBC Query (Obtener configuracion)

**Proposito:** Obtener parametros de configuracion de la tabla DHI_MX_PRC_CONFIG

```sql
-- PENDIENTE: Confirmar estructura de tabla DHI_MX_PRC_CONFIG
-- Ejemplo de query:
SELECT
    PARAM_NAME,
    PARAM_VALUE
FROM DHI_MX_PRC_CONFIG
WHERE PROCESO = 'MOVIMIENTOS_TRY'
  AND ACTIVO = 'S'
```

**Configuracion JDBC:**
```xml
<output>
  <Oper_Assign_Activity>
    <field>to</field>
    <value>pool</value>
    <field>constant</field>
    <value>oraclePool</value>  <!-- PENDIENTE: Confirmar nombre del pool -->
  </Oper_Assign_Activity>
  <Oper_Assign_Activity>
    <field>to</field>
    <value>query_type</value>
    <field>constant</field>
    <value>SELECT</value>
  </Oper_Assign_Activity>
  <Oper_Assign_Activity>
    <field>to</field>
    <value>result_name</value>
    <field>constant</field>
    <value>ConfigParams</value>
  </Oper_Assign_Activity>
  <Oper_Assign_Activity>
    <field>to</field>
    <value>sql</value>
    <field>constant</field>
    <value><!-- QUERY AQUI --></value>
  </Oper_Assign_Activity>
</output>
```

---

## 5. Extra_Movs - JDBC Query (Movimientos confirmados)

**Proposito:** Extraer movimientos con estatus confirmado del dia

```sql
-- Query para extraer movimientos confirmados por Transfer
SELECT
    COD_EMP,
    VAL_INTER,
    FH_INTER,
    COD_PROD,
    COD_SUB_PROD,
    COD_APUNTE,
    COD_CONT,
    COD_DIVI,
    VAL_CNTA_CONT,
    VAL_AUTO,
    VAL_CENT_ORIGN,
    VAL_CENT_DESTN,
    VAL_MOVI_DEBE,
    VAL_MOV_HABER,
    IMP_DEBE,
    IMP_HABER,
    VAL_NUM_CONT,
    VAL_CLV_CONCP,
    VAL_DES_CLV_CONCP,
    ID_REG_API,
    COD_SANCTCCC,
    COD_APLI_ORG
FROM [TABLA_ORIGEN]  -- PENDIENTE: Confirmar nombre tabla origen
WHERE CLAVE_ESTATUS = 'CO'
  AND TRUNC(FECHA_RESPUESTA) = TRUNC(SYSDATE)
```

**PENDIENTE:** Confirmar:
- Nombre de la tabla origen de donde se extraen los movimientos
- Nombres exactos de las columnas CLAVE_ESTATUS y FECHA_RESPUESTA

---

## 6. Query de PURGADO (>90 dias)

**Proposito:** Eliminar registros antiguos de la tabla auxiliar

```sql
-- Query DELETE para purgado de registros mayores a 90 dias
DELETE FROM DHI_MX_AUX_MOVS_PAGOS_CNF_GBL
WHERE FH_INTER < TRUNC(SYSDATE) - 90
```

**Alternativa con logging:**
```sql
-- Si necesitas saber cuantos registros se eliminaron
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM DHI_MX_AUX_MOVS_PAGOS_CNF_GBL
    WHERE FH_INTER < TRUNC(SYSDATE) - 90;

    DELETE FROM DHI_MX_AUX_MOVS_PAGOS_CNF_GBL
    WHERE FH_INTER < TRUNC(SYSDATE) - 90;

    COMMIT;

    -- v_count contiene el numero de registros eliminados
END;
```

**Nota:** El diagrama menciona 10 dias de backup para la tabla, pero el TXT dice 90 dias.
**CONFIRMAR cual es el correcto.**

---

## 7. Mailbox Delete Service

**Proposito:** Eliminar archivo original despues de respaldarlo

```xml
<output>
  <Oper_Assign_Activity>
    <field>to</field>
    <value>MailboxPath</value>
    <field>from</field>
    <value>/ProcessData/FileToProcess/MailboxPath</value>
  </Oper_Assign_Activity>
  <Oper_Assign_Activity>
    <field>to</field>
    <value>MessageId</value>
    <field>from</field>
    <value>/ProcessData/FileToProcess/MessageId</value>
  </Oper_Assign_Activity>
</output>
```

---

## 8. Configuracion de Rutas (Assign Services)

### 8.1 Backup archivo origen (_org.txt)
```
Ruta: /NODS/confirmingGBL/contabilidad/his
Nombre: confglobal_movimientos_ddmmyyyy_org.txt
Retencion: 5 dias
```

### 8.2 Archivo final concatenado
```
Ruta: /NODS/confirmingGBL/contabilidad/input
Nombre: confglobal_movimientos_ddmmyyyy.txt
Retencion: 1 dia
```

### 8.3 Backup archivo final (_fin.txt)
```
Ruta: /NODS/confirmingGBL/contabilidad/his
Nombre: confglobal_movimientos_ddmmyyyy_fin.txt
Retencion: 5 dias
```

---

## ITEMS PENDIENTES - PREGUNTAR A COMPANEROS

### Alta Prioridad:
1. **Nombre del pool JDBC Oracle** - Cual es el nombre del connection pool?
2. **Tabla origen de movimientos** - De donde se extraen los movimientos con CLAVE_ESTATUS = 'CO'?
3. **Estructura tabla DHI_MX_PRC_CONFIG** - Columnas y valores esperados

### Media Prioridad:
4. **Purgado 10 o 90 dias?** - El diagrama dice 10, el TXT dice 90
5. **Formato fecha en nombre archivo** - ddMMyyyy o yyyyMMdd?
6. **Virtual Root** - Confirmar si es /NODS o hay otro

### Baja Prioridad:
7. **Routing Rule SNTDR_DHI_RR_01** - Que modificaciones necesita?
8. **Properties file** - Contenido de dhi_properties_pag.properties

---

## CREATE TABLE (Script para crear tabla auxiliar)

```sql
CREATE TABLE DHI_MX_AUX_MOVS_PAGOS_CNF_GBL (
    COD_EMP           VARCHAR2(10),
    VAL_INTER         VARCHAR2(50),
    FH_INTER          DATE,
    COD_PROD          VARCHAR2(10),
    COD_SUB_PROD      VARCHAR2(10),
    COD_APUNTE        VARCHAR2(20),
    COD_CONT          VARCHAR2(20),
    COD_DIVI          VARCHAR2(5),
    VAL_CNTA_CONT     VARCHAR2(30),
    VAL_AUTO          VARCHAR2(20),
    VAL_CENT_ORIGN    VARCHAR2(20),
    VAL_CENT_DESTN    VARCHAR2(20),
    VAL_MOVI_DEBE     VARCHAR2(1),
    VAL_MOV_HABER     VARCHAR2(1),
    IMP_DEBE          NUMBER(18,2),
    IMP_HABER         NUMBER(18,2),
    VAL_NUM_CONT      VARCHAR2(30),
    VAL_CLV_CONCP     VARCHAR2(20),
    VAL_DES_CLV_CONCP VARCHAR2(100),
    ID_REG_API        VARCHAR2(50),
    COD_SANCTCCC      VARCHAR2(20),
    COD_APLI_ORG      VARCHAR2(20)
);

-- Index para mejorar performance del purgado
CREATE INDEX IDX_DHI_MOVS_PAGOS_FH_INTER ON DHI_MX_AUX_MOVS_PAGOS_CNF_GBL(FH_INTER);

-- Comentario de tabla
COMMENT ON TABLE DHI_MX_AUX_MOVS_PAGOS_CNF_GBL IS 'Tabla auxiliar para movimientos de neutralizacion CNF Global';
```

**NOTA:** Los tipos de datos son estimados. Confirmar con el equipo de BD.

---

## Flujo Visual del BP

```
START
  |
  v
[Timestamp Utility] --> Obtiene fecha YYYYMMdd
  |
  v
[Mailbox Extract Begin] --> Extrae confglobal_movimientos_ddmmyyyy.txt
  |
  v
[Mailbox Query] --> Busca archivo _fin.txt en /his
  |
  v
<Choice: Existe _fin?>
  |
  |--[SI]--> END (ya proceso)
  |
  |--[NO]
      |
      v
    [Assign Backup Origen] --> Prepara ruta /his/*_org.txt
      |
      v
    [Invoke MAILBOX_ADD] --> Respalda archivo original
      |
      v
    [Mailbox Delete] --> Borra archivo de /input
      |
      v
    [Parametria JDBC] --> Obtiene config de DHI_MX_PRC_CONFIG
      |
      v
    [Extra_Movs JDBC] --> SELECT movimientos WHERE estatus='CO'
      |
      v
    [MAP_MOVIMIENTO_PAGOS_TRY] --> Transforma datos
      |
      v
    [MAP_GENERA_INTER_PAGOS_TRY] --> Genera interface
      |
      v
    [MERGE] --> Concatena interface original + neutralizacion
      |
      v
    [Purgado JDBC] --> DELETE WHERE FH_INTER < SYSDATE-90
      |
      v
    [Assign Final] --> Prepara ruta /input (1 dia)
      |
      v
    [Invoke MAILBOX_ADD] --> Deposita archivo concatenado
      |
      v
    [Assign Backup Fin] --> Prepara ruta /his/*_fin.txt (5 dias)
      |
      v
    [Invoke MAILBOX_ADD] --> Respalda archivo final
      |
      v
    END
```
