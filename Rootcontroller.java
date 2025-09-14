package mx.santander.h2h.monitoreoapi.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public final class RootController {

    private static final String OK = "ok";

    // GET /
    @GetMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> rootGet() {
        return ResponseEntity.ok(OK);
    }

    // HEAD /  (nota: Spring resuelve HEAD de un GET automáticamente; este método puedes omitirlo)
    @RequestMapping(value = "/", method = RequestMethod.HEAD)
    public ResponseEntity<Void> rootHead() {
        return ResponseEntity.ok().build();
    }
}