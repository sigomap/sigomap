package ci.dgmp.sigomap;

import ci.dgmp.sigomap.authmodule.controller.services.spec.IJwtService;
import ci.dgmp.sigomap.modulelog.controller.service.AuditorAwareImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication @EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class SigomapApplication
{
    @Bean
    public AuditorAware<String> auditorProvider(IJwtService jwtService)
    {
        return new AuditorAwareImpl(jwtService);

    }
    public static void main(String[] args) {
        SpringApplication.run(SigomapApplication.class, args);
    }
}
