package com.example.demo.Data;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.security.spec.KeySpec;

public class encryption
{

    public String caesarEncrypt(String pass,int offSet) // צופן קייסר שמקבל את הסיסמה ומספר ראנדומלי
    {
        String cipher=""; // צופן
        for(int i=0;i<pass.length();i++)
        {
            int hashedChar = ((int)pass.charAt(i) + offSet)%128;
            char newChar = (char)hashedChar;
            cipher = cipher + newChar;
        }
        return cipher;
    }
    public String caeserDecrypt(String hashedPassword, int offSet)
    {
        String pass = caesarEncrypt(hashedPassword, -offSet);
        return pass;
    }

}
