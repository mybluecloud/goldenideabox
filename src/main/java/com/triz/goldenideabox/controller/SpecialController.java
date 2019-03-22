package com.triz.goldenideabox.controller;

import com.triz.goldenideabox.common.helper.FileUploadHelper;
import com.triz.goldenideabox.common.helper.ParameterHelper;
import com.triz.goldenideabox.dao.CpqueryResultMapper;
import com.triz.goldenideabox.model.Cpquery;
import com.triz.goldenideabox.model.CpqueryResult;
import com.triz.goldenideabox.model.Order;
import com.triz.goldenideabox.model.Patent;
import com.triz.goldenideabox.model.PatentProperty;
import com.triz.goldenideabox.model.Template;
import com.triz.goldenideabox.model.TemplateProperty;
import com.triz.goldenideabox.model.User;
import com.triz.goldenideabox.service.CpqueryService;
import com.triz.goldenideabox.service.PatentService;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/test")
public class SpecialController {

    @Autowired
    private PatentService patentService;

    @Autowired
    private CpqueryResultMapper cpqueryResultMapper;

    @ResponseBody
    @RequestMapping(value = {"/makeOrders"}, produces = "application/json;charset=UTF-8")
    public Object makeOrders(@RequestParam(required = true) int num) {
        if (num > 1000) {
            return "输入太多啦，细水长流！";
        }
        List<Order> orders = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String date = dateFormat.format(new Date());
        for (int i = 0; i < num; ++i) {
            Order order = new Order();
            order.setOrderNum("ORD" + date + "" + i);
            order.setTime(new Date());
            order.setTechName("订单-" + date + "" + i);
            order.setAgentId(i+"");
            order.setApplicantId(i+"");
            order.setCity(i+"");
            order.setContact(i+"");
            order.setContactPhone(i);
            order.setDetailAddress(i+"");
            order.setDistrict(i+"");
            order.setProvince(i+"");
            order.setState("0");
            orders.add(order);
        }

        patentService.addOrders(orders);

        return "完成";
    }

    @RequestMapping("/import")
    public String importPatent(Model model) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<Template> templates = patentService.getTemplates(user.getId());
        model.addAttribute("templates", templates);
        return "common/import";
    }

//    @ResponseBody
//    @RequestMapping(value = "/fileupload", produces = "application/json;charset=UTF-8")
//    public Object fileUpload(@RequestParam(required = true) int id,
//        @RequestParam("files[]") MultipartFile file) {
//        Map<Object, Object> info = new HashMap<Object, Object>();
//        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");// 设置日期格式
//        String dateDir = "E:\\upload\\" + df.format(new Date());// new Date()为获取当前系统时间
//
//        FileUploadHelper.upload(file, dateDir);
//
//        try {
//            Workbook wb = WorkbookFactory.create(file.getInputStream());
//            Sheet sheet = wb.getSheetAt(0);
//            int count = sheet.getLastRowNum();
//
//            for (int i = 1;i < count; i++) {
//                Row row = sheet.getRow(i);
//
//                CpqueryResult cpqueryResult = new CpqueryResult();
//                cpqueryResult.setApplicationNumber(row.getCell(0).getStringCellValue());
//                cpqueryResult.setCpqueryId(1);
//                cpqueryResult.setPatentId(i+900);
//                cpqueryResult.setStatus(row.getCell(6).getStringCellValue());
//                cpqueryResult.setUpdateTime(new Date());
//
//                cpqueryResultMapper.insert(cpqueryResult);
//            }
//
//            wb.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InvalidFormatException e) {
//            e.printStackTrace();
//        }
//
//
//        info.put("file", dateDir + "\\" + file.getOriginalFilename());
//
//        return info;
//    }
    @ResponseBody
    @RequestMapping(value = "/fileupload", produces = "application/json;charset=UTF-8")
    public Object fileUpload(@RequestParam(required = true) int id,
        @RequestParam("files[]") MultipartFile file) {
        Map<Object, Object> info = new HashMap<Object, Object>();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");// 设置日期格式
        String dateDir = "E:/upload/temp/";// new Date()为获取当前系统时间

        FileUploadHelper.upload(file, dateDir,file.getOriginalFilename());
        List<TemplateProperty> porp = patentService.getTemplateProperty(id);
        List<TemplateProperty> title = new ArrayList<>();
        try {
            Workbook wb = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = wb.getSheetAt(0);
            Row row = sheet.getRow(0);

            int i = 0;
            while (row.getCell(i) != null) {
                TemplateProperty templateProperty = new TemplateProperty();
                Cell cell = row.getCell(i);
                String value = cell.getStringCellValue();
                templateProperty.setSortId(i + 1);
                templateProperty.setName(value);
                title.add(templateProperty);
                ++i;
            }
            wb.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

        info.put("porp", porp);
        info.put("title", title);
        info.put("file", dateDir + "\\" + file.getOriginalFilename());

        return info;
    }

    @ResponseBody
    @RequestMapping(value = "/importData", produces = "application/json;charset=UTF-8")
    public Object importData(@RequestParam("id") int id,
        @RequestParam("file") String file,
        @RequestParam("order") String order) {

        Map<Object, Object> info = new HashMap<Object, Object>();
        Template template = new Template();
        template.setId(id);
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        //List<Patent> patents = new ArrayList<>();
        int lastPatentID = patentService.getPatentNewID();
        List<Integer> nums = ParameterHelper.StringToIntegerList(order, ",");

        try {
            InputStream inp = new FileInputStream(file);
            Workbook wb = WorkbookFactory.create(inp);
            Sheet sheet = wb.getSheetAt(0);
            int count = sheet.getLastRowNum()+1;

            for (int i = 1; i < count; i++) {
                Patent patent = new Patent();
                patent.setId(lastPatentID + i - 1);
                patent.setTemplateId(template.getId());
                patent.setCreator(user);
                patentService.importPatent(patent);

                Row row = sheet.getRow(i);
                for (int index = 0; index < nums.size(); index++) {

                    Cell cell = row.getCell(nums.get(index) - 1);

                    if (cell != null) {
                        CellType type = cell.getCellTypeEnum();


                        String value = null;
                        if (type.getCode() == 0) {
                            Date vls = cell.getDateCellValue();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                            value = formatter.format(vls);


                        } else if (type.getCode() == 2){

                        } else {
                            value = cell.getStringCellValue();

                        }

                        if (value != null && value.length() > 0) {
                            PatentProperty patentProperty = new PatentProperty();
                            patentProperty.setPatentId(lastPatentID + i - 1);
                            patentProperty.setSortId(index + 1);
                            patentProperty.setValue(value);

                            patentService.editProperty(patentProperty);
                        }



                    }
                }

            }

            wb.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        info.put("state", 1);
        return info;
    }

    @RequestMapping("/order")
    public String order(Model model) {

        return "common/order";
    }

    @RequestMapping("/flipbook")
    public String flipbook(Model model) {

        return "backstage/flipbook";
    }

}
