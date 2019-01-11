package com.triz.goldenideabox.dao;

import com.triz.goldenideabox.model.AgentEduction;

public interface AgentEductionMapper {
    int insert(AgentEduction record);

    int insertSelective(AgentEduction record);
}