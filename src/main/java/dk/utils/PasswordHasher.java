package dk.utils;

public class PasswordHasher {

    public static String hash(String password) {
        return Integer.toString(password.hashCode());
    }

    public static boolean verify(String password, String storedHash) {
        return hash(password).equals(storedHash);
    }
}