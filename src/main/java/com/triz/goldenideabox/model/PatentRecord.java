package com.triz.goldenideabox.model;

import java.io.Serializable;
import java.util.Date;

public class PatentRecord implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_patent_record.record_type
     *
     * @mbg.generated
     */
    private Integer recordType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_patent_record.patent_id
     *
     * @mbg.generated
     */
    private Integer patentId;


    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_patent_record.reocrd
     *
     * @mbg.generated
     */
    private String reocrd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_patent_record.update_time
     *
     * @mbg.generated
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_patent_record
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_patent_record.record_type
     *
     * @return the value of t_patent_record.record_type
     *
     * @mbg.generated
     */
    public Integer getRecordType() {
        return recordType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_patent_record.record_type
     *
     * @param recordType the value for t_patent_record.record_type
     *
     * @mbg.generated
     */
    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_patent_record.patent_id
     *
     * @return the value of t_patent_record.patent_id
     *
     * @mbg.generated
     */
    public Integer getPatentId() {
        return patentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_patent_record.patent_id
     *
     * @param patentId the value for t_patent_record.patent_id
     *
     * @mbg.generated
     */
    public void setPatentId(Integer patentId) {
        this.patentId = patentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_patent_record.reocrd
     *
     * @return the value of t_patent_record.reocrd
     *
     * @mbg.generated
     */
    public String getReocrd() {
        return reocrd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_patent_record.reocrd
     *
     * @param reocrd the value for t_patent_record.reocrd
     *
     * @mbg.generated
     */
    public void setReocrd(String reocrd) {
        this.reocrd = reocrd == null ? null : reocrd.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_patent_record.update_time
     *
     * @return the value of t_patent_record.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_patent_record.update_time
     *
     * @param updateTime the value for t_patent_record.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}