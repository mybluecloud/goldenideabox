package com.triz.goldenideabox.dao;

import com.triz.goldenideabox.model.Order;
import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    List<Order> selectAppointOrder();

    List<Integer> selectOrderIDNoPatent();

    List<Order> selectClaimOrder(Integer userID);
}