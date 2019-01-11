package com.triz.goldenideabox.controller;

import com.alibaba.fastjson.JSONObject;
import com.triz.goldenideabox.model.Chart;
import com.triz.goldenideabox.model.DisplayConfig;
import com.triz.goldenideabox.model.Template;
import com.triz.goldenideabox.model.TemplateProperty;
import com.triz.goldenideabox.model.User;
import com.triz.goldenideabox.service.ChartService;
import com.triz.goldenideabox.service.PatentService;
import com.triz.goldenideabox.service.TemplateService;
import java.util.ArrayList;
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
@RequestMapping("/chart")
public class ChartController {

    @Autowired
    private PatentService patentService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private ChartService chartService;

    @ResponseBody
    @RequestMapping(value = {"/getCharts"}, produces = "application/json;charset=UTF-8")
    public Object getCharts() {

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return chartService.getCharts(user.getId());
    }

    @ResponseBody
    @RequestMapping(value = {"/getChartOption"}, produces = "application/json;charset=UTF-8")
    public Object getChartOption(@RequestParam(required = true) boolean bDate) {

        Map<Object, Object> info = new HashMap<Object, Object>();
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (SecurityUtils.getSubject().hasRole("")) {

        }
        List<DisplayConfig> configs = patentService.getDisplayConfigs(user.getId());
        info.put("source",configs);

        List<Template> tpls = patentService.getTemplates(user.getId());

        info.put("template",tpls);
        for (Template tpl:tpls) {
            List<TemplateProperty> properties = patentService.getTemplateProperty(tpl.getId());

            info.put("template"+tpl.getId(),properties);
            if (bDate) {
                List<TemplateProperty> dateProperties = patentService.getTemplatePropertyByType(tpl.getId(),5);

                info.put("dateTemplate"+tpl.getId(),dateProperties);
            }
        }


        return info;

    }


    @ResponseBody
    @RequestMapping(value = {"/addChart"}, produces = "application/json;charset=UTF-8")
    public Object addChart(@RequestParam(required = true) String name,
        @RequestParam(required = true) int templateId,
        @RequestParam(required = true) int configId,
        @RequestParam(required = true) int property1,
        @RequestParam(required = false,defaultValue="0") int property2,
        @RequestParam(required = false,defaultValue="0") int scope,
        @RequestParam(required = true) int type) {

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        String filter = "";
        if (configId != 0) {
            filter = patentService.getDisplayConfig(configId).getFilter();
        }


        if (type == 1){
            List<JSONObject> charts = chartService.getSectorChart(user.getId(), templateId, filter, property1);
            if (charts.size() > 50) {
                return -1;
            }
        } else if (type == 2) {
            List<JSONObject> charts = chartService.getHistogram(user.getId(), templateId, filter, property1);
            if (charts.size() > 50) {
                return -1;
            }
        }


        Chart chart = new Chart();
        chart.setUserId(user.getId());
        chart.setName(name);
        chart.setTemplateId(templateId);
        chart.setFilter(filter);
        chart.setProperty1(property1);
        chart.setProperty2(property2);
        chart.setType(type);
        chart.setScope(scope);
        chartService.insertChart(chart);


        return 0;

    }

    @ResponseBody
    @RequestMapping(value = {"/showChart"}, produces = "application/json;charset=UTF-8")
    public Object showChart(@RequestParam(required = true) int id) {

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<JSONObject> charts = new ArrayList<>();
        Map<Object, Object> info = new HashMap<Object, Object>();

        Chart chart = chartService.getChart(id);
        info.put("chart",chart);
        if (chart.getType() == 1){
            charts = chartService.getSectorChart(user.getId(), chart.getTemplateId(), chart.getFilter(), chart.getProperty1());
        } else if (chart.getType() == 2) {
            charts = chartService.getHistogram(user.getId(), chart.getTemplateId(), chart.getFilter(), chart.getProperty1());
        } else if (chart.getType() == 3) {
            charts = chartService.getLineChart(user.getId(), chart.getTemplateId(), chart.getFilter(), chart.getProperty1(),chart.getProperty2(),chart.getScope());
        }
        info.put("data",charts);
        return info;

    }

    @ResponseBody
    @RequestMapping(value = {"/deleteChart"}, produces = "application/json;charset=UTF-8")
    public Object deleteChart(@RequestParam(required = true) int id) {
        return chartService.deleteChart(id);
    }

    @RequestMapping(value = "/sectorChart")
    public String sectorChart(Model model) {

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<Chart> charts = chartService.getChartsByType(user.getId(),1);
        model.addAttribute("charts",charts);
        return "backstage/sector_chart";
    }

    @RequestMapping(value = "/histogram")
    public String histogram(Model model) {

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<Chart> charts = chartService.getChartsByType(user.getId(),2);
        model.addAttribute("charts",charts);
        return "backstage/histogram";
    }

    @RequestMapping(value = "/lineChart")
    public String lineChart(Model model) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<Chart> charts = chartService.getChartsByType(user.getId(),3);
        model.addAttribute("charts",charts);
        return "backstage/line_chart";
    }
}
