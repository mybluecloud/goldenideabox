package com.triz.goldenideabox.service;

import com.triz.goldenideabox.model.DashBoard;
import com.triz.goldenideabox.model.DisplayConfig;
import com.triz.goldenideabox.model.Document;
import com.triz.goldenideabox.model.Order;
import com.triz.goldenideabox.model.Patent;
import com.triz.goldenideabox.model.PatentProperty;
import com.triz.goldenideabox.model.PatentRecord;
import com.triz.goldenideabox.model.Template;
import com.triz.goldenideabox.model.TemplateProperty;
import java.util.List;
import java.util.Map;

public interface PatentService {

    List<Order> getAppointOrder();

    List<DashBoard> getUserList(String resUrl);

    int appointPatent(int userID, List<Integer> orderIDs);

    List<Patent> getNoClaimPatent();

    List<Order> getClaimOrder(int id);

    List<Template> getTemplates(int userID);

    List<TemplateProperty> getTemplateProperty(int id);

    int editFileProperty(PatentProperty property, Document document);

    int deleteFileProperty(PatentProperty property, Document document);

    int editProperty(PatentProperty property);

    int applyTemplate(int patentID, int id);

    List<com.alibaba.fastjson.JSONObject> getPatents(int id, int templateId, String filter,int start,int length,String orderIndex,String order,String search);

    List<Map<String,String>> selectPropertyName(int templateId, String sort, String visible);

    DisplayConfig getDisplayConfig(int configId);

    List<DisplayConfig> getDisplayConfigs(int userId);

    void saveDisplayConfig(DisplayConfig config);

    void updateDisplayConfig(DisplayConfig config);

    int newPatent(Patent patent);

    List<TemplateProperty> getSearchCondition(int templateId);

    Patent getPatent(int patentId);

    Map<Integer,String> getPatentProperty(int id);

    int getPatentNewID();

    int importPatent(Patent patent);


    int deleteDisplayConfig(int configId);

    int getPatentCount(int id, int templateId, String filter,String search);

    List<TemplateProperty> getTemplatePropertyByType(Integer id, int type);

    void addOrders(List<Order> orders);

    Patent getPatentByOrderID(int id);

    List<PatentRecord> getPatentRecords(int id);


}
