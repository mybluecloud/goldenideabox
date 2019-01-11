package com.triz.goldenideabox.dao;

import com.triz.goldenideabox.model.AgentInfo;

public interface AgentInfoMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(AgentInfo record);

    int insertSelective(AgentInfo record);

    AgentInfo selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(AgentInfo record);

    int updateByPrimaryKey(AgentInfo record);
}