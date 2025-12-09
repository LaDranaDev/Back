--CATALOGO DE CLAVES DE TRANSFER
CREATE TABLE tsfcvetransfer
(
   cdtpctao  nvarchar(3),
   cdtpctad  nvarchar(3),
   cdtippag  nvarchar(3),
   cdmoneda  nvarchar(3),
   cdconcep  nvarchar(3),
   cdcvtran  nvarchar(5),
   cdcvoper  nvarchar(8),
   cdubiban  nvarchar(3)
)

CREATE UNIQUE INDEX ipk_sfcvetransf3
   ON tsfcvetransfer (cdtpctao, cdtpctad, cdtippag, cdmoneda, cdconcep, cdubiban);

---------------------------------------------------------------
---------------------------------------------------------------
---------------------------------------------------------------
--CATALOGO DE BANCOS
CREATE TABLE tsfbancos
(
   cdbanco   nvarchar(4)     NOT NULL,
   cdgruban  nvarchar(1),
   nbbanco   nvarchar(40),
   nubancif  nvarchar(40),
   nbdomici  nvarchar(40),
   cdpostal  nvarchar(6),
   nbplaza   nvarchar(40),
   nbplazav  nvarchar(40),
   cdprovin  nvarchar(2),
   cdpais    nvarchar(3),
   nutelprf  nvarchar(3),
   nutelefo  nvarchar(7),
   nutelext  nvarchar(7),
   nufax     nvarchar(9),
   cdaswift  char(12),
   mcsepa    smallint        NOT NULL,
   cdcveban  nchar(5),
   tpinterm  nchar(2),
   nucecobn  nchar(3),
   nbbancor  nvarchar(60),
   nbbanlar  nvarchar(255),
   fhalta    date,
   fhbaja    date,
   fhultmod  date,
   nubanxic  nchar(5),
   nblogin   nvarchar(10),
   fhultact  date
)

ALTER TABLE tsfbancos
   ADD CONSTRAINT PRIMARY KEY (cdbanco)
   CONSTRAINT pk_tsfbancos;

CREATE INDEX idx_tsfbancos_cdcveban
   ON tsfbancos (cdcveban);

