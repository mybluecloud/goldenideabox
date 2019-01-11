package com.triz.goldenideabox.common.kaptcha;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

public class KaptchaFilter extends FormAuthenticationFilter {

    public static final String DEFAULT_CAPTCHA_PARAM = "captcha";

    private String captchaParam = DEFAULT_CAPTCHA_PARAM;


    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        WebUtils.getAndClearSavedRequest(request);
        //WebUtils.redirectToSavedRequest(request, response, this.getSuccessUrl());
        return super.onLoginSuccess(token, subject, request, response);
    }

    //登录验证
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response)  throws Exception {

        CaptchaAuthenticationToken token = createToken(request, response);

        try {

            /*图形验证码验证*/
            doCaptchaValidate((HttpServletRequest) request, token);
            this.setSuccessUrl("/index");
            Subject subject = getSubject(request, response);
            subject.login(token);

            return onLoginSuccess(token, subject, request, response);
        } catch (IncorrectCaptchaException e) {
            return onLoginFailure(token, e, request, response);
        } catch (IncorrectCredentialsException e) {
            return onLoginFailure(token, e, request, response);
        } catch (UnknownAccountException e) {
            return onLoginFailure(token, e, request, response);
        } catch (LockedAccountException e) {
            return onLoginFailure(token, e, request, response);
        }
    }

    // 验证码校验
    protected void doCaptchaValidate(HttpServletRequest request, CaptchaAuthenticationToken token) {


        //session中的图形码字符串
        String captcha = (String) request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);

        if (captcha == null || !captcha.equalsIgnoreCase(token.getKaptcha())) {
            throw new IncorrectCaptchaException();
        }
    }

    @Override
    protected CaptchaAuthenticationToken createToken(ServletRequest request, ServletResponse response) {

        String username = getUsername(request);
        String password = getPassword(request);
        String captcha = getCaptcha(request);
        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);

        return new CaptchaAuthenticationToken(username, password, rememberMe, host, captcha);
    }


    public String getCaptchaParam() {
        return captchaParam;
    }



    protected String getCaptcha(ServletRequest request) {
        return WebUtils.getCleanParam(request, getCaptchaParam());
    }

    //保存异常对象到request
    @Override
    protected void setFailureAttribute(ServletRequest request, AuthenticationException ae) {
        request.setAttribute(getFailureKeyAttribute(), ae);
    }

}


