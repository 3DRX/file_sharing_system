package fss_server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fss_server.file_access.FileManager;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
public class GetDirController {
    private final Logger logger = LoggerFactory.getLogger(GetDirController.class);

    @Autowired
    private FileManager fileManager;

    @PostMapping("/getDir")
    public String getDir(@RequestBody String path, HttpServletRequest request) {
        logger.info("User at " + request.getRemoteAddr() + " requested directory " + path);
        return this.fileManager.dir(path);
    }
}
