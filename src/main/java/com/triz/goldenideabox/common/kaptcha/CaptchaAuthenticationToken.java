package com.triz.goldenideabox.common.kaptcha;

import org.apache.shiro.authc.UsernamePasswordToken;

public class CaptchaAuthenticationToken extends UsernamePasswordToken {

    private String kaptcha;


    public CaptchaAuthenticationToken (String username, String password,
        boolean rememberMe, String host, String kaptcha) {
        super(username, password, rememberMe, host);
        this.kaptcha = kaptcha;
    }

    public void setKaptcha(String kaptcha){
        this.kaptcha= kaptcha;
    }

    public String getKaptcha(){
        return this.kaptcha;
    }
}
