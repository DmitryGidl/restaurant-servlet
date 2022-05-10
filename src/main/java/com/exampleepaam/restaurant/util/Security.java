package com.exampleepaam.restaurant.util;

import org.mindrot.jbcrypt.BCrypt;

/*
 * Helper class with Auth methods
 */
public final class Security {
    private Security() {
    }

    /**
     * Hashes a password
     *
     * @param password Password to be hashed
     * @return Hashed password
     */
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
