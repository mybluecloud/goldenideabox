package com.triz.goldenideabox.dao;

import com.triz.goldenideabox.model.Template;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TemplateMapper {


    List<Template> selectAllTemplate();

    int deleteByTemplateID(Integer id);

    int changeTemplateState(@Param("id")int id, @Param("state")int state);

    int insertTemplate(Template template);

    Template getTemplate(Integer id);

    int updateTemplate(Template template);

    List<Template> selectReviewTemplateByUser(Integer userID);

    List<Template> selectTemplateByUser(@Param("userID")Integer userID,@Param("review")boolean review);
}