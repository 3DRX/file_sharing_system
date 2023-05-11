package fss_client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.GsonBuilder;

public class FileManager {
    private String pwd = "/";
    private final String host = App.settings.getHost();
    private final int port = App.settings.getPort();

    public FileManager() {
    }

    // login
    public boolean login(User user) {
        String path = "/login";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(URI.create("http://" + host + ":" + port + path))
                .POST(HttpRequest.BodyPublishers
                        .ofString(new GsonBuilder()
                                .setPrettyPrinting()
                                .create()
                                .toJson(user)))
                .setHeader("Content-Type", "application/json")
                .build();
        try {
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
            return response.body().equals("true");
        } catch (Exception e) {
            System.out.println("Server is not responding");
            return false;
        }
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

    public String getDir(String filePath)
            throws InterruptedException, IOException {
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

    public void get(String path) {
        String fullPath = path;
        if (path.charAt(0) == '/') {
        } else if (path.charAt(0) == '.' && path.charAt(1) == '/') {
            fullPath = pwd + path.substring(2, path.length());
        } else {
            fullPath = pwd + path;
        }
        if (!isFile(fullPath)) {
            System.out.println("error: " + path + " is not a file");
            return;
        } else {
            System.out.println("downloading " + fullPath + ":");
        }
        getFile(fullPath);
        System.out.println("downloaded " + fullPath);
    }

    private void getFile(String fullPath) {
        String path = "getFile";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + host + ":" + port + "/" + path))
                .POST(HttpRequest.BodyPublishers.ofString(fullPath))
                .build();
        HttpResponse<InputStream> response = null;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofInputStream());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        HttpHeaders header = response.headers();
        String fileName = null;
        try {
            fileName = header
                    .firstValue("Content-Disposition")
                    .get()
                    .split("=")[1];
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        File file = new File(App.settings.getRoot() + fileName);
        InputStream is = response.body();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, false);
            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            fos.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    private boolean isFile(String fullPath) {
        try {
            String res = getDir(fullPath);
            if (res.startsWith("\tno such file or directory")) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public void put(String path) {
        if (App.loggedUser.name().equals("anonymous")) {
            System.out.println("error: permission denied");
            return;
        }
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("error: " + path + " does not exist");
            return;
        }
        if (file.isDirectory()) {
            System.out.println("error: " + path + " is a directory");
            return;
        }
        putFile(file);
    }

    private void putFile(File file) {
        String pathOnServer = pwd + file.getName();
        System.out.println("uploading " + file.getName() + "to " + pathOnServer + ":");
        String path = "putFile";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                    .uri(URI.create(
                            new StringBuilder()
                                    .append("http://")
                                    .append(host)
                                    .append(":")
                                    .append(port)
                                    .append("/")
                                    .append(path)
                                    .append("?")
                                    .append("newFileName=")
                                    .append(pathOnServer)
                                    .toString()))
                    .POST(HttpRequest.BodyPublishers.ofFile(file.toPath()))
                    .build();
        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
            return;
        }
        HttpResponse<String> response = null;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println(response.body());
    }
}
