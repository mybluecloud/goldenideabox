package com.triz.goldenideabox.service;

import com.triz.goldenideabox.model.Cpquery;
import com.triz.goldenideabox.model.CpqueryResult;
import com.triz.goldenideabox.model.PatentRecord;
import java.util.Date;
import java.util.List;

public interface CpqueryService {

    List<Cpquery> getCpqueryInfo();

    int addCpqueryInfo(Cpquery cpquery);

    String getJsessionID();

    String getJcaptchaImage(Date date);

    String getJcaptchaText(Date date);

    int getValidationStatus(String code);

    String beginQuery(int id, String checks,boolean isWhole);


    Cpquery getCpqueryInfoById(int id);

    int updateCpqueryInfo(Cpquery cpquery);

    List<PatentRecord> getCpqueryRecord();

    int deleteCpqueryInfo(int id);

    void stopCpquery();

    List<CpqueryResult> getCpqueryResult();
}
