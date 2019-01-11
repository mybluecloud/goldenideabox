package com.triz.goldenideabox.controller;

import com.triz.goldenideabox.model.Template;
import com.triz.goldenideabox.model.TemplateProperty;
import com.triz.goldenideabox.model.User;
import com.triz.goldenideabox.common.helper.ParameterHelper;
import com.triz.goldenideabox.service.TemplateService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TemplateController {

    @Autowired
    private TemplateService templateService;


    @ResponseBody
    @RequestMapping(value = "/getTemplates", produces = "application/json;charset=UTF-8")
    public Object getAllTemplate(@RequestParam(required = true) boolean review) {

        User user= (User) SecurityUtils.getSubject().getPrincipal();
        List<Template> lst = templateService.selectTemplateByUser(user.getId(),review);
        Map<Object, Object> info = new HashMap<Object, Object>();
        info.put("data", lst);
        return info;
    }

    @ResponseBody
    @RequestMapping(value = "/delTemplate", produces = "application/json;charset=UTF-8")
    public int delTemplate(
        @RequestParam(required = true) int id) {

        return templateService.deleteByTemplateID(id);
    }

    @ResponseBody
    @RequestMapping(value = "/reviewTemplate", produces = "application/json;charset=UTF-8")
    public int reviewTemplate(
        @RequestParam(required = true) int id,@RequestParam(required = true) int state) {

        return templateService.reviewTemplate(id,state);
    }

    @ResponseBody
    @RequestMapping(value = "/changeTemplateState", produces = "application/json;charset=UTF-8")
    public int changeTemplateState(
        @RequestParam(required = true) int id, @RequestParam(required = true) int state) {

        return templateService.changeTemplateState(id, state);
    }

    @ResponseBody
    @RequestMapping(value = "/saveTemplate", produces = "application/json;charset=UTF-8")
    public int saveTemplate(
        @RequestParam(required = true) int id,
        @RequestParam(required = true) String name,
        @RequestParam(required = true) String description,
        @RequestParam(required = true) String data) {


        User user= (User) SecurityUtils.getSubject().getPrincipal();

        return templateService.saveTemplate(id,name,description,user,data);
    }

    @RequestMapping(value = "/template")
    public String checkTemplate(Model model) {

        // model.addAttribute("list", lst);
        return "backstage/template";
    }

    @RequestMapping(value = "/newTemplate")
    public String newTemplate(Model model) {

        // model.addAttribute("list", lst);
        return "backstage/template_property";
    }
    @RequestMapping(value = "/editTemplate")
    public String editTemplate(
        @RequestParam(required = true) int id,Model model) {
        model.addAttribute("id",id);
        return "backstage/template_property";
    }

    @ResponseBody
    @RequestMapping(value = "/getTemplate", produces = "application/json;charset=UTF-8")
    public Object getTemplate(
        @RequestParam(required = true) int id) {
        Template template = templateService.getTemplate(id);
        List<TemplateProperty> lst = templateService.getTemplateProperty(id);
        Map<Object, Object> info = new HashMap<Object, Object>();
        info.put("info", template);
        info.put("atts", lst);
        return info;
    }

    @ResponseBody
    @RequestMapping(value = "/permitTemplate", produces = "application/json;charset=UTF-8")
    public Object permitTemplate(
        @RequestParam(required = true) int id) {
        List<User> users = templateService.getNoPermitUser(id);
        List<User> permits = templateService.getPermitUser(id);
        Map<Object, Object> info = new HashMap<Object, Object>();
        info.put("user", users);
        info.put("permit", permits);
        return info;
    }

    @ResponseBody
    @RequestMapping(value = "/copyTemplate", produces = "application/json;charset=UTF-8")
    public Object copyTemplate(
        @RequestParam(required = true) int id) {
        Template tpl = templateService.getTemplate(id);
        User user= (User) SecurityUtils.getSubject().getPrincipal();


        return templateService.copyTemplate(id,tpl.getName()+"-副本",tpl.getDescription(),user);
    }

    @ResponseBody
    @RequestMapping(value = "/permit", produces = "application/json;charset=UTF-8")
    public int permit(
        @RequestParam(required = true) int id,@RequestParam(required = true) String permit) {

        return templateService.permit(id, ParameterHelper.StringToIntegerList(permit,","));
    }
}
