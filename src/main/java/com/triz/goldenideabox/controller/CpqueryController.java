package com.triz.goldenideabox.controller;

import com.triz.goldenideabox.model.Cpquery;
import com.triz.goldenideabox.model.CpqueryResult;
import com.triz.goldenideabox.model.OperationLog;
import com.triz.goldenideabox.model.OperationLog.LogCategory;
import com.triz.goldenideabox.model.PatentRecord;
import com.triz.goldenideabox.model.Template;
import com.triz.goldenideabox.model.TemplateProperty;
import com.triz.goldenideabox.model.User;
import com.triz.goldenideabox.service.CpqueryService;
import com.triz.goldenideabox.service.OperationLogService;
import com.triz.goldenideabox.service.PatentService;
import java.util.Date;
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
@RequestMapping("/cpquery")
public class CpqueryController {

    @Autowired
    private PatentService patentService;

    @Autowired
    private CpqueryService cpqueryService;

    @Autowired
    private OperationLogService operationLogService;

    @RequestMapping("/cpquerylist")
    public String cpquery(Model model) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<Template> tpls = patentService.getTemplates(user.getId());

        for (Template tpl:tpls) {
            List<TemplateProperty> properties = patentService.getTemplateProperty(tpl.getId());
            tpl.setProperties(properties);
        }
        model.addAttribute("templates",tpls);

        List<OperationLog> logs = operationLogService.getOperationLog(LogCategory.CPQUERY);
        model.addAttribute("logs",logs);

        return "backstage/cpquery";
    }

    @ResponseBody
    @RequestMapping(value = {"/cpqueryInfo"}, produces = "application/json;charset=UTF-8")
    public Object cpqueryInfo() {

        Map<Object, Object> info = new HashMap<Object, Object>();

        List<Cpquery> lst = cpqueryService.getCpqueryInfo();
        info.put("data",lst);
        return info;
    }

    @ResponseBody
    @RequestMapping(value = {"/getCpqueryInfo"}, produces = "application/json;charset=UTF-8")
    public Object getCpqueryInfo(@RequestParam(required = true) int id) {
        return cpqueryService.getCpqueryInfoById(id);
    }

    @ResponseBody
    @RequestMapping(value = {"/delCpqueryInfo"}, produces = "application/json;charset=UTF-8")
    public Object delCpqueryInfo(@RequestParam(required = true) int id) {
        return cpqueryService.deleteCpqueryInfo(id);
    }

    @ResponseBody
    @RequestMapping(value = {"/saveCpqueryInfo"}, produces = "application/json;charset=UTF-8")
    public Object saveCpqueryInfo(@RequestParam(required = true) int id,
        @RequestParam(required = true) int templateId,
        @RequestParam(required = true) int matchField,
        @RequestParam(required = true) String matchChar,
        @RequestParam(required = true) int applicationNumberField,
        @RequestParam(required = true) int statusField,
        @RequestParam(required = true) String account,
        @RequestParam(required = true) String password) {

        Map<Object, Object> info = new HashMap<Object, Object>();
        Cpquery cpquery = new Cpquery();
        cpquery.setAccount(account);
        cpquery.setPassword(password);
        cpquery.setTemplateId(templateId);
        cpquery.setMatchField(matchField);
        cpquery.setMatchChar(matchChar);
        cpquery.setApplicationNumberField(applicationNumberField);
        cpquery.setStatusField(statusField);
        cpquery.setStatus(0);
        if (id != 0) {
            cpquery.setId(id);
            cpqueryService.updateCpqueryInfo(cpquery);
        } else {
            cpqueryService.addCpqueryInfo(cpquery);
        }

        info.put("data",cpquery);
        return info;
    }

    @ResponseBody
    @RequestMapping(value = {"/getJcaptcha"}, produces = "application/json;charset=UTF-8")
    public Object getJcaptchaServlet(@RequestParam(required = true) int id) {

        Map<Object, Object> info = new HashMap<Object, Object>();
        List<Cpquery> lst = cpqueryService.getCpqueryInfo();
        for (Cpquery cpquery:lst) {
            if (cpquery.getStatus() == 1) {
                info.put("status",cpquery.getStatus());
                return info;
            }
        }

        String jsessionID = cpqueryService.getJsessionID();
        if (jsessionID.length() == 0) {
            info.put("status",2);
            return info;
        }

        //info.put("JSESSIONID",jsessionID);
        Date date = new Date();
        String imagePath = cpqueryService.getJcaptchaImage(date);
        info.put("imagePath",imagePath);
        String imageText = cpqueryService.getJcaptchaText(date);
        info.put("imageText",imageText);
        info.put("status",0);
        return info;
    }

    @ResponseBody
    @RequestMapping(value = {"/executeCpquery"}, produces = "application/json;charset=UTF-8")
    public Object executeCpquery(@RequestParam(required = true) int id,
        @RequestParam(required = true) String code) {

        Map<Object, Object> info = new HashMap<Object, Object>();

        int status = 1;
        if (code != null && code.length() > 0) {
            status = cpqueryService.getValidationStatus(code);
        }

        info.put("status",status);
        if (status == 0) {
            String retcode = cpqueryService.beginQuery(id);
            info.put("retcode",retcode);
        }

        return info;
    }

    @ResponseBody
    @RequestMapping(value = {"/stopCpquery"}, produces = "application/json;charset=UTF-8")
    public Object stopCpquery() {

        cpqueryService.stopCpquery();


        return 0;
    }

    @ResponseBody
    @RequestMapping("/cpqueryResult")
    public Object cpqueryResult() {
        Map<Object, Object> info = new HashMap<Object, Object>();
        List<CpqueryResult> results = cpqueryService.getCpqueryResult();
        info.put("data",results);
        return info;
    }

    @ResponseBody
    @RequestMapping(value = {"/cpqueryRecord"}, produces = "application/json;charset=UTF-8")
    public Object cpqueryRecord() {

        Map<Object, Object> info = new HashMap<Object, Object>();

        List<PatentRecord> records = cpqueryService.getCpqueryRecord();
        info.put("data",records);
        return info;
    }
}
