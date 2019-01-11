package com.triz.goldenideabox.service;


import com.alibaba.fastjson.JSONObject;
import com.triz.goldenideabox.model.Chart;
import com.triz.goldenideabox.model.IndexSetting;
import com.triz.goldenideabox.model.Message;
import com.triz.goldenideabox.model.Resource;
import com.triz.goldenideabox.model.Role;
import com.triz.goldenideabox.model.User;
import java.util.Date;
import java.util.List;


public interface  UserService {


    List<User> selectAllUser();

    int deleteByUserID(int id);

    int changeUserState(int id, int state);

    List<Role> selectAllRole();

    Role selectRoleByID(int id);

    int deleteRoleByID(int id);

    List<User> selectUserByRoleID(int roleID);

    int insertRole(Role role, List<Integer> users);

    int updateRole(Role role, List<Integer> users);

    List<Resource> selectAllResource();

    int deleteResourceByID(int id);

    Resource selectResourceByID(int id);

    List<Role> selectRoleByResourceID(int id);

    int insertResource(Resource resource, List<Integer> integers);

    int updateResource(Resource resource, List<Integer> integers);

    List<Resource> loadAllResources();

    User selectByAccount(String account);

    List<Resource> loadUserResources(int id);

    List<Role> loadUserRole(int id);

    List<User> selectUserBystate(int state);

    int regeditAccout(User user);

    List<User> queryUser(String query);

    List<Role> queryRole(String query);

    int sendMessage(Message msg);

    List<JSONObject> loadChatUserList(int id);

    List<Message> loadUserMessage(int id, int chatId);

    List<JSONObject> loadGroupMessage(int chatId);

    void updateLoginTime(int id);

    List<JSONObject> loadChatGroupList(int id, Date time);

    void updateUserInfo(User user);


    void addIndexSetting(IndexSetting indexSetting);

    List<IndexSetting> getIndexSetting(Integer id);

    int deleteIndexSetting(Integer id);

    int sortIndexSetting(List<Integer> lst);
}
