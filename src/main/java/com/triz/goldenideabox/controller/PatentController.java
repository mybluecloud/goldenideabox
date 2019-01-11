package com.triz.goldenideabox.controller;

import com.alibaba.fastjson.JSONObject;
import com.triz.goldenideabox.common.helper.FileUploadHelper;
import com.triz.goldenideabox.common.helper.ParameterHelper;
import com.triz.goldenideabox.model.DashBoard;
import com.triz.goldenideabox.model.DisplayConfig;
import com.triz.goldenideabox.model.Document;
import com.triz.goldenideabox.model.Order;
import com.triz.goldenideabox.model.Patent;
import com.triz.goldenideabox.model.PatentProperty;
import com.triz.goldenideabox.model.PatentRecord;
import com.triz.goldenideabox.model.Template;
import com.triz.goldenideabox.model.TemplateProperty;
import com.triz.goldenideabox.model.User;
import com.triz.goldenideabox.service.DocumentService;
import com.triz.goldenideabox.service.PatentService;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/patent")
public class PatentController {

    @Value("${goldenideabox.path.file}")
    private String filePath;

    @Value("${goldenideabox.path.temp}")
    private String excelPath;


    @Autowired
    private PatentService patentService;

    @Autowired
    private DocumentService documentService;

    @ResponseBody
    @RequestMapping(value = "/appointorder", produces = "application/json;charset=UTF-8")
    public Object appointOrder() {

        Map<Object, Object> info = new HashMap<Object, Object>();

        List<Order> lst = patentService.getAppointOrder();
        info.put("data", lst);

        return info;
    }

    @ResponseBody
    @RequestMapping(value = "/claimorder", produces = "application/json;charset=UTF-8")
    public Object claimOrder() {

        Map<Object, Object> info = new HashMap<Object, Object>();

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<Order> lst = patentService.getClaimOrder(user.getId());
        info.put("data", lst);

        return info;
    }

    @ResponseBody
    @RequestMapping(value = "/users", produces = "application/json;charset=UTF-8")
    public Object checkUserList(
        @RequestParam(required = true) String resUrl) {

        List<DashBoard> lst = patentService.getUserList(resUrl);
        Map<Object, Object> info = new HashMap<Object, Object>();
        info.put("data", lst);
        return info;
    }

    @ResponseBody
    @RequestMapping(value = "/appoint", produces = "application/json;charset=UTF-8")
    public int appointPatent(
        @RequestParam(required = true) int userID,
        @RequestParam(required = true) String orderIDs) {

        return patentService
            .appointPatent(userID, ParameterHelper.StringToIntegerList(orderIDs, ","));
    }

    @ResponseBody
    @RequestMapping(value = "/noclaimpatent", produces = "application/json;charset=UTF-8")
    public Object noClaimPatent() {
        List<Patent> lst = patentService.getNoClaimPatent();
        Map<Object, Object> info = new HashMap<Object, Object>();
        info.put("data", lst);
        return info;
    }

    @ResponseBody
    @RequestMapping(value = "/templates", produces = "application/json;charset=UTF-8")
    public Object templates() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<Template> lst = patentService.getTemplates(user.getId());

