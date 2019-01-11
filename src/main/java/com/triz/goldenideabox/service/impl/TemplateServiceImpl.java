package com.triz.goldenideabox.service.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.triz.goldenideabox.model.Template;
import com.triz.goldenideabox.model.TemplateProperty;
import com.triz.goldenideabox.model.User;
import com.triz.goldenideabox.dao.TemplateMapper;
import com.triz.goldenideabox.dao.TemplatePropertyMapper;
import com.triz.goldenideabox.dao.UserMapper;
import com.triz.goldenideabox.service.TemplateService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("templateService")
public class TemplateServiceImpl implements TemplateService {


    @Autowired
    private TemplateMapper templateMapper;

    @Autowired
    private TemplatePropertyMapper templatePropertyMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Template> selectAllTemplate() {
        return templateMapper.selectAllTemplate();
    }

    @Override
    public int deleteByTemplateID(int id) {
        return templateMapper.deleteByTemplateID(id);
    }

    @Override
    public int reviewTemplate(int id, int state) {
        return templateMapper.changeTemplateState(id,state);
    }

    @Override
    public int changeTemplateState(int id, int state) {
        return templateMapper.changeTemplateState(id, state);
    }

    @Override
    public int saveTemplate(int id,String name,String description,User user,String data) {


        Template template = new Template();

        template.setName(name);
        template.setDescription(description);
        template.setCreator(user);
        template.setState(0);

        template.setTime(new Date());

        if (id == 0){
            templateMapper.insertTemplate(template);
        } else {
            template.setId(id);
            templateMapper.updateTemplate(template);
        }



        ObjectMapper mapper = new ObjectMapper();
        try {
            JavaType type = mapper.getTypeFactory().constructParametricType(ArrayList.class, TemplateProperty.class);
            List<TemplateProperty> lst = mapper.readValue(data, type);
            if (id != 0){
                templatePropertyMapper.delPropertyByTemplateID(template.getId());
            }


            for (TemplateProperty record:lst){
                record.setTemplateId(template.getId());
                templatePropertyMapper.insertSelective(record);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }




        return 0;
    }

    @Override
    public Template getTemplate(int id) {
        return templateMapper.getTemplate(id);
    }

    @Override
    public List<TemplateProperty> getTemplateProperty(int id) {
        return templatePropertyMapper.getTemplateProperty(id);
    }

    @Override
    public List<Template> selectTemplateByUser(int id, boolean review) {
        return templateMapper.selectTemplateByUser(id,review);
    }

    @Override
    public List<User> getNoPermitUser(int id) {
        return userMapper.getNoPermitUser(id);
    }

    @Override
    public List<User> getPermitUser(int id) {
        return userMapper.getPermitUser(id);
    }

    @Override
    public int permit(int templateID, List<Integer> users) {
        userMapper.deletePermitUserByTemplateID(templateID);
        if (users != null && users.size() > 0){
            userMapper.updatePermitUser(users,templateID);
        }
        return 0;
    }

    @Override
    public int copyTemplate(Integer id,String name, String description, User user) {

        Template template = new Template();

        template.setName(name);
        template.setDescription(description);
        template.setCreator(user);
        template.setState(0);

        template.setTime(new Date());

        templateMapper.insertTemplate(template);

        templatePropertyMapper.copyTemplate(template.getId(),id);


        return 0;
    }
}
