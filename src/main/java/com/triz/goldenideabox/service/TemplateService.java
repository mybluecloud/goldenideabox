package com.triz.goldenideabox.service;

import com.triz.goldenideabox.model.Template;
import com.triz.goldenideabox.model.TemplateProperty;
import com.triz.goldenideabox.model.User;
import java.util.List;

public interface TemplateService {

    List<Template> selectAllTemplate();

    int deleteByTemplateID(int id);

    int reviewTemplate(int id, int state);

    int changeTemplateState(int id, int state);

    int saveTemplate(int id, String name, String description, User user, String data);

    Template getTemplate(int id);

    List<TemplateProperty> getTemplateProperty(int id);

    List<Template> selectTemplateByUser(int id, boolean review);

    List<User> getNoPermitUser(int id);

    List<User> getPermitUser(int id);

    int permit(int id, List<Integer> users);

    int copyTemplate(Integer id,String name, String description, User user);
}
