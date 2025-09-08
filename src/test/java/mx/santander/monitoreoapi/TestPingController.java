package mx.santander.monitoreoapi;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/*clase
* uteleria
* para
* cubrir
* los
* test
* con
* la
* forma
* correcta
* del
* fortify
* y
* sonar*/
@RestController
public class TestPingController {

    @GetMapping("/ping")
    ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

    @PostMapping("/echo")
    ResponseEntity<String> echo(@RequestBody(required = false) String body) {
        return ResponseEntity.ok(body == null ? "" : body);
    }
}
