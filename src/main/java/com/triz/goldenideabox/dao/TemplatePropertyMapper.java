package com.triz.goldenideabox.dao;

import com.triz.goldenideabox.model.TemplateProperty;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface TemplatePropertyMapper {
    int insert(TemplateProperty record);

    int insertSelective(TemplateProperty record);

    int insertPropertys(@Param("lst")List<TemplateProperty> lst,@Param("templateID")Integer templateID);

    List<TemplateProperty> getTemplateProperty(Integer id);

    List<TemplateProperty> getTemplatePropertyByType(@Param("id")Integer id,@Param("type")Integer type);

    int delPropertyByTemplateID(Integer id);

    List<Map<String,String>> selectPropertyName(@Param("templateId")Integer templateId,@Param("sort")String sort,@Param("visible")String visible);

    List<TemplateProperty> getSearchCondition(Integer id);

    int copyTemplate(@Param("id")Integer id,@Param("templateId")Integer templateId);
}