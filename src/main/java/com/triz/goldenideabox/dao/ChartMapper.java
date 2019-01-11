package com.triz.goldenideabox.dao;


import com.alibaba.fastjson.JSONObject;
import com.triz.goldenideabox.model.Chart;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ChartMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_chart
     *
     * @mbg.generated
     */
    int deleteChart(Integer id);



    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_chart
     *
     * @mbg.generated
     */
    int insertChart(Chart record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_chart
     *
     * @mbg.generated
     */
    Chart getChart(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_chart
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(Chart record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_chart
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Chart record);

    List<JSONObject> getSectorChart(@Param("userId")Integer userId, @Param("templateId")Integer templateId,
        @Param("filter")String filter,@Param("property")Integer property);

    List<JSONObject> getLineChart(@Param("userId")Integer userId, @Param("templateId")Integer templateId,
        @Param("filter")String filter,@Param("property1")Integer property1,@Param("property2")Integer property2,@Param("scope")int scope);

    List<Chart> getChartsByType(@Param("userId")Integer userId, @Param("type")int type);

    List<Chart> getCharts(@Param("userId")Integer userId);
}