package com.triz.goldenideabox.model;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {
    private Integer id;

    private String title;

    private Integer from;

    private Integer to;

    private Integer template;

    private Date time;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public Integer getTemplate() {
        return template;
    }

    public void setTemplate(Integer template) {
        this.template = template;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}