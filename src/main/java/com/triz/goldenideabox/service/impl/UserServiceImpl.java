package com.triz.goldenideabox.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.triz.goldenideabox.dao.ChartMapper;
import com.triz.goldenideabox.dao.IndexSettingMapper;
import com.triz.goldenideabox.dao.MessageMapper;
import com.triz.goldenideabox.dao.ResourceMapper;
import com.triz.goldenideabox.dao.RoleMapper;
import com.triz.goldenideabox.dao.UserMapper;
import com.triz.goldenideabox.model.Chart;
import com.triz.goldenideabox.model.IndexSetting;
import com.triz.goldenideabox.model.Message;
import com.triz.goldenideabox.model.Resource;
import com.triz.goldenideabox.model.Role;
import com.triz.goldenideabox.model.User;
import com.triz.goldenideabox.service.UserService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private IndexSettingMapper indexSettingMapper;

    @Override
    public List<User> selectAllUser() {
        return userMapper.selectAllUser();
    }

    @Override
    public int deleteByUserID(int id) {
        return userMapper.deleteByUserID(id);
    }

    @Override
    public int changeUserState(int id, int state) {
        return userMapper.changeUserState(id, state);
    }

    @Override
    public List<Role> selectAllRole() {
        return roleMapper.selectAllRole();
    }

    @Override
    public Role selectRoleByID(int id) {
        return roleMapper.selectRoleByID(id);
    }

    @Override
    public int deleteRoleByID(int id) {
        roleMapper.deleteRoleByID(id);
        userMapper.deleteAllUserFromRole(id);
        return 0;
    }

    @Override
    public List<User> selectUserByRoleID(int roleID) {
        return userMapper.selectUserByRoleID(roleID);
    }

    @Override
    public int insertRole(Role role, List<Integer> users) {
        int r = roleMapper.insertRole(role);
        if (users != null && users.size() > 0) {
            for (Integer userID : users) {
                r = userMapper.addUserToRole(userID, role.getId());
            }
        }

        return r;
    }

    @Override
    public int updateRole(Role role, List<Integer> users) {

        roleMapper.updateRole(role);
        userMapper.deleteRoleUserByID(role.getId());
        if (users != null && users.size() > 0) {
            userMapper.updateRoleUser(users, role.getId());
        }

        return 0;
    }

    @Override
    public List<Resource> selectAllResource() {
        return resourceMapper.selectAllResource();
    }

    @Override
    public int deleteResourceByID(int id) {
        return resourceMapper.deleteResourceByID(id);
    }

    @Override
    public Resource selectResourceByID(int id) {
        return resourceMapper.selectResourceByID(id);
    }

    @Override
    public List<Role> selectRoleByResourceID(int id) {
        return resourceMapper.selectRoleByResourceID(id);
    }

    @Override
    public int insertResource(Resource resource, List<Integer> roles) {
        resourceMapper.insertResource(resource);
        if (roles != null && roles.size() > 0) {
            for (Integer roleID : roles) {
                resourceMapper.addRolePermissionToResource(roleID, resource.getId());
            }
        }

        return 0;
    }

    @Override
    public int updateResource(Resource resource, List<Integer> roles) {
        resourceMapper.updateResource(resource);
        resourceMapper.deleteRolePermissionByResourceID(resource.getId());
        resourceMapper.updateResourceRole(roles, resource.getId());
        return 0;
    }

    @Override
    public List<Resource> loadAllResources() {
        return resourceMapper.selectAllResource();
    }

    @Override
    public User selectByAccount(String account) {
        return userMapper.selectByAccount(account);
    }

    @Override
    public List<Resource> loadUserResources(int id) {
        return resourceMapper.loadUserResources(id);
    }

    @Override
    public List<Role> loadUserRole(int id) {
        return roleMapper.loadUserRoles(id);
    }

    @Override
    public List<User> selectUserBystate(int state) {
        return userMapper.selectUserBystate(state);
    }

    @Override
    public int regeditAccout(User user) {
        return userMapper.regedit(user);
    }

    @Override
    public List<User> queryUser(String query) {
        return userMapper.queryUser(query);
    }

    @Override
    public List<Role> queryRole(String query) {
        return roleMapper.queryRole(query);
    }

    @Override
    public int sendMessage(Message msg) {
        return messageMapper.insertSelective(msg);
    }

    @Override
    public List<JSONObject> loadChatUserList(int id) {
        return messageMapper.selectChatUser(id);
    }

    @Override
    public List<Message> loadUserMessage(int id, int chatId) {
        messageMapper.changeUnread(id,chatId);
        return messageMapper.selectChatUserRecord(id, chatId);


    }

    @Override
    public List<JSONObject> loadGroupMessage(int chatId) {

        return messageMapper.selectChatGroupRecord(chatId);


    }

    @Override
    public void updateLoginTime(int id) {

        userMapper.updateLoginTime(id);
    }

    @Override
    public List<JSONObject> loadChatGroupList(int id, Date time) {
        return messageMapper.selectChatGroup(id, time);
    }

    @Override
    public void updateUserInfo(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public void addIndexSetting(IndexSetting indexSetting) {
        indexSettingMapper.addIndexSetting(indexSetting);
    }

    @Override
    public List<IndexSetting> getIndexSetting(Integer id) {
        return indexSettingMapper.getIndexSetting(id);
    }

    @Override
    public int deleteIndexSetting(Integer id) {
        return indexSettingMapper.deleteIndexSetting(id);
    }

    @Override
    public int sortIndexSetting(List<Integer> lst) {
        return indexSettingMapper.sortIndexSetting(lst);
    }


}
