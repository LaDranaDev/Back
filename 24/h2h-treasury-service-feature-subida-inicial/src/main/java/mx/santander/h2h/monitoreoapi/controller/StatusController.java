package mx.santander.h2h.monitoreoapi.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
@RequestMapping(path = "/api/status", produces = MediaType.TEXT_PLAIN_VALUE)
public final class StatusController {
  @GetMapping
  public ResponseEntity<String> status() { return ResponseEntity.ok("ok"); }
}
