package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

//@RefreshScope // seems not work to add refresh scope here
// update: no need to add RefreshScope here, onApplicationEvent in EurekaServerAutoConfiguration already do this
// only listen eureka.client.region / eureka.client.service-url.* / eureka.client.availability-zones.* change event to refresh peer list
@EnableEurekaServer
@SpringBootApplication
public class EurekaServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(EurekaServiceApplication.class, args);
    }
}
