package assignment1;

import java.security.SecureRandom;
import com.password4j.types.Argon2;
import com.password4j.Argon2Function;
import com.password4j.Hash;
import com.password4j.Password;

public class PasswordSalt {

    public Double genSalt(){
        SecureRandom secureRandom = new SecureRandom();
        return secureRandom.nextDouble(0,1);
    }

    public String hashPass(String password, Double salt){
        Argon2Function argon2 = Argon2Function.getInstance(15, 2, 1, 32, Argon2.ID);
        Hash hash = Password.hash(password).addSalt(salt.toString()).with(argon2);
        return hash.getResult();
    }

    public boolean verifyPass(String password, Student student){
        if(hashPass(password, student.getPasswordSalt()).equals(student.getPasswordHash())){
            return true;
        }

        return false;
    }
}
