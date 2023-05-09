package fss_server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import fss_server.file_access.FileManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
public class PutFileController {
    private final Logger logger = LoggerFactory.getLogger(PutFileController.class);

    @Autowired
    FileManager fileManager;

    @PostMapping("/putFile")
    public boolean putFile(@RequestParam String newFileName, HttpServletRequest request) {
        try {
            InputStream is = request.getInputStream();
            fileManager.put(is, newFileName);
        } catch (Exception e) {
            logger.warn("failed to put file");
            return false;
        }
        return true;
    }
}
