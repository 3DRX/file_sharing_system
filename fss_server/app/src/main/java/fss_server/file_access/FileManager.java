package fss_server.file_access;

import java.io.File;

public class FileManager {

    private String root;

    public FileManager(String root) {
        this.root = root;
    }

    public String dir(String pwd) {
        if (!pwd.startsWith(this.root)) {
            return "\tno such file or directory\n";
        }
        StringBuilder sb = new StringBuilder();
        final File folder = new File(pwd);
        if (!folder.exists()) {
            return "\tno such file or directory\n";
        }
        if (!folder.isDirectory()) {
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
}
