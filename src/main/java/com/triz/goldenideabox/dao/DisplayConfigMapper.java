package com.triz.goldenideabox.dao;

import com.triz.goldenideabox.model.DisplayConfig;
import java.util.List;

public interface DisplayConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DisplayConfig record);

    int insertSelective(DisplayConfig record);

    DisplayConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DisplayConfig record);

    int updateByPrimaryKey(DisplayConfig record);

    DisplayConfig selectConfigByID(Integer id);

    List<DisplayConfig> selectConfigByUserID(Integer userId);
}