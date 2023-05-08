package fss_server.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fss_server.file_access.userData;

@Configuration
public class dataConfig {

    @Bean
    public userData userData() {
        return new userData();
    }
}
