package fss_client;

import java.io.File;
import java.io.FileReader;

import com.google.common.net.InetAddresses;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class Settings {
    private String root;
    private ServerConfig server;

    public String getRoot() {
        return root;
    }

    public String getHost() {
        return server.host();
    }

    public int getPort() {
        return server.port();
    }

    public Settings(String root, ServerConfig server) {
        this.root = root;
        this.server = server;
    }

    public static Settings loadSettings() {
        System.out.println("======================");
        String path = "src/main/resources/settings.json";
        // check if file exists
        File f = new File(path);
        if (!f.exists()) {
            System.out.println("Settings file not found, creating default settings");
            Settings res = new Settings("/tmp/fss_client/", new ServerConfig("localhost", 8888));
            System.out.println("Root: " + res.getRoot());
            System.out.println("Host: " + res.getHost());
            System.out.println("Port: " + res.getPort());
            System.out.println("======================");
            return res;
        }
        try {
            JsonReader reader = new JsonReader(new FileReader(path));
            Settings res = new Gson().fromJson(reader, Settings.class);
            if (res == null) {
                res = new Settings("/tmp/fss_client/", new ServerConfig("localhost", 8888));
            } else {
                if (res.root == null) {
                    res.root = "/tmp/fss_client/";
                }
                if (res.server == null) {
                    res.server = new ServerConfig("localhost", 8888);
                }
                if (res.root.charAt(res.root.length() - 1) != File.separatorChar)
                    res.root += File.separator;
            }
            // check if root exists
            File root = new File(res.getRoot());
            if (!root.exists()) {
                System.out.println("Root path does not exist, creating it");
                root.mkdirs();
            }
            if (!InetAddresses.isInetAddress(res.getHost())) {
                System.out.println("Host is not a valid IP address, using localhost");
                res.server = new ServerConfig("localhost", res.getPort());
            }
            if (res.getPort() < 1024 || res.getPort() > 65535) {
                System.out.println("Port is not in range [1024, 65535], using 8888");
                res.server = new ServerConfig(res.getHost(), 8888);
            }
            System.out.println("Settings loaded:");
            System.out.println("Root: " + res.getRoot());
            System.out.println("Host: " + res.getHost());
            System.out.println("Port: " + res.getPort());
            System.out.println("======================");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

record ServerConfig(String host, int port) {
}
