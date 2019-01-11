package com.triz.goldenideabox.dao;


import com.triz.goldenideabox.model.PatentRecord;
import java.util.List;

public interface PatentRecordMapper {
    int insert(PatentRecord record);

    List<PatentRecord> getCpqueryRecord();

    List<PatentRecord> getPatentRecords(Integer id);
}