package com.triz.goldenideabox.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.triz.goldenideabox.dao.ChartMapper;
import com.triz.goldenideabox.model.Chart;
import com.triz.goldenideabox.service.ChartService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("chartService")
public class ChartServiceImpl implements ChartService {

    @Autowired
    private ChartMapper chartMapper;

    @Override
    public int insertChart(Chart chart) {
        return chartMapper.insertChart(chart);
    }

    @Override
    public List<JSONObject> getSectorChart(Integer id, int templateId, String filter, int property1) {
        return chartMapper.getSectorChart(id,templateId,filter,property1);
    }

    @Override
    public List<Chart> getChartsByType(Integer userId, int type) {
        return chartMapper.getChartsByType(userId,type);
    }

    @Override
    public List<Chart> getCharts(Integer id) {
        return chartMapper.getCharts(id);
    }

    @Override
    public int deleteChart(Integer id) {
        return chartMapper.deleteChart(id);
    }

    @Override
    public List<JSONObject> getHistogram(Integer id, int templateId, String filter, int property1) {
        return chartMapper.getSectorChart(id,templateId,filter,property1);
    }

    @Override
    public List<JSONObject> getLineChart(Integer id, int templateId, String filter, int property1, int property2, int scope) {
        return chartMapper.getLineChart(id,templateId,filter,property1,property2,scope);
    }

    @Override
    public Chart getChart(Integer id) {
        return chartMapper.getChart(id);
    }


}
