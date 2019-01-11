package com.triz.goldenideabox.model;

import java.io.Serializable;

public class TaskProperty implements Serializable {
    private Integer taskId;

    private String name;

    private String value;

    private Integer index;

    private static final long serialVersionUID = 1L;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}