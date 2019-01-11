package com.triz.goldenideabox.model;

import java.io.Serializable;

public class Cpquery implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_cpquery.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_cpquery.template_id
     *
     * @mbg.generated
     */
    private Integer templateId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_cpquery.match_field
     *
     * @mbg.generated
     */
    private Integer matchField;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_cpquery.match_char
     *
     * @mbg.generated
     */
    private String matchChar;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_cpquery.application_number_field
     *
     * @mbg.generated
     */
    private Integer applicationNumberField;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_cpquery.status_field
     *
     * @mbg.generated
     */
    private Integer statusField;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_cpquery.account
     *
     * @mbg.generated
     */
    private String account;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_cpquery.password
     *
     * @mbg.generated
     */
    private String password;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_cpquery.status
     *
     * @mbg.generated
     */
    private Integer status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_cpquery
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_cpquery.id
     *
     * @return the value of t_cpquery.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_cpquery.id
     *
     * @param id the value for t_cpquery.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_cpquery.template_id
     *
     * @return the value of t_cpquery.template_id
     *
     * @mbg.generated
     */
    public Integer getTemplateId() {
        return templateId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_cpquery.template_id
     *
     * @param templateId the value for t_cpquery.template_id
     *
     * @mbg.generated
     */
    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_cpquery.match_field
     *
     * @return the value of t_cpquery.match_field
     *
     * @mbg.generated
     */
    public Integer getMatchField() {
        return matchField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_cpquery.match_field
     *
     * @param matchField the value for t_cpquery.match_field
     *
     * @mbg.generated
     */
    public void setMatchField(Integer matchField) {
        this.matchField = matchField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_cpquery.match_char
     *
     * @return the value of t_cpquery.match_char
     *
     * @mbg.generated
     */
    public String getMatchChar() {
        return matchChar;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_cpquery.match_char
     *
     * @param matchChar the value for t_cpquery.match_char
     *
     * @mbg.generated
     */
    public void setMatchChar(String matchChar) {
        this.matchChar = matchChar == null ? null : matchChar.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_cpquery.application_number_field
     *
     * @return the value of t_cpquery.application_number_field
     *
     * @mbg.generated
     */
    public Integer getApplicationNumberField() {
        return applicationNumberField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_cpquery.application_number_field
     *
     * @param applicationNumberField the value for t_cpquery.application_number_field
     *
     * @mbg.generated
     */
    public void setApplicationNumberField(Integer applicationNumberField) {
        this.applicationNumberField = applicationNumberField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_cpquery.status_field
     *
     * @return the value of t_cpquery.status_field
     *
     * @mbg.generated
     */
    public Integer getStatusField() {
        return statusField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_cpquery.status_field
     *
     * @param statusField the value for t_cpquery.status_field
     *
     * @mbg.generated
     */
    public void setStatusField(Integer statusField) {
        this.statusField = statusField;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_cpquery.account
     *
     * @return the value of t_cpquery.account
     *
     * @mbg.generated
     */
    public String getAccount() {
        return account;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_cpquery.account
     *
     * @param account the value for t_cpquery.account
     *
     * @mbg.generated
     */
    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_cpquery.password
     *
     * @return the value of t_cpquery.password
     *
     * @mbg.generated
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_cpquery.password
     *
     * @param password the value for t_cpquery.password
     *
     * @mbg.generated
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_cpquery.status
     *
     * @return the value of t_cpquery.status
     *
     * @mbg.generated
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_cpquery.status
     *
     * @param status the value for t_cpquery.status
     *
     * @mbg.generated
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Cpquery{" +
            "id=" + id +
            ", templateId=" + templateId +
            ", matchField=" + matchField +
            ", matchChar='" + matchChar + '\'' +
            ", applicationNumberField=" + applicationNumberField +
            ", statusField=" + statusField +
            ", account='" + account + '\'' +
            ", password='" + password + '\'' +
            ", status=" + status +
            '}';
    }
}