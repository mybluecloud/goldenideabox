package com.triz.goldenideabox.dao;

import com.triz.goldenideabox.model.Cpquery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CpqueryMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_cpquery
     *
     * @mbg.generated
     */
    int deleteCpqueryInfo(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_cpquery
     *
     * @mbg.generated
     */
    int insert(Cpquery record);



    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_cpquery
     *
     * @mbg.generated
     */
    Cpquery selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_cpquery
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(Cpquery record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_cpquery
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Cpquery record);

    List<Cpquery> getCpqueryInfo();

    int updateStatus(@Param("id")Integer id, @Param("status")int status);
}