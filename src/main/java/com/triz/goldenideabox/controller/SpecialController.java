package com.triz.goldenideabox.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.triz.goldenideabox.common.helper.FileUploadHelper;
import com.triz.goldenideabox.common.helper.ParameterHelper;
import com.triz.goldenideabox.dao.CpqueryResultMapper;
import com.triz.goldenideabox.model.Order;
import com.triz.goldenideabox.model.Patent;
import com.triz.goldenideabox.model.PatentProperty;
import com.triz.goldenideabox.model.Template;
import com.triz.goldenideabox.model.TemplateProperty;
import com.triz.goldenideabox.model.User;
import com.triz.goldenideabox.service.PatentService;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/special")
public class SpecialController {

    @Autowired
    private PatentService patentService;



    @Value("${goldenideabox.path.temp}")
    private String temp;



    @RequestMapping("/import")
    public String importPatent(Model model) {

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        List<Template> templates = patentService.getTemplates(user.getId());
        model.addAttribute("templates", templates);
        return "backstage/import";
    }

    @ResponseBody
    @RequestMapping(value = "/fileupload", produces = "application/json;charset=UTF-8")
    public Object fileUpload(@RequestParam("files[]") MultipartFile file) {
        Map<Object, Object> info = new HashMap<Object, Object>();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");// 设置日期格式

        FileUploadHelper.upload(file, temp, file.getOriginalFilename());

        List<String> sheets = new ArrayList<>();
        try {
            Workbook wb = WorkbookFactory.create(file.getInputStream());
            for (int i = 0; i < wb.getNumberOfSheets(); ++i) {
                sheets.add(wb.getSheetName(i));
            }

            wb.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

        info.put("sheets", sheets);

        info.put("file", temp + "\\" + file.getOriginalFilename());

        return info;
    }


    @ResponseBody
    @RequestMapping(value = "/parseTemplate", produces = "application/json;charset=UTF-8")
    public Object parseTemplate(@RequestParam("templateID") int templateID,
            @RequestParam("sheetID") int sheetID,
            @RequestParam("file") String file) {
        Map<Object, Object> info = new HashMap<Object, Object>();
        List<String> title = new ArrayList<>();
        try {
            InputStream inp = new FileInputStream(file);
            Workbook wb = WorkbookFactory.create(inp);
            Sheet sheet = wb.getSheetAt(sheetID);

            Row row = sheet.getRow(0);
            for (int i = 0; i < row.getLastCellNum(); ++i) {

                title.add(ParameterHelper.replaceSpecialStr(row.getCell(i).getStringCellValue()));
            }

            wb.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

        List<TemplateProperty> props = patentService.getTemplateProperty(templateID);
        List<JSONObject> config = patentService.getImportTemplate(templateID);

        info.put("props", props);
        info.put("config", config);
        info.put("titles", title);
        return info;
    }

    @ResponseBody
    @RequestMapping(value = "/parseData", produces = "application/json;charset=UTF-8")
    public Object parseData(@RequestParam("templateID") int templateID,
            @RequestParam("sheetID") int sheetID,
            @RequestParam("file") String file,
            @RequestParam("config") String config,
            @RequestParam("save") boolean bSave,
            @RequestParam("filter") int filter) {

        Map<Object, Object> info = new HashMap<Object, Object>();

        User user = (User) SecurityUtils.getSubject().getPrincipal();

        Map<String, Integer> configs = new HashMap<>();
        Map<Integer, Integer> indexs = new HashMap<>();
        List<Integer> ids = new ArrayList<>();
        JSONArray rules = JSON.parseArray(config);
        for (int i = 0; i < rules.size(); ++i) {

            JSONObject rule = rules.getJSONObject(i);

            String name = rule.getString("name");
            int sortId = rule.getIntValue("sortId");

            configs.put(name, sortId);
            ids.add(sortId);
        }

        if (bSave) {
            patentService.deleteImportConfig(templateID);
            patentService.saveImportConfig(templateID, configs);
        }

        List<Map<String, String>> newPatentData = new ArrayList<>();

        try {
            InputStream inp = new FileInputStream(file);
            Workbook wb = WorkbookFactory.create(inp);
            Sheet sheet = wb.getSheetAt(sheetID);

            Row row = sheet.getRow(0);
            for (int i = 0; i < row.getLastCellNum(); ++i) {

                String title = ParameterHelper.replaceSpecialStr(row.getCell(i).getStringCellValue());

                if (configs.containsKey(title)) {
                    indexs.put(i, configs.get(title));
                }
            }

            int count = sheet.getLastRowNum() + 1;

            for (int i = 1; i < count; i++) {
                row = sheet.getRow(i);
                String filterValue = "";
                JSONArray recordJson = new JSONArray();
                Map<String, String> record = new HashMap<>();
                for (Integer index : indexs.keySet()) {
                    String value = getCellValue(row, index);
                    if (value != null && value.trim().length() > 0) {
                        record.put(String.valueOf(indexs.get(index)), ParameterHelper.replaceSpecialChar(value));
                        JSONObject prop = new JSONObject();
                        prop.put("sortId", indexs.get(index));
                        prop.put("value", ParameterHelper.replaceSpecialChar(value));
                        recordJson.add(prop);
                    }

                    if (filter == indexs.get(index)) {
                        filterValue = ParameterHelper.replaceSpecialChar(value);
                    }

                }

                if (filterValue != null && record.size() > 0) {

                    Map<Integer, List<JSONObject>> patent = patentService.getPatentProperty(templateID, filter, filterValue,ids);

                    if (patent == null) {
                        if (record.size() > 0) {
                            record.put("id", String.valueOf(i));
                            newPatentData.add(record);
                        }

                    } else {

                        JSONObject patentObj = JSONObject.parseObject(ParameterHelper.replaceSpecialChar(JSONObject.toJSONString(patent)));

                        JSONArray patentProps = patentObj.getJSONArray("props");

                        patentProps.sort(Comparator.comparing(obj -> ((JSONObject) obj).getBigDecimal("sortId")));
                        recordJson.sort(Comparator.comparing(obj -> ((JSONObject) obj).getBigDecimal("sortId")));

                        if (!patentProps.equals(recordJson)) {

                            record.put("id", String.valueOf(i));
                            record.put("patent", patentObj.toJSONString());
                            newPatentData.add(record);
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

        List<Map<String, String>> columns = patentService
                .selectPropertyName(templateID, null, null);

        info.put("columns", columns);
        info.put("datas", newPatentData);
        return info;
    }

    @ResponseBody
    @RequestMapping(value = "/importData", produces = "application/json;charset=UTF-8")
    public Object importData(@RequestParam("templateID") int templateID,
            @RequestParam("sheetID") int sheetID,
            @RequestParam("file") String file,
            @RequestParam("config") String config,
            @RequestParam("patents") String patentJson) {


        User user = (User) SecurityUtils.getSubject().getPrincipal();

        Map<Object, Object> info = new HashMap<>();
        Map<String, Integer> configs = new HashMap<>();
        Map<Integer, Integer> indexs = new HashMap<>();
        JSONArray rules = JSON.parseArray(config);

        for (int i = 0; i < rules.size(); ++i) {

            JSONObject rule = rules.getJSONObject(i);

            configs.put(rule.getString("name"), rule.getIntValue("sortId"));
        }

        try {
            InputStream inp = new FileInputStream(file);
            Workbook wb = WorkbookFactory.create(inp);
            Sheet sheet = wb.getSheetAt(sheetID);

            Row row = sheet.getRow(0);
            for (int i = 0; i < row.getLastCellNum(); ++i) {

                String title = ParameterHelper.replaceSpecialStr(row.getCell(i).getStringCellValue());

                if (configs.containsKey(title)) {
                    indexs.put(i, configs.get(title));
                }
            }

            JSONArray patents = JSON.parseArray(patentJson);

            for (int i = 0; i < patents.size(); i++) {
                JSONObject obj = patents.getJSONObject(i);
                Map<Integer, JSONObject> props = new HashMap<>();
                int id = obj.getIntValue("id");
                Patent patent;
                if (id == 0) {
                    patent = new Patent();
                    patent.setTemplateId(templateID);
                    patent.setCreator(user);
                    patentService.newPatent(patent);
                    id = patent.getId();
                } else {
                    props = patentService.getPatentProperty(id);
                }

                row = sheet.getRow(obj.getIntValue("row"));

                for (Integer index : indexs.keySet()) {
                    String cellValue = getCellValue(row, index);
                    int sortId = indexs.get(index);
                    if (props.containsKey(sortId)) {

                        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(props.get(sortId)));

                        String value = jsonObject.getString("value");
                        if (value != null && value.equals(cellValue)) {
                            continue;
                        }
                    }

                    PatentProperty property = new PatentProperty();
                    property.setPatentId(id);
                    property.setSortId(sortId);
                    property.setValue(cellValue);
                    patentService.editProperty(property);

                }

            }

            wb.close();

        } catch (Exception e) {
            info.put("error", e.getMessage());
            e.printStackTrace();
        }

        return info;
    }

    private String getCellValue(Row row, int index) {
        String value = "";
        Cell cell = row.getCell(index);
        if (cell != null) {
            CellType type = cell.getCellTypeEnum();
            if (type == CellType.NUMERIC) {
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date vls = cell.getDateCellValue();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                    value = formatter.format(vls);
                } else {
                    value = new DecimalFormat("0").format(cell.getNumericCellValue());
                }

            } else if (type == CellType.FORMULA) {
                value = "";
            } else {
                value = cell.getStringCellValue();
            }

        }
        return value;
    }



}