        return lst;
    }

    @ResponseBody
    @RequestMapping(value = "/templateproperty", produces = "application/json;charset=UTF-8")
    public Object templateProperty(@RequestParam(required = true) int id,
        @RequestParam(required = true) int patentID) {

        patentService.applyTemplate(patentID, id);
        List<TemplateProperty> lst = patentService.getTemplateProperty(id);

        return lst;
    }

    @ResponseBody
    @RequestMapping(value = "/edit", produces = "application/json;charset=UTF-8")
    public Object editProperty(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) int pk,
        @RequestParam(required = false) String value) {

        Map<Object, Object> info = new HashMap<Object, Object>();
        int result = 0;

        PatentProperty property = new PatentProperty();
        property.setPatentId(pk);
        property.setSortId(Integer.valueOf(name));
        property.setValue(value);
        result = patentService.editProperty(property);

        info.put("result", result);
        return info;
    }

    @ResponseBody
    @RequestMapping(value = "/upload", produces = "application/json;charset=UTF-8")
    public Object uploadFile(@RequestParam("files[]") MultipartFile file,
        @RequestParam("sortId") int sortId,
        @RequestParam("patentId") int patentId) {
        Map<Object, Object> info = new HashMap<Object, Object>();

        String path = filePath + patentId + "//";
        String filemame = UUID.randomUUID().toString().replace("-", "").toLowerCase()
            + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        int result = 0;

        result = FileUploadHelper.upload(file, path, filemame);

        if (result != 0) {
            info.put("result", result);
            info.put("file", file.getOriginalFilename());
            return info;
        }

        String md5 = FileUploadHelper.getMD5HexCode(path + filemame);

        List<Document> docs = documentService.getDocumentByMd5hex(md5);

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Document document = new Document();
        document.setName(file.getOriginalFilename());
        if (docs.size() > 0) {
            FileUploadHelper.delete(path + filemame);
            document.setPath(docs.get(0).getPath());
        } else {
            document.setPath(path + filemame);
        }

        document.setCategory(1);
        document.setPatentId(patentId);
        document.setUserId(user.getId());
        document.setTime(new Date());
        document.setMd5hex(md5);
        result = documentService.upload(document);

        PatentProperty property = new PatentProperty();
        property.setName(document.getName());
        property.setPatentId(patentId);
        property.setSortId(sortId);
        property.setValue(document.getId() + ",");
        result = patentService.editFileProperty(property, document);

        info.put("id", document.getId());
        info.put("result", result);
        info.put("file", file.getOriginalFilename());

        return info;
    }

    @RequestMapping(value = "/download", produces = "application/json;charset=UTF-8")
    public String download(@RequestParam("id") int id, HttpServletResponse response) {

        Document doc = documentService.getDocumentByID(id);
        if (doc.getName() != null) {

            File file = new File(doc.getPath());
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            if (file.exists()) {
                try {
                    //response.setContentType("application/force-download");
                    response.setContentType("application/octet-stream");
                    String name = URLEncoder.encode(doc.getName(), "UTF-8").replaceAll("\\+", "%20").replaceAll("%28", "\\(").replaceAll("%29", "\\)").replaceAll("%3B", ";").replaceAll("%40", "@").replaceAll("%23", "\\#").replaceAll("%26", "\\&").replaceAll("%2C", "\\,");
                    response.addHeader("Content-Disposition", "attachment;fileName=" + name);
                    byte[] buffer = new byte[1024];

                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    return "下载成功";
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return "下载失败";
    }

    @ResponseBody
    @RequestMapping(value = "/deletefile", produces = "application/json;charset=UTF-8")
    public Object deleteFile(@RequestParam("sortId") int sortId,
        @RequestParam("patentId") int patentId,
        @RequestParam(required = false) int id) {

        Map<Object, Object> info = new HashMap<Object, Object>();
        int result = 0;

        Document document = documentService.getDocumentByID(id);

        List<Document> docs = documentService.getDocumentByMd5hex(document.getMd5hex());
        if (docs.size() == 1) {
            result = FileUploadHelper.delete(filePath + document.getPath());
            if (result != 0) {
                info.put("result", result);
                return info;
            }
        }

        documentService.deleteDocument(document);

        PatentProperty property = new PatentProperty();
        property.setPatentId(patentId);
        property.setSortId(sortId);
        property.setValue(document.getId() + ",");

        result = patentService.deleteFileProperty(property,document);
        info.put("result", result);

        return info;
    }

    @ResponseBody
    @RequestMapping(value = "/patentTable", produces = "application/json;charset=UTF-8")
    public Object patentTable(@RequestParam("configId") int configId,
        @RequestParam("templateId") int templateId,
        @RequestParam("filter") String filter,
        @RequestParam("order") String order,
        @RequestParam("visible") String visible,
        @RequestParam("left") int left,
        @RequestParam("right") int right,
        @RequestParam("pageLength") int pageLength) {

        Map<Object, Object> info = new HashMap<Object, Object>();
        if (configId == 0) {

            List<Map<String, String>> columns = patentService.selectPropertyName(templateId, order, visible);

            info.put("columns", columns);
            info.put("order", order);
            info.put("visible", visible);
            info.put("left", left);
            info.put("right", right);
            info.put("filter", filter);
            info.put("pageLength", pageLength);

        } else {
            DisplayConfig config = patentService.getDisplayConfig(configId);

            if (filter.length() > 0) {
                config.setFilter(filter);
            }
            if (order.length() > 0) {
                config.setOrderList(order);
            }
            if (visible.length() > 0) {
                config.setVisibleList(visible);
            }
            if (left > 0) {
                config.setLeftFixedSum(left);
            }
            if (right > 0) {
                config.setRightFixedSum(right);
            }

            List<Map<String, String>> columns = patentService
                .selectPropertyName(config.getTemplateId(), config.getOrderList(), config.getVisibleList());

            info.put("columns", columns);
            info.put("order", config.getOrderList());
            info.put("visible", config.getVisibleList());
            info.put("left", config.getLeftFixedSum());
            info.put("right", config.getRightFixedSum());
            info.put("filter", config.getFilter());
            info.put("pageLength", config.getPageLength());
        }

        return info;
    }

    @ResponseBody
    @RequestMapping(value = "/patents", produces = "application/json;charset=UTF-8")
    public Object patents(HttpServletRequest request, @RequestParam("configId") int configId,
        @RequestParam("templateId") int templateId,
        @RequestParam("filter") String filter,
        @RequestParam("draw") int draw,
        @RequestParam("start") int start,
        @RequestParam("length") int length,
        @RequestParam("appoint") boolean appoint

    ) {
        int userId = 0;

        String orderIndex = "";
        String orderColumn = request.getParameter("order[0][column]");
        String order = request.getParameter("order[0][dir]");

        if (orderColumn != null) {
            orderIndex = request.getParameter("columns[" + orderColumn + "][data]");
        }

        String search = request.getParameter("search[value]");

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (!appoint) {
            userId = user.getId();
        }

        Map<Object, Object> info = new HashMap<Object, Object>();
        if (configId == 0) {
            List<JSONObject> lst = patentService.getPatents(userId, templateId, filter, start, length, orderIndex, order, search);

            info.put("data", lst);

        } else {
            DisplayConfig config = patentService.getDisplayConfig(configId);

            if (filter.length() > 0) {
                config.setFilter(filter);
            }

            List<JSONObject> lst = patentService
                .getPatents(userId, config.getTemplateId(), config.getFilter(), start, length, orderIndex, order, search);

            info.put("data", lst);

        }

        int count = patentService.getPatentCount(userId, templateId, filter, search);
        info.put("draw", draw);
        info.put("recordsTotal", count);
        info.put("recordsFiltered", count);

        return info;
    }

    @ResponseBody
    @RequestMapping(value = "/saveLable", produces = "application/json;charset=UTF-8")
    public Object saveLable(
        @RequestParam("configId") int configId,
        @RequestParam("configname") String configname,
        @RequestParam("templateId") int templateId,
        @RequestParam("filter") String filter,
        @RequestParam("order") String order,
        @RequestParam("visible") String visible,
        @RequestParam("left") int left,
        @RequestParam("right") int right,
        @RequestParam("pageLength") int pageLength) {

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        DisplayConfig config = new DisplayConfig();
        if (configId == 0) {
            config.setUserId(user.getId());
            config.setConfigname(configname);
            config.setTemplateId(templateId);
            config.setFilter(filter);
            config.setOrderList(order);
            config.setVisibleList(visible);
            config.setLeftFixedSum(left);
            config.setRightFixedSum(right);
            config.setPageLength(pageLength);
            patentService.saveDisplayConfig(config);


        } else {
            config.setId(configId);
            config.setFilter(filter);
            config.setOrderList(order);
            config.setVisibleList(visible);
            config.setLeftFixedSum(left);
            config.setRightFixedSum(right);
            config.setPageLength(pageLength);
            patentService.updateDisplayConfig(config);
        }
        return config;
    }

    @ResponseBody
    @RequestMapping(value = "/renameLable", produces = "application/json;charset=UTF-8")
    public Object renameLable(
        @RequestParam("configId") int configId,
        @RequestParam("configname") String configname) {

        DisplayConfig config = new DisplayConfig();

        config.setId(configId);
        config.setConfigname(configname);

        patentService.updateDisplayConfig(config);

        return config;
    }

    @ResponseBody
    @RequestMapping(value = "/deleteLable", produces = "application/json;charset=UTF-8")
    public Object deleteLable(
        @RequestParam(required = true) int configId) {
        return patentService.deleteDisplayConfig(configId);
    }

    @ResponseBody
    @RequestMapping(value = "/search", produces = "application/json;charset=UTF-8")
    public Object getTemplate(
        @RequestParam(required = true) int templateId) {

        List<TemplateProperty> lst = patentService.getSearchCondition(templateId);

        return lst;
    }

    @ResponseBody
    @RequestMapping(value = "/patentproperty", produces = "application/json;charset=UTF-8")
    public Object patentProperty(@RequestParam(required = true) int id) {

        Map<Object, Object> info = new HashMap<Object, Object>();
        Map<Integer, String> propertys = patentService.getPatentProperty(id);
        List<Document> docs = documentService.getDocumentByPatentID(id);
        info.put("propertys", propertys);
        info.put("documents", docs);
        return info;
    }

    @ResponseBody
    @RequestMapping(value = "/export", produces = "application/json;charset=UTF-8")
    public String export(
        @RequestParam(required = true) int configId,
        @RequestParam(required = true) int templateId,
        @RequestParam("appoint") boolean appoint) {

        int userId = 0;
        String fileName = "";
        List<JSONObject> lst = null;
        List<Map<String, String>> columns = null;

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if (!appoint) {
            userId = user.getId();
        }

        if (configId == 0) {

            lst = patentService
                .getPatents(userId, templateId, "", 0, 0, "", "", "");

            columns = patentService
                .selectPropertyName(templateId, "", "");
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmm");
            fileName = dateFormat.format(new Date());
        } else {
            DisplayConfig config = patentService.getDisplayConfig(configId);

            lst = patentService
                .getPatents(userId, config.getTemplateId(), config.getFilter(), 0, 0, "", "", "");

            columns = patentService
                .selectPropertyName(config.getTemplateId(), config.getOrderList(), config.getVisibleList());

            fileName = config.getConfigname();
        }

        Workbook wb = new XSSFWorkbook();
        try {

            Sheet sheet = wb.createSheet(fileName);

            Row name = sheet.createRow(0);

            CellStyle style = wb.createCellStyle();
            style.setBorderBottom(BorderStyle.THIN);
            style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            style.setBorderLeft(BorderStyle.THIN);
            style.setLeftBorderColor(IndexedColors.GREEN.getIndex());
            style.setBorderRight(BorderStyle.THIN);
            style.setRightBorderColor(IndexedColors.BLUE.getIndex());
            style.setBorderTop(BorderStyle.MEDIUM_DASHED);
            style.setTopBorderColor(IndexedColors.BLACK.getIndex());
            style.setAlignment(HorizontalAlignment.CENTER);
            Font font = wb.createFont();
            font.setBold(true);
            style.setFont(font);

            int i = 0;
            Map<String, Integer> sorts = new HashMap<String, Integer>();
            for (Map<String, String> column : columns) {
                Object visible = column.get("visible");
                if (!(column.containsKey("visible") && ((Long) visible).intValue() == 0)) {
                    Cell cell = name.createCell(i);
                    cell.setCellValue(column.get("title"));
                    cell.setCellStyle(style);

                    int length = cell.getStringCellValue().getBytes().length;
                    int columnWidth = sheet.getColumnWidth(i) / 256;
                    if (columnWidth < length + 1) {
                        columnWidth = length + 1;
                    }
                    sheet.autoSizeColumn(i);
                    sheet.setColumnWidth(i, columnWidth * 256);

                    sorts.put(column.get("data"), i);

                    i++;
                }
            }
            i = 1;
            for (JSONObject datas : lst) {
                Row row = sheet.createRow(i);

                for (Map.Entry<String, Integer> entry : sorts.entrySet()) {
                    Cell cell = row.createCell(entry.getValue());
                    String value = datas.getString(String.valueOf(entry.getKey()));
                    cell.setCellValue(value);
                }

                i++;
            }

            OutputStream fileOut = new FileOutputStream(excelPath + fileName + ".xlsx");
            wb.write(fileOut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return JSONObject.toJSONString("/temp/" + fileName + ".xlsx");
    }


    @RequestMapping(value = "/appointpatent")
    public String appointPatent(Model model) {

        return "backstage/appoint_patent";
    }

    @RequestMapping(value = "/claimpatent")
    public String claimPatent(Model model) {

        return "backstage/claim_patent";
    }

    @RequestMapping(value = "/patentlist")
    public String queryPatent(Model model) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<DisplayConfig> configs = patentService.getDisplayConfigs(user.getId());
        List<Template> templates = patentService.getTemplates(user.getId());

        model.addAttribute("configs", configs);
        model.addAttribute("templates", templates);
        return "backstage/query_patent";
    }

    @RequestMapping(value = "/patent")
    public String patent(@RequestParam(required = false) int id, Model model) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Patent patent = null;
        if (id == 0) {

            patent = new Patent();
            patent.setCreator(user);
            patent.setState(0);
            patentService.newPatent(patent);

            id = patent.getId();
        } else {
            patent = patentService.getPatent(id);
            List<PatentRecord> records = patentService.getPatentRecords(id);
            model.addAttribute("records", records);
        }
        List<Template> templates = patentService.getTemplates(user.getId());

        model.addAttribute("patent", patent);
        model.addAttribute("templates", templates);
        return "backstage/patent";
    }

    @RequestMapping(value = "/newpatent")
    public String newpatent(@RequestParam(required = false) int id, Model model) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();

        Patent patent = patentService.getPatentByOrderID(id);

        List<Template> templates = patentService.getTemplates(user.getId());

        model.addAttribute("patent", patent);
        model.addAttribute("templates", templates);
        return "backstage/patent";
    }
}
