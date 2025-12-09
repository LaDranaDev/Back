-- CATALOGO DE CLAVES DE TRANSFER
CREATE TABLE TSF_MX_MAE_CVE_TRANSFER (
    COD_TP_CTA_ORG   NVARCHAR(3),  -- cdtpctao
    COD_TP_CTA_DES   NVARCHAR(3),  -- cdtpctad
    COD_TP_PAGO      NVARCHAR(3),  -- cdtippag
    COD_MONEDA       NVARCHAR(3),  -- cdmoneda
    COD_CONCEPTO     NVARCHAR(3),  -- cdconcep
    COD_CVE_TRANSF   NVARCHAR(5),  -- cdcvtran
    COD_CVE_OPER     NVARCHAR(8),  -- cdcvoper
    COD_UBI_IBAN     NVARCHAR(3)   -- cdubiban
);

CREATE UNIQUE INDEX IDX_TSF_TRF_CVE_UN_01
    ON TSF_MX_MAE_CVE_TRANSFER (
        COD_TP_CTA_ORG,
        COD_TP_CTA_DES,
        COD_TP_PAGO,
        COD_MONEDA,
        COD_CONCEPTO,
        COD_UBI_IBAN
    );



-- CATALOGO DE BANCOS
CREATE TABLE TSF_MX_MAE_BANCOS (
    COD_BANCO_PK       NVARCHAR(4)   NOT NULL,  -- cdbanco
    FLG_GRUPO_BAN      NVARCHAR(1),            -- cdgruban
    TXT_NOM_BANCO      NVARCHAR(40),           -- nbbanco
    TXT_NOM_BANCIF     NVARCHAR(40),           -- nubancif
    TXT_DOMICILIO      NVARCHAR(40),           -- nbdomici
    COD_CP_POSTAL      NVARCHAR(6),            -- cdpostal
    TXT_NOM_PLAZA      NVARCHAR(40),           -- nbplaza
    TXT_NOM_PLAZA_V    NVARCHAR(40),           -- nbplazav
    COD_PROVINCIA      NVARCHAR(2),            -- cdprovin
    COD_PAIS           NVARCHAR(3),            -- cdpais
    NUM_TEL_PREFIJO    NVARCHAR(3),            -- nutelprf
    NUM_TELEFONO       NVARCHAR(7),            -- nutelefo
    NUM_TEL_EXT        NVARCHAR(7),            -- nutelext
    NUM_FAX            NVARCHAR(9),            -- nufax
    COD_SWIFT          CHAR(12),               -- cdaswift
    FLG_MCSEPA         SMALLINT      NOT NULL, -- mcsepa
    COD_CVE_BAN        NCHAR(5),               -- cdcveban
    COD_TP_INTERM      NCHAR(2),               -- tpinterm
    NUM_CEC_BN         NCHAR(3),               -- nucecobn
    TXT_NOM_BANCOR     NVARCHAR(60),           -- nbbancor
    TXT_NOM_BANLARGO   NVARCHAR(255),          -- nbbanlar
    FCH_ALTA           DATE,                   -- fhalta
    FCH_BAJA           DATE,                   -- fhbaja
    FCH_ULT_MOD        DATE,                   -- fhultmod
    NUM_BANXIC         NCHAR(5),               -- nubanxic
    TXT_LOGIN          NVARCHAR(10),           -- nblogin
    FCH_ULT_ACT        DATE                    -- fhultact
);

ALTER TABLE TSF_MX_MAE_BANCOS
    ADD CONSTRAINT PK_TSF_MX_MAE_BANCOS
        PRIMARY KEY (COD_BANCO_PK);

CREATE INDEX IDX_TSF_BAN_CVE_CO_01
    ON TSF_MX_MAE_BANCOS (COD_CVE_BAN);
