package com.triz.goldenideabox.model;

import java.io.Serializable;

public class Monitor implements Serializable {
    private Integer id;

    private Integer type;

    private Integer templateid;

    private Integer patentid;

    private Integer filed;

    private String condition;

    private Integer state;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getTemplateid() {
        return templateid;
    }

    public void setTemplateid(Integer templateid) {
        this.templateid = templateid;
    }

    public Integer getPatentid() {
        return patentid;
    }

    public void setPatentid(Integer patentid) {
        this.patentid = patentid;
    }

    public Integer getFiled() {
        return filed;
    }

    public void setFiled(Integer filed) {
        this.filed = filed;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition == null ? null : condition.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}