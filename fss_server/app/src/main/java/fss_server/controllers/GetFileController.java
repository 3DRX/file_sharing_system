package fss_server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import fss_server.file_access.FileManager;

@RestController
@CrossOrigin
public class GetFileController {
    private final Logger logger = LoggerFactory.getLogger(GetFileController.class);

    @Autowired
    private FileManager fileManager;

    @GetMapping("/getFile")
    public ResponseEntity getFile(String path) {
        // TODO
    }
}
