package fss_server.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fss_server.entities.User;
import fss_server.file_access.UserData;

@RestController
@CrossOrigin
public class LoginController {
    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserData userData;

    @PostMapping("/login")
    public boolean login(@RequestBody User user) {
        boolean success;
        if (user.getName().equals("anonymous")) {
            success = true;
        } else {
            success = validate(user);
        }
        if (success) {
            logger.info("User " + user.getName() + " logged in.");
        } else {
            logger.info("User " + user.getName() + " failed to log in.");
        }
        return success;
    }

    private boolean validate(User user) {
        for (User u : this.userData.getUserData()) {
            if (u.getName().equals(user.getName())
                    && u.getPassword().equals(user.getPassword())) {
                return true;
            }
        }
        return false;
    }
}
