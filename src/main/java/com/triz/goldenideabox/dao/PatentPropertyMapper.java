package com.triz.goldenideabox.dao;

import com.alibaba.fastjson.JSONObject;
import com.triz.goldenideabox.model.PatentProperty;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

public interface PatentPropertyMapper {
    int insert(PatentProperty record);

    int insertSelective(PatentProperty record);

    int editFileProperty(PatentProperty property);

    int deleteFileProperty(PatentProperty property);

    int editProperty(PatentProperty property);


    @MapKey("sort_id")
    Map<Integer,JSONObject> getProperty(Integer id);


    Map<Integer,List<JSONObject>> getPropertyByValue(@Param("templateId")Integer templateId,@Param("sortId")Integer sortId,@Param("value")String value,@Param("ids")List<Integer> ids);

    String getPropertyName(@Param("id")Integer id,@Param("sortId")Integer sortId);

    String getPropertyValue(@Param("id")Integer id,@Param("sortId") Integer sortId);
}