package com.triz.goldenideabox.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Template implements Serializable {
    private Integer id;

    private String name;

    private String description;

    private Integer state;

    private User creator;

    private Date time;

    private List<TemplateProperty> properties;

    private static final long serialVersionUID = 1L;

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
        this.name = name == null ? null : name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public List<TemplateProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<TemplateProperty> properties) {
        this.properties = properties;
    }
}