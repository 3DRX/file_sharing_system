package fss_server.controllers;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fss_server.file_access.FileManager;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin
public class GetFileController {
    private final Logger logger = LoggerFactory.getLogger(GetFileController.class);

    @Autowired
    private FileManager fileManager;

    @PostMapping("/getFile")
    public void getFile(@RequestBody String path, HttpServletResponse response) {
        response.setHeader("Content-Disposition", "attachment;filename=" + getFileName(path));
        byte[] buffer = new byte[1];
        BufferedInputStream bis = this.fileManager.streamedFile(path);
        if (bis == null) {
            logger.warn("File " + path + " not found.");
            return;
        }
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            int i = bis.read(buffer);
            int count = 0;
            while (i != -1) {
                count++;
                os.write(buffer);
                if (count % 1000 == 0) {
                    count = 0;
                    os.flush();
                }
                i = bis.read(buffer);
            }
            if (count != 0) {
                os.flush();
            }
        } catch (Exception e) {
            logger.error("Error sending file " + path);
            e.printStackTrace();
        } finally {
            try {
                bis.close();
                logger.info("File " + path + " sent.");
            } catch (IOException e2) {
                logger.error("Error closing file " + path);
                e2.printStackTrace();
            }
        }
        return;
    }

    private String getFileName(String path) {
        String[] parts = path.split("/");
        return parts[parts.length - 1];
    }
}
