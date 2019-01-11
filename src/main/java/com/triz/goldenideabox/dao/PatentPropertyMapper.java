package com.triz.goldenideabox.dao;

import com.triz.goldenideabox.model.PatentProperty;
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
    Map<Integer,String> getProperty(Integer id);

    String getPropertyName(@Param("id")Integer id,@Param("sortId")Integer sortId);
}