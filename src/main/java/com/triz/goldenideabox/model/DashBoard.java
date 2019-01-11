package com.triz.goldenideabox.model;

import java.io.Serializable;

public class DashBoard implements Serializable {

    private Integer id;

    private String name;

    private Integer patentCount;

    private String displayConfig;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPatentCount() {
        return patentCount;
    }

    public void setPatentCount(Integer patentCount) {
        this.patentCount = patentCount;
    }

    public String getDisplayConfig() {
        return displayConfig;
    }

    public void setDisplayConfig(String displayConfig) {
        this.displayConfig = displayConfig;
    }
}
