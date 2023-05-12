package fss_server.file_access;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import fss_server.entities.User;

/**
 * This class is responsible for reading and writing the users file.
 */
public class UserData {
    final private String path = "src/main/resources/users.json";
    final private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    // array of users
    private User[] jsonUsers;

    /**
     * Read users file.
     */
    public UserData() {
        this.jsonUsers = readUsers();
    }

    private User[] readUsers() {
        User[] read_users = {};
        try {
            JsonReader reader = new JsonReader(new FileReader(path));
            read_users = new Gson().fromJson(reader, User[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return read_users;
    }

    /**
     * @return array of users
     */
    public User[] getUserData() {
        return this.jsonUsers;
    }

    /**
     * @param user user to add
     */
    public void addUser(User user) {
        this.jsonUsers = Arrays.copyOf(this.jsonUsers, this.jsonUsers.length + 1);
        this.jsonUsers[this.jsonUsers.length - 1] = user;
        writeUsers();
    }

    private void writeUsers() {
        File file = new File(path);
        String res = gson.toJson(this.jsonUsers);
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(res);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
