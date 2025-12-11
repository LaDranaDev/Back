CREATE TABLE DHI_MX_CTL_SECU_DIARIO (
    FCH_DIA  DATE    PRIMARY KEY,
    VAL_SECU NUMBER
);


FCH_DIA = día (TRUNC(SYSDATE))

VAL_SECU = último valor usado para ese día
(el primero será 60000; luego 60001, 60002, … y si pasa de 70000 sigue normal: 70001, 70002, etc.)


Asumo que en DHI_MX_AUX_PAGOS_CNF_GBL ya tienes la columna:

VAL_SECU  NUMBER


Si no, sería algo como:

ALTER TABLE DHI_MX_AUX_PAGOS_CNF_GBL
  ADD VAL_SECU NUMBER;


CREATE OR REPLACE TRIGGER TRG_DHI_INS_PAGOS_SECU_01
BEFORE INSERT ON DHI_MX_AUX_PAGOS_CNF_GBL
FOR EACH ROW
DECLARE
    v_valor  NUMBER;
BEGIN
    -- Intentamos obtener el valor del día actual
    BEGIN
        SELECT VAL_SECU
          INTO v_valor
          FROM DHI_MX_CTL_SECU_DIARIO
         WHERE FCH_DIA = TRUNC(SYSDATE)
         FOR UPDATE;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            -- Primer uso del día: insertamos el 60000
            BEGIN
                INSERT INTO DHI_MX_CTL_SECU_DIARIO (FCH_DIA, VAL_SECU)
                VALUES (TRUNC(SYSDATE), 60000);
                v_valor := 60000;
            EXCEPTION
                WHEN DUP_VAL_ON_INDEX THEN
                    -- Condición de carrera: otro hilo ya insertó el día,
                    -- lo volvemos a leer con FOR UPDATE
                    SELECT VAL_SECU
                      INTO v_valor
                      FROM DHI_MX_CTL_SECU_DIARIO
                     WHERE FCH_DIA = TRUNC(SYSDATE)
                     FOR UPDATE;
            END;
    END;

    -- Asignamos el valor actual al registro nuevo
    :NEW.VAL_SECU := v_valor;

    -- Incrementamos el valor para el siguiente insert del día
    UPDATE DHI_MX_CTL_SECU_DIARIO
       SET VAL_SECU = VAL_SECU + 1
     WHERE FCH_DIA = TRUNC(SYSDATE);

    -- NOTA:
    -- No ponemos tope: si pasa de 70000 (60000 + 10001 inserts)
    -- simplemente sigue a 70001, 70002, etc., como te pidieron.
END;
/




Qué hace exactamente

Cada día nuevo:

Primer insert del día → crea registro en DHI_MX_CTL_SECU_DIARIO con VAL_SECU = 60000.

Asigna 60000 a :NEW.VAL_SECU.

Actualiza la tabla a 60001 para el siguiente insert.

En el mismo día:

Lee el valor actual con SELECT ... FOR UPDATE (evita que dos sesiones usen el mismo número).

Lo asigna al nuevo registro.

Lo incrementa en la tabla de control.

Si se pasa de 70000:

No hay validación de tope.

El contador sigue: 70001, 70002, etc., tal como te dijeron (“si llega a rebasar, se continua”).
