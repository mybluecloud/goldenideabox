package com.triz.goldenideabox.model;

import java.io.Serializable;

public class DisplayConfig implements Serializable {
    private Integer id;

    private Integer userId;

    private Integer templateId;

    private String configname;

    private String filter;

    private Integer leftFixedSum;

    private Integer rightFixedSum;

    private String orderList;

    private String visibleList;

    private Integer pageLength;

    private String searchList;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getConfigname() {
        return configname;
    }

    public void setConfigname(String configname) {
        this.configname = configname == null ? null : configname.trim();
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter == null ? null : filter.trim();
    }

    public Integer getLeftFixedSum() {
        return leftFixedSum;
    }

    public void setLeftFixedSum(Integer leftFixedSum) {
        this.leftFixedSum = leftFixedSum;
    }

    public Integer getRightFixedSum() {
        return rightFixedSum;
    }

    public void setRightFixedSum(Integer rightFixedSum) {
        this.rightFixedSum = rightFixedSum;
    }

    public String getOrderList() {
        return orderList;
    }

    public void setOrderList(String orderList) {
        this.orderList = orderList == null ? null : orderList.trim();
    }

    public String getVisibleList() {
        return visibleList;
    }

    public void setVisibleList(String visibleList) {
        this.visibleList = visibleList == null ? null : visibleList.trim();
    }

    public Integer getPageLength() {
        return pageLength;
    }

    public void setPageLength(Integer pageLength) {
        this.pageLength = pageLength;
    }

    public String getSearchList() {
        return searchList;
    }

    public void setSearchList(String searchList) {
        this.searchList = searchList;
    }
}