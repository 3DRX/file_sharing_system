package fss_client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FileManager {
    private String pwd = "/";
    private final String host = "localhost";
    private final int port = 8888;

    public FileManager() {
    }

    public void dir() {
        String res = "";
        try {
            res = getDir(pwd);
        } catch (Exception e) {
            System.out.println("no such file or directory");
            return;
        }
        System.out.println(res);
    }

    public String getDir(String filePath) throws InterruptedException, IOException {
        String path = "/getDir";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + host + ":" + port + path))
                .POST(HttpRequest.BodyPublishers.ofString(filePath))
                .build();
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public void pwd() {
        System.out.println(pwd);
    }

    public void cd(String path) {
        path = trimSlash(path);
        if (path.equals(".")) {
            dir();
        } else if (path.equals("..")) {
            if (pwd.equals("/")) {
                dir();
                return;
            }
            int index = pwd.length() - 1;
            for (int i = index - 1; i >= 0; i--) {
                if (pwd.charAt(i) == '/') {
                    index = i;
                    break;
                }
            }
            pwd = pwd.substring(0, index + 1);
            dir();
        } else {
            if (path.charAt(0) == '/') {
            } else if (path.charAt(0) == '.' && path.charAt(1) == '/') {
                path = pwd + path.substring(2, path.length());
            } else {
                path = pwd + path;
            }
            try {
                String respond = getDir(path);
                if (respond.startsWith("\tno such file or directory")) {
                    System.out.println(path + " is not a directory");
                    return;
                }
                pwd = path + "/";
                dir();
            } catch (Exception e) {
                System.out.println("no such file or directory: " + path);
            }
        }
    }

    private String trimSlash(String path) {
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }
}
