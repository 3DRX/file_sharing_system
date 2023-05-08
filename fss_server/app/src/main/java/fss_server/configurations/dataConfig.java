package fss_server.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fss_server.file_access.UserData;

@Configuration
public class dataConfig {

    @Bean
    public UserData userData() {
        return new UserData();
    }
}
