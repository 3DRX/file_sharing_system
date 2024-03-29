package fss_server.file_access;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * This class is responsible for all file operations.
 */
public class FileManager {
    // logger
    private final Logger logger = LoggerFactory.getLogger(FileManager.class);

    // root directory on server
    private String root;

    /**
     * @param root root directory on server
     */
    public FileManager(File root) {
        this.root = root.getAbsolutePath() + File.separator;
    }

    /**
     * @param pwd path to directory
     * @return directory listing
     */
    public String dir(String pwd) {
        String fullPwd = this.root + pwd;
        if (!fullPwd.startsWith(this.root)) {
            logger.warn("User tried to access file outside root directory");
            return "\tno such file or directory\n";
        }
        StringBuilder sb = new StringBuilder();
        final File folder = new File(fullPwd);
        if (!folder.exists()) {
            logger.warn("User tried to access non-existing file");
            return "\tno such file or directory\n";
        }
        if (!folder.isDirectory()) {
            logger.warn("User tried to access file as directory");
            return "\t" + folder.getName() + "\n";
        }
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                sb
                        .append("\t")
                        .append(" ")
                        .append(fileEntry.getName())
                        .append("\n");
            } else {
                sb
                        .append("\t")
                        .append(" ")
                        .append(fileEntry.getName())
                        .append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * @param filepath path to file on server
     * @return BufferedInputStream of file
     */
    public BufferedInputStream streamedFile(String filepath) {
        String fullpath = this.root + filepath;
        File file = new File(fullpath);
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
        } catch (Exception e) {
            logger.error("Error while reading file " + fullpath);
            e.printStackTrace();
        }
        return bis;
    }

    /**
     * @param is          InputStream of file
     * @param newFileName name of new file
     */
    public void put(InputStream is, String newFileName) {
        File file = new File(this.root + newFileName);
        if (file.exists()) {
            logger.warn("Overwriting existing file");
            return;
        } else {
            try {
                file.createNewFile();
            } catch (Exception e) {
                logger.error("Error while creating new file " + newFileName);
                e.printStackTrace();
                return;
            }
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            fos.close();
            logger.info("Successfully created file " + newFileName);
        } catch (Exception e) {
            logger.error("Error while writing file: " + newFileName);
            e.printStackTrace();
            return;
        }
    }
}
