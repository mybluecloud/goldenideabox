package com.triz.goldenideabox.service;

import com.alibaba.fastjson.JSONObject;
import com.triz.goldenideabox.model.Chart;
import java.util.List;

public interface ChartService {

    int insertChart(Chart chart);

    List<JSONObject> getSectorChart(Integer id, int templateId, String filter, int property1);

    List<Chart> getChartsByType(Integer userId, int type);

    int deleteChart(Integer id);

    List<JSONObject> getHistogram(Integer id, int templateId, String filter, int property1);

    List<JSONObject> getLineChart(Integer id, int templateId, String filter, int property1, int property2, int scope);

    Chart getChart(Integer id);

    List<Chart> getCharts(Integer id);
}
