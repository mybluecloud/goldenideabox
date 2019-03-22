package com.triz.goldenideabox.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.triz.goldenideabox.dao.CpqueryAnnounceInfoMapper;
import com.triz.goldenideabox.dao.CpqueryApplicationInfoMapper;
import com.triz.goldenideabox.dao.CpqueryCostInfoMapper;
import com.triz.goldenideabox.dao.CpqueryPostInfoMapper;
import com.triz.goldenideabox.dao.CpqueryResultMapper;
import com.triz.goldenideabox.dao.CpqueryReviewInfoMapper;
import com.triz.goldenideabox.dao.PatentRecordMapper;
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
import com.triz.goldenideabox.dao.DashBoardMapper;
import com.triz.goldenideabox.dao.DisplayConfigMapper;
import com.triz.goldenideabox.dao.OrderMapper;
import com.triz.goldenideabox.dao.PatentMapper;
import com.triz.goldenideabox.dao.PatentPropertyMapper;
import com.triz.goldenideabox.dao.TemplateMapper;
import com.triz.goldenideabox.dao.TemplatePropertyMapper;
import com.triz.goldenideabox.service.PatentService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("PatentService")
public class PatentServiceImpl implements PatentService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private PatentMapper patentMapper;

    @Autowired
    private PatentPropertyMapper patentPropertyMapper;

    @Autowired
    private DashBoardMapper dashBoardMapper;

    @Autowired

    private TemplateMapper templateMapper;

    @Autowired
    private TemplatePropertyMapper templatePropertyMapper;

    @Autowired
    private DisplayConfigMapper displayConfigMapper;

    @Autowired
    private PatentRecordMapper patentRecordMapper;


    @Autowired
    private CpqueryResultMapper cpqueryResultMapper;

    @Autowired
    private CpqueryApplicationInfoMapper cpqueryApplicationInfoMapper;

    @Autowired
    private CpqueryReviewInfoMapper cpqueryReviewInfoMapper;

    @Autowired
    private CpqueryCostInfoMapper cpqueryCostInfoMapper;

    @Autowired
    private CpqueryPostInfoMapper cpqueryPostInfoMapper;

    @Autowired
    private CpqueryAnnounceInfoMapper cpqueryAnnounceInfoMapper;

    @Override
    public List<Order> getAppointOrder() {

        return orderMapper.selectAppointOrder();
    }

    @Override
    public void addOrders(List<Order> orders) {
        for (Order order:orders) {
            orderMapper.insertSelective(order);
        }
    }

    @Override
    public Patent getPatentByOrderID(int id) {
        return patentMapper.getPatentByOrderID(id);
    }

    @Override
    public List<PatentRecord> getPatentRecords(int id) {
        return patentRecordMapper.getPatentRecords(id);
    }

    @Override
    public List<DashBoard> getUserList(String resUrl) {
        return dashBoardMapper.selectPatentCount(resUrl);
    }

    @Override
    public int appointPatent(int userID, List<Integer> orderIDs) {

        return patentMapper.appointPatent(userID,orderIDs);
    }

    @Override
    public List<Patent> getNoClaimPatent() {
        return patentMapper.getNoClaimPatent();
    }

    @Override
    public List<Order> getClaimOrder(int userID) {

        return orderMapper.selectClaimOrder(userID);
    }

    @Override
    public List<Template> getTemplates(int userID) {
        return templateMapper.selectReviewTemplateByUser(userID);
    }

    @Override
    public List<TemplateProperty> getTemplateProperty(int id) {
        return templatePropertyMapper.getTemplateProperty(id);
    }

    @Override
    public int editFileProperty(PatentProperty property, Document document) {
        String name = patentPropertyMapper.getPropertyName(property.getPatentId(),property.getSortId());
        property.setName(name);
        PatentRecord record = new PatentRecord();
        record.setRecordType(0);
        record.setPatentId(property.getPatentId());
        record.setReocrd("属性 " + name + " 上传了 "+document.getName());
        patentRecordMapper.insert(record);
        return patentPropertyMapper.editFileProperty(property);
    }

    @Override
    public int deleteFileProperty(PatentProperty property, Document document) {
        String name = patentPropertyMapper.getPropertyName(property.getPatentId(),property.getSortId());
        PatentRecord record = new PatentRecord();
        record.setRecordType(0);
        record.setPatentId(property.getPatentId());
        record.setReocrd(name + " 删除了 "+document.getName());
        patentRecordMapper.insert(record);
        return patentPropertyMapper.deleteFileProperty(property);
    }

    @Override
    public int editProperty(PatentProperty property) {
        String name = patentPropertyMapper.getPropertyName(property.getPatentId(),property.getSortId());
        property.setName(name);
        PatentRecord record = new PatentRecord();
        record.setRecordType(0);
        record.setPatentId(property.getPatentId());
        record.setReocrd(name + " 变更为 "+property.getValue());
        patentRecordMapper.insert(record);
        return patentPropertyMapper.editProperty(property);
    }

    @Override
    public int applyTemplate(int patentID, int id) {
        return patentMapper.applyTemplate(patentID,id);
    }

    @Override
    public List<JSONObject> getPatents(int id, int templateId,String filter,int start,int length,String orderIndex,String order,String search) {
        return patentMapper.getPatents(id,templateId,filter,start,length,orderIndex,order,search);
    }

    @Override
    public int getPatentCount(int id, int templateId, String filter,String search) {
        return patentMapper.getPatentCount(id,templateId,filter,search);
    }

    @Override
    public List<TemplateProperty> getTemplatePropertyByType(Integer id, int type) {
        return templatePropertyMapper.getTemplatePropertyByType(id,type);
    }


    @Override
    public List<Map<String, String>> selectPropertyName(int templateId,String sort,String visible) {
        return templatePropertyMapper.selectPropertyName(templateId,sort,visible);
    }

    @Override
    public DisplayConfig getDisplayConfig(int configId) {
        return displayConfigMapper.selectConfigByID(configId);
    }

    @Override
    public List<DisplayConfig> getDisplayConfigs(int userId) {
        return displayConfigMapper.selectConfigByUserID(userId);
    }

    @Override
    public void saveDisplayConfig(DisplayConfig config) {
        displayConfigMapper.insert(config);
    }

    @Override
    public void updateDisplayConfig(DisplayConfig config) {
        displayConfigMapper.updateByPrimaryKeySelective(config);
    }

    @Override
    public int deleteDisplayConfig(int configId) {
        return displayConfigMapper.deleteByPrimaryKey(configId);
    }



    @Override
    public int newPatent(Patent patent) {
        return patentMapper.newPatent(patent);
    }

    @Override
    public Patent getPatent(int patentId) {
        return patentMapper.getPatentByID(patentId);
    }

    @Override
    public Map<Integer,String> getPatentProperty(int id) {
        return patentPropertyMapper.getProperty(id);
    }

    @Override
    public int getPatentNewID() {
        return patentMapper.getPatentNewID();
    }

    @Override
    public int importPatent(Patent patent) {
        return patentMapper.importPatent(patent);
    }



    @Override
    public List<TemplateProperty> getSearchCondition(int templateId) {
        return templatePropertyMapper.getSearchCondition(templateId);
    }


    @Override
    public String getPatentApplicationNumber(int id) {
        return cpqueryResultMapper.getPatentApplicationNumber(id);
    }

    @Override
    public CpqueryApplicationInfo getCpqueryApplication(String applicationNumber) {
        return cpqueryApplicationInfoMapper.selectByPrimaryKey(applicationNumber);
    }

    @Override
    public List<CpqueryReviewInfo> getCpqueryReview(String applicationNumber) {
        return cpqueryReviewInfoMapper.selectFileListByApplicationNumber(applicationNumber);
    }

    @Override
    public CpqueryCostInfo getCpqueryCost(String applicationNumber) {
        return cpqueryCostInfoMapper.selectByPrimaryKey(applicationNumber);
    }

    @Override
    public CpqueryPostInfo getCpqueryPost(String applicationNumber) {
        return cpqueryPostInfoMapper.selectByPrimaryKey(applicationNumber);
    }

    @Override
    public CpqueryAnnounceInfo getCpqueryAnnounce(String applicationNumber) {
        return cpqueryAnnounceInfoMapper.selectByPrimaryKey(applicationNumber);
    }
}
