package com.triz.goldenideabox.controller;

import com.alibaba.fastjson.JSONObject;
import com.triz.goldenideabox.common.kaptcha.IncorrectCaptchaException;
import com.triz.goldenideabox.common.helper.ParameterHelper;
import com.triz.goldenideabox.common.helper.PasswordHelper;
import com.triz.goldenideabox.model.IndexSetting;
import com.triz.goldenideabox.model.Message;
import com.triz.goldenideabox.model.User;
import com.triz.goldenideabox.service.UserService;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Decoder;

@Controller
public class HomeController {

    @Value("${goldenideabox.path.avatar}")
    private String avatar;


    @Autowired
    private UserService userService;


    @RequestMapping(value = {"/login"}, method = {RequestMethod.POST})
    public String login(HttpServletRequest request, Model model) {

        Object exception = request.getAttribute("shiroLoginFailure");

        if (exception != null) {
            if (AuthenticationException.class.getName().equalsIgnoreCase(exception.getClass().getName())) {
                model.addAttribute("msg", "用户或密码不正确！");
            } else if (UnknownAccountException.class.getName().equalsIgnoreCase(exception.getClass().getName())) {
                model.addAttribute("msg", "用户不存在！");
            } else if (IncorrectCredentialsException.class.getName().equalsIgnoreCase(exception.getClass().getName())) {
                model.addAttribute("msg", "密码错误！");
            } else if (LockedAccountException.class.getName().equalsIgnoreCase(exception.getClass().getName())) {
                model.addAttribute("msg", "用户已经被锁定不能登录，请与管理员联系！");
            } else if (IncorrectCaptchaException.class.getName().equalsIgnoreCase(exception.getClass().getName())) {
                model.addAttribute("msg", "验证码不正确！");
            } else {
                model.addAttribute("msg", "登录失败！");
            }

            return "backstage/login";
        }

        return "redirect:index";
    }


    @RequestMapping(value = {"/regedit"}, method = {RequestMethod.POST})
    public String regeditUser(User user, Model model) {

        User user1 = userService.selectByAccount(user.getAccount());

        if (user1 != null) {
            model.addAttribute("msg", "注册失败，用户名已存在！");
            model.addAttribute("state", 1);
        } else {
            PasswordHelper passwordHelper = new PasswordHelper();
            passwordHelper.encryptPassword(user);
            user.setState(1);
            user.setSex(0);

            user.setImage("/layouts/layout/backstage/img/avatar.jpg");
            user.setLastedLoginTime(new Date());
            userService.regeditAccout(user);
            model.addAttribute("msg", "注册成功，请等待管理员进行审核！");
            model.addAttribute("state", 0);
        }

        return "backstage/login";
    }

    @RequestMapping(value = {"/updateUserInfo"}, method = {RequestMethod.POST})
    public String userInfo(User user) {

        userService.updateUserInfo(user);
        User user1 = (User) SecurityUtils.getSubject().getPrincipal();

        user = userService.selectByAccount(user.getAccount());

        user1.setSex(user.getSex());
        user1.setEmail(user.getEmail());
        user1.setName(user.getName());
        user1.setPhone(user.getPhone());
        user1.setIntroduction(user.getIntroduction());
        return "redirect:backstage/userinfo";
    }

    @ResponseBody
    @RequestMapping(value = {"/uploadAvatar"}, produces = "application/json;charset=UTF-8")
    public Object uploadAvatar(@RequestParam(required = true) int id, @RequestParam(required = true) String src) throws IOException {

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

        String filename = df.format(new Date()) + id + ".png";
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] b = decoder.decodeBuffer(src.split(",")[1]);
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        BufferedImage bi = ImageIO.read(bais);
        File w2 = new File(avatar + filename);
        w2.createNewFile();
        ImageIO.write(bi, "png", w2);

        User user = new User();
        user.setId(id);
        user.setImage("/avatar/" + filename);
        userService.updateUserInfo(user);
        User user1 = (User) SecurityUtils.getSubject().getPrincipal();
        user1.setImage(user.getImage());

        return 0;

    }

    @RequestMapping(value = {"/changePassword"}, method = {RequestMethod.POST})
    public String changePassword(@RequestParam(required = true) String cur_password,
        @RequestParam(required = true) String password, Model model) {
        User user = new User();
        User user1 = (User) SecurityUtils.getSubject().getPrincipal();
        PasswordHelper passwordHelper = new PasswordHelper();

        if (user1.getPassword().equalsIgnoreCase(passwordHelper.encryptPassword(user1.getAccount(), cur_password))) {
            user.setId(user1.getId());
            user.setPassword(passwordHelper.encryptPassword(user1.getAccount(), password));
            userService.updateUserInfo(user);
            user1.setPassword(user.getPassword());


        } else {
            model.addAttribute("error", "当前密码输入不正确！");

            return "backstage/userinfo#tab_3-3";
        }

        return "redirect:backstage/userinfo";
    }

    @ResponseBody
    @RequestMapping(value = {"/chooseUser"}, produces = "application/json;charset=UTF-8")
    public Object chooseUser(@RequestParam(required = true) String query) {
        return userService.queryUser(query);

    }

    @ResponseBody
    @RequestMapping(value = {"/chatUser"}, produces = "application/json;charset=UTF-8")
    public Object chooseRole(@RequestParam(required = true) String time) {
        Map<Object, Object> info = new HashMap<Object, Object>();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<JSONObject> users = userService.loadChatUserList(user.getId());
        try {
            date = simpleDateFormat.parse(time);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<JSONObject> roles = userService.loadChatGroupList(user.getId(), date);
        info.put("user", users);
        info.put("role", roles);
        return info;
    }

    @ResponseBody
    @RequestMapping(value = {"/loadMessage"}, produces = "application/json;charset=UTF-8")
    public Object loadMessage(@RequestParam(required = true) int id, @RequestParam(required = true) int type) {
        if (type == 0) {
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            return userService.loadUserMessage(user.getId(), id);


        } else {
            return userService.loadGroupMessage(id);
        }

    }

    @ResponseBody
    @RequestMapping(value = {"/sendMessage"}, produces = "application/json;charset=UTF-8")
    public Object sendMessage(@RequestParam(required = true) int id,
        @RequestParam(required = true) String text,
        @RequestParam(required = true) int type) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Message msg = new Message();
        msg.setContent(text);
        msg.setReceiver(id);
        msg.setSender(user.getId());
        msg.setIsGroup(type);
        msg.setTime(new Date());
        msg.setState(0);
        return userService.sendMessage(msg);

    }

    @ResponseBody
    @RequestMapping(value = {"/addIndexSetting"}, produces = "application/json;charset=UTF-8")
    public Object addIndexSetting(@RequestParam(required = true) int type,
        @RequestParam(required = true) int modelId)  {

        User user = (User) SecurityUtils.getSubject().getPrincipal();

        IndexSetting indexSetting = new IndexSetting();
        indexSetting.setType(type);
        indexSetting.setModelId(modelId);
        indexSetting.setUserId(user.getId());

        userService.addIndexSetting(indexSetting);

        return indexSetting;

    }

    @ResponseBody
    @RequestMapping(value = {"/removeModel"}, produces = "application/json;charset=UTF-8")
    public Object removeModel(@RequestParam(required = true) int id)  {

        return userService.deleteIndexSetting(id);
    }

    @ResponseBody
    @RequestMapping(value = {"/getIndexSetting"}, produces = "application/json;charset=UTF-8")
    public Object getIndexSetting()  {

        User user = (User) SecurityUtils.getSubject().getPrincipal();

        return userService.getIndexSetting(user.getId());
    }

    @ResponseBody
    @RequestMapping(value = {"/sortModel"}, produces = "application/json;charset=UTF-8")
    public Object sortModel(@RequestParam(required = true) String sort)  {

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<Integer> lst = ParameterHelper.StringToIntegerList(sort, ",");
        return userService.sortIndexSetting(lst);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "backstage/login";
    }

    @RequestMapping(value = {"/index"})
    public String usersPage(Model model) {

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        model.addAttribute("lastLoginTime", formatter.format(user.getLastedLoginTime()));
        userService.updateLoginTime(user.getId());
        return "backstage/index";
    }

    @RequestMapping("/userinfo")
    public String userProfile(Model model) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();

        user = userService.selectByAccount(user.getAccount());
        model.addAttribute("user", user);
        return "backstage/user_profile";
    }

    @RequestMapping("/403")
    public String forbidden() {
        return "common/403";
    }

    @RequestMapping("/404")
    public String nofound() {
        return "common/403";
    }

    @RequestMapping("/405")
    public String noAuth() {
        return "common/403";
    }
}
