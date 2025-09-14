package mx.santander.h2h.monitoreoapi.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/", produces = MediaType.TEXT_PLAIN_VALUE)
public final class RootPingController {
  @GetMapping
  public ResponseEntity<String> root() { return ResponseEntity.ok("ok"); }
}
