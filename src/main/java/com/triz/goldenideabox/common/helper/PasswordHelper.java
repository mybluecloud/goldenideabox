package com.triz.goldenideabox.common.helper;


import com.triz.goldenideabox.model.User;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class PasswordHelper {

    //private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    private String algorithmName = "md5";
    private int hashIterations = 2;

    public void encryptPassword(User user) {
        //String salt=randomNumberGenerator.nextBytes().toHex();
        String newPassword = new SimpleHash(algorithmName, user.getPassword(), ByteSource.Util.bytes(user.getAccount()), hashIterations).toHex();
        //String newPassword = new SimpleHash(algorithmName, user.getPassword()).toHex();
        user.setPassword(newPassword);

    }

    public String encryptPassword(String account, String password) {
        return new SimpleHash(algorithmName, password, ByteSource.Util.bytes(account), hashIterations).toHex();
    }

    public static void main(String[] args) {
        //PasswordHelper passwordHelper = new PasswordHelper();
        //User user = new User();
        //user.setAccount("zhangqi");
        //user.setPassword("zhangqi");
        //passwordHelper.encryptPassword(user);
        //System.out.println(user.getPassword());
        String type = "0301";
        if (type.equalsIgnoreCase("01") || type.equalsIgnoreCase("02")
                || !type.equalsIgnoreCase("03")) {
            System.out.println(type);
        }

    }
}
