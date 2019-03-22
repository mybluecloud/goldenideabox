package com.triz.goldenideabox.dao;

import com.triz.goldenideabox.model.CpqueryResult;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CpqueryResultMapper {

    int updateApplicationStatus(@Param("applicationNumber")String applicationNumber,@Param("applicationStatus")Integer applicationStatus);

    int updateReviewStatus(@Param("applicationNumber")String applicationNumber,@Param("reviewStatus")Integer reviewStatus);

    int updateCostStatus(@Param("applicationNumber")String applicationNumber,@Param("costStatus")Integer costStatus);

    int updatePostStatus(@Param("applicationNumber")String applicationNumber,@Param("postStatus")Integer postStatus);

    int updateAnnounceStatus(@Param("applicationNumber")String applicationNumber,@Param("announceStatus")Integer announceStatus);

    int updateTime(@Param("applicationNumber")String applicationNumber);

    List<CpqueryResult> getCpqueryResultByID(Integer id);

    List<CpqueryResult> getFailResultByID(Integer id);


    int updateApplicationNumbers(@Param("id") Integer id, @Param("templateId") Integer templateId,
            @Param("matchField") Integer matchField, @Param("matchChar") String matchChar,
            @Param("applicationNumberField") Integer applicationNumberField);

    List<CpqueryResult> getCpqueryResult();

    String getPatentApplicationNumber(Integer id);
}