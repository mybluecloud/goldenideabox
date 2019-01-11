package com.triz.goldenideabox.dao;

import com.triz.goldenideabox.model.Monitor;

public interface MonitorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Monitor record);

    int insertSelective(Monitor record);

    Monitor selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Monitor record);

    int updateByPrimaryKey(Monitor record);
}