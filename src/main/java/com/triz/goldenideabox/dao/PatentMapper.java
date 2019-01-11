package com.triz.goldenideabox.dao;

import com.alibaba.fastjson.JSONObject;
import com.triz.goldenideabox.model.Patent;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PatentMapper {
    int appointPatent(@Param("userID")Integer userID, @Param("orderIDs")List<Integer> orderIDs);

    List<Patent> getNoClaimPatent();

    int applyTemplate(@Param("patentID")Integer patentID, @Param("templateID")Integer templateID);

    List<JSONObject> getPatents(@Param("userId")Integer userId, @Param("templateId")Integer templateId,
        @Param("filter")String filter,@Param("start")Integer start,@Param("length")Integer length,
        @Param("orderIndex")String orderIndex,@Param("order")String order,@Param("search")String search);

    int getPatentCount(@Param("userId")Integer userId, @Param("templateId")Integer templateId,
        @Param("filter")String filter,@Param("search")String search);

    int newPatent(Patent patent);

    Patent getPatentByID(Integer patentId);

    int getPatentNewID();

    int importPatent(Patent patent);

    Patent getPatentByOrderID(Integer id);
}