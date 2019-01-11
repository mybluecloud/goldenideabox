package com.triz.goldenideabox.dao;

import com.triz.goldenideabox.model.Resource;
import com.triz.goldenideabox.model.Role;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ResourceMapper {
    List<Resource> selectAllResource();

    List<Resource> loadUserResources(Integer id);

    List<Role> selectRoleByResourceID(Integer id);

    int addRolePermissionToResource(@Param("roleID")Integer roleID, @Param("resourceID")Integer resourceID);

    int deleteRolePermissionByResourceID(Integer resourceID);

    int addRole(String name);

    List<Role> loadAllRoles();

    int deleteResourceByID(Integer id);

    Resource selectResourceByID(int id);

    void insertResource(Resource resource);

    void updateResource(Resource resource);

    void updateResourceRole(@Param("roles")List<Integer> roles, @Param("resourceID")Integer resourceID);
}