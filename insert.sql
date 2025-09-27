 Preparar la sesión (opcional pero recomendado)

SET AUTOCOMMIT OFF;                 -- Asegura que nada se confirme solo
SAVEPOINT before_test;              -- Punto de retorno rápido
2. Ejecuta las inserciones en este orden

-- a) tipoPago/tipoOper nulos
INSERT INTO API_MX_MAE_OPER_PAGO (
    ID_REFE_UNICO, TXT_CANAL,
    ID_TIPO_PAGO, ID_TIPO_OPER,
    ID_STATUS_OPER_FK, FLG_ENVIO,
    FCH_ENVIO, FCH_RESP_APP,
    NUM_REFER, TXT_CTA_ORDEN, TXT_CTA_RECEP,
    IMP, ID_DIV, TXT_TIPO_PRODUCT
) VALUES (
    'REF_NULL_PAGO_OPER', 'PORTAL EMP',
    NULL, NULL,
    'PR', 'Y',
    SYSDATE, SYSDATE,
    600001, '1111222233334444', '9999000011112222',
    150.00, 'MXN', 'SPEI'
);

-- b) tipoProducto nulo
INSERT INTO API_MX_MAE_OPER_PAGO (
    ID_REFE_UNICO, TXT_CANAL,
    ID_TIPO_PAGO, ID_TIPO_OPER,
    ID_STATUS_OPER_FK, FLG_ENVIO,
    FCH_ENVIO, FCH_RESP_APP,
    NUM_REFER, TXT_CTA_ORDEN, TXT_CTA_RECEP,
    IMP, ID_DIV, TXT_TIPO_PRODUCT
) VALUES (
    'REF_NULL_PRODUCT', 'PORTAL EMP',
    'SPEI', 'TT',
    'PR', 'Y',
    SYSDATE, SYSDATE,
    600002, '5555666677778888', '3333444455556666',
    200.00, 'MXN', NULL
);

-- c) cuentas vacías/nulas
INSERT INTO API_MX_MAE_OPER_PAGO (
    ID_REFE_UNICO, TXT_CANAL,
    ID_TIPO_PAGO, ID_TIPO_OPER,
    ID_STATUS_OPER_FK, FLG_ENVIO,
    FCH_ENVIO, FCH_RESP_APP,
    NUM_REFER, TXT_CTA_ORDEN, TXT_CTA_RECEP,
    IMP, ID_DIV, TXT_TIPO_PRODUCT
) VALUES (
    'REF_BLANK_CTA', 'PORTAL EMP',
    'SPEI', 'TT',
    'PR', 'Y',
    SYSDATE, SYSDATE,
    600003, ' ', NULL,
    250.00, 'MXN', 'SPEI'
);

-- d) importe nulo
INSERT INTO API_MX_MAE_OPER_PAGO (
    ID_REFE_UNICO, TXT_CANAL,
    ID_TIPO_PAGO, ID_TIPO_OPER,
    ID_STATUS_OPER_FK, FLG_ENVIO,
    FCH_ENVIO, FCH_RESP_APP,
    NUM_REFER, TXT_CTA_ORDEN, TXT_CTA_RECEP,
    IMP, ID_DIV, TXT_TIPO_PRODUCT
) VALUES (
    'REF_NULL_IMP', 'PORTAL EMP',
    'SPEI', 'TT',
    'PR', 'Y',
    SYSDATE, SYSDATE,
    600004, '7777888899990000', '0000999988887777',
    NULL, 'MXN', 'SPEI'
);
3. Haz tus pruebas (curl, selects, etc.)

-- Ejemplo de verificación
SELECT ID_REFE_UNICO, ID_TIPO_PAGO, ID_TIPO_OPER, ID_DIV, IMP
FROM   API_MX_MAE_OPER_PAGO
WHERE  ID_REFE_UNICO LIKE 'REF_%';
4. Rollback para dejar la base limpia

ROLLBACK TO before_test;  -- regresa justo antes de las pruebas
-- o si no usaste SAVEPOINT:
ROLLBACK;             
