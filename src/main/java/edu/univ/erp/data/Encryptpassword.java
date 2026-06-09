package edu.univ.erp.data;
import org.mindrot.jbcrypt.BCrypt;
public class Encryptpassword {

    public static String hashPassword(String plainPassword) {

        String salt = BCrypt.gensalt();


        String h = BCrypt.hashpw(plainPassword, salt);

        return h;
    }
}
