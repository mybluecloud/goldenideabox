package com.triz.goldenideabox.dao;

import com.alibaba.fastjson.JSONObject;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface ImportTemplateMapper {


    List<JSONObject> getImportTemplate(Integer id);

    int insertImportTemplate(@Param("templateID")Integer templateID, @Param("configs")Map<String,Integer> configs);

    int deleteImportTemplate(Integer id);
}