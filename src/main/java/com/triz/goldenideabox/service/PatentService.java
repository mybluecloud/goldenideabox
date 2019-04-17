package com.triz.goldenideabox.service;

import com.alibaba.fastjson.JSONObject;
import com.triz.goldenideabox.model.CpqueryAnnounceInfo;
import com.triz.goldenideabox.model.CpqueryApplicationInfo;
import com.triz.goldenideabox.model.CpqueryCostInfo;
import com.triz.goldenideabox.model.CpqueryPostInfo;
import com.triz.goldenideabox.model.CpqueryReviewInfo;
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

    List<JSONObject> getImportTemplate(int id);

    int editFileProperty(PatentProperty property, Document document);

    int deleteFileProperty(PatentProperty property, Document document);

    int editProperty(PatentProperty property);

    int applyTemplate(int patentID, int id);

    List<JSONObject> getPatents(int id, int templateId, String filter,int start,int length,String orderIndex,String order,String search);

    List<Map<String,String>> selectPropertyName(int templateId, String sort, String visible);

    DisplayConfig getDisplayConfig(int configId);

    List<DisplayConfig> getDisplayConfigs(int userId);

    void saveDisplayConfig(DisplayConfig config);

    void updateDisplayConfig(DisplayConfig config);

    int newPatent(Patent patent);

    List<TemplateProperty> getSearchCondition(int templateId);

    Patent getPatent(int patentId);

    Map<Integer,JSONObject> getPatentProperty(int id);

    Map<Integer,List<JSONObject>> getPatentProperty(int templateId,int sortId,String value,List<Integer> ids);

    int getPatentNewID();

    int importPatent(Patent patent);


    int deleteDisplayConfig(int configId);

    int getPatentCount(int id, int templateId, String filter,String search);

    List<TemplateProperty> getTemplatePropertyByType(Integer id, int type);

    void addOrders(List<Order> orders);

    Patent getPatentByOrderID(int id);

    List<PatentRecord> getPatentRecords(int id);


    String getPatentApplicationNumber(int id);

    CpqueryApplicationInfo getCpqueryApplication(String applicationNumber);

    List<CpqueryReviewInfo> getCpqueryReview(String applicationNumber);

    CpqueryCostInfo getCpqueryCost(String applicationNumber);

    CpqueryPostInfo getCpqueryPost(String applicationNumber);

    CpqueryAnnounceInfo getCpqueryAnnounce(String applicationNumber);

    int saveImportConfig(int templateID, Map<String,Integer> configs);

    int deleteImportConfig(int templateID);
}
