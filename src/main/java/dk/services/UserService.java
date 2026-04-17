package dk.services;

import dk.entities.User;
import dk.exceptions.DatabaseException;
import dk.persistence.AccountMapper;
import dk.persistence.ConnectionPool;
import dk.persistence.UserMapper;
import dk.utils.PasswordHasher;

public class UserService {

    public static void registerUser(
            String email,
            String password,
            String role,
            ConnectionPool cp
    ) throws DatabaseException {

        // 1. hash password
        String hashedPassword = PasswordHasher.hash(password);

        // 2. create user
        User user = new User(
                0,
                email,
                hashedPassword,
                role
        );

        // 3. insert user
        UserMapper.createUser(user, cp);

        // 4. fetch created user (to get generated ID)
        User createdUser = UserMapper.getUserByEmail(email, cp);

        // 5. create empty account
        AccountMapper.createAccount(createdUser.getUserId(), 0, cp);
    }

    public static User getUserByEmail(String email, ConnectionPool cp)
            throws DatabaseException {

        return UserMapper.getUserByEmail(email, cp);
    }
}