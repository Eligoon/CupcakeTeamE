package dk.services;

import dk.entities.User;
import dk.persistence.ConnectionPool;
import dk.persistence.UserMapper;
import dk.utils.PasswordHasher;

public class AuthService {

    public static User login(String email, String password, ConnectionPool cp) throws Exception {

        User user = UserMapper.getUserByEmail(email, cp);

        if (user == null) {
            throw new Exception("User not found");
        }

        if (!PasswordHasher.verify(password, user.getPasswordHash())) {
            throw new Exception("Wrong password");
        }

        return user;
    }
}