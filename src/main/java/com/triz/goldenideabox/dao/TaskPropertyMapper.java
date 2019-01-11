package com.triz.goldenideabox.dao;

import com.triz.goldenideabox.model.TaskProperty;

public interface TaskPropertyMapper {
    int insert(TaskProperty record);

    int insertSelective(TaskProperty record);
}