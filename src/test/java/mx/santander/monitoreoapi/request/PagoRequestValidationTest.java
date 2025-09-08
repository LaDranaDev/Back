package mx.santander.monitoreoapi.request;

import mx.santander.monitoreoapi.model.request.PagoRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PagoRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void valido_ok() {
        PagoRequest r = new PagoRequest();
        r.setOperacion("SPEI");
        r.setDivisa("MXN");
        r.setTipoPago("SPEI");
        r.setEstatus("EN");
        r.setReferenciaCanal("REF-001");
        r.setImporte(new BigDecimal("10.00"));
        r.setTipoOperacion("PAGO");

        PagoRequest.RangoFechas rf = new PagoRequest.RangoFechas();
        r.setRangoFechas(rf);

        PagoRequest.Cuentas ctas = new PagoRequest.Cuentas();
        ctas.setCuentaCargo("123456789012");
        ctas.setCuentaAbono("987654321098");
        r.setCuentas(ctas);

        PagoRequest.Identificadores ids = new PagoRequest.Identificadores();
        ids.setCanal("PORTAL");
        ids.setTransactionId("1234567890");
        r.setIdentificadores(ids);

        Set<ConstraintViolation<PagoRequest>> v = validator.validate(r);
        assertThat(v).isEmpty();
    }

    @Test
    void divisa_invalida_falla() {
        PagoRequest r = new PagoRequest();
        r.setDivisa("MXXN"); // más de 3
        Set<ConstraintViolation<PagoRequest>> v = validator.validate(r);
        assertThat(v).anyMatch(cv -> cv.getPropertyPath().toString().equals("divisa"));
    }

    @Test
    void cuentas_formato_invalido_falla() {
        PagoRequest r = new PagoRequest();
        PagoRequest.Cuentas ctas = new PagoRequest.Cuentas();
        ctas.setCuentaCargo("ABC"); // no numérica
        r.setCuentas(ctas);
        Set<ConstraintViolation<PagoRequest>> v = validator.validate(r);
        assertThat(v).anyMatch(cv -> cv.getPropertyPath().toString().contains("cuentas"));
    }

    @Test
    void importe_soloDosDecimales_y_noNegativo() {
        PagoRequest r = new PagoRequest();
        r.setImporte(new BigDecimal("12.345")); // 3 decimales
        Set<ConstraintViolation<PagoRequest>> v = validator.validate(r);
        assertThat(v).isNotEmpty();

        r.setImporte(new BigDecimal("-1.00")); // negativo
        v = validator.validate(r);
        assertThat(v).isNotEmpty();
    }
}
