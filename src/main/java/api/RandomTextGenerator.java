package api;

import java.security.SecureRandom;

public class RandomTextGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int LENGTH = 5;

    public static String generateRandomText() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder("Test ");
        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

}
