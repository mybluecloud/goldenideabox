package com.triz.goldenideabox.dao;

import com.triz.goldenideabox.model.Role;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RoleMapper {
    List<Role> selectRoleByPage(@Param("pageNum")Integer pageNum, @Param("pageSize")Integer pageSize);

    List<Role> selectAllRole();

    Role selectRoleByID(Integer roleID);

    int deleteRoleByID(Integer roleID);

    int insertRole(Role role);

    int updateRole(Role role);

    List<Role> loadUserRoles(Integer id);

    List<Role> queryRole(String query);
}