package com.triz.goldenideabox.dao;

import com.triz.goldenideabox.model.Role;
import com.triz.goldenideabox.model.User;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {


    User selectByAccount(String account);
    List<User> queryUser(String query);

    List<User> selectAllUser();

    List<User> selectUserByPage(@Param("pageNum")Integer pageNum, @Param("pageSize")Integer pageSize);

    int addUserToRole(@Param("userID")Integer userID, @Param("roleID")Integer roleID);

    int deleteAllUserFromRole(Integer roleID);

    int changeUserState(@Param("userID")Integer userID, @Param("state")Integer state);

    int deleteByUserID(Integer userID);

    List<User> selectUserByRoleID(Integer roleID);

    int findUserFromRole(@Param("userID")Integer userID, @Param("roleID")Integer roleID);

    int deleteRoleUserByID(Integer roleID);

    int updateRoleUser(@Param("users")List<Integer> users, @Param("roleID")Integer roleID);

    List<User> getPermitUser(Integer templateID);

    List<User> getNoPermitUser(Integer templateID);

    int deletePermitUserByTemplateID(Integer templateID);

    int updatePermitUser(@Param("users")List<Integer> users, @Param("templateID")Integer templateID);

    List<User> selectUserBystate(Integer state);

    int regedit(User record);

    int updateByPrimaryKeySelective(User record);

    int updateLoginTime(Integer id);
}