package mx.santander.h2h.monitoreoapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
/*proyecto
 * apis
 * version 1
 * clase
 * principal
 * del 
 * proyecto
 * tipo spring
 * boot
 * */
@SpringBootApplication
@ComponentScan({ "mx.santandertec.*", "mx.santander.h2h.monitoreoapi", "mx.santander" })
public class H2hTreasuryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(H2hTreasuryServiceApplication.class, args);
	}

}
