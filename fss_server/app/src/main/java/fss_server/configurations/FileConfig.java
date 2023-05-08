package fss_server.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fss_server.file_access.FileManager;
import fss_server.file_access.Settings;

@Configuration
public class FileConfig {

    @Bean
    public FileManager fileManager() {
        Settings settings = new Settings();
        return new FileManager(settings.getRoot());
    }
}
