package com.triz.goldenideabox.controller;

import com.triz.goldenideabox.common.helper.ParameterHelper;
import com.triz.goldenideabox.common.shiro.ShiroService;
import com.triz.goldenideabox.model.Resource;
import com.triz.goldenideabox.model.Role;
import com.triz.goldenideabox.model.User;
import com.triz.goldenideabox.service.UserService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ShiroService shiroService;

    @ResponseBody
    @RequestMapping(value = "/users", produces = "application/json;charset=UTF-8")
    public Object checkUserList(@RequestParam(required = true) int state) {

        List<User> lst = userService.selectUserBystate(state);
        Map<Object, Object> info = new HashMap<Object, Object>();
        info.put("data", lst);
        return info;
    }


    @ResponseBody
    @RequestMapping(value = "/changestate", produces = "application/json;charset=UTF-8")
    public int changeState(
        @RequestParam(required = true) int id, @RequestParam(required = true) int state) {
        int resultCode = userService.changeUserState(id, state);
        shiroService.updatePermission();
        return resultCode;
    }

    @ResponseBody
    @RequestMapping(value = "/roles", produces = "application/json;charset=UTF-8")
    public Object checkRoleList() {

        List<Role> lst = userService.selectAllRole();
        Map<Object, Object> info = new HashMap<Object, Object>();
        info.put("data", lst);
        return info;
    }

    @ResponseBody
    @RequestMapping(value = "/queryrole", produces = "application/json;charset=UTF-8")
    public Object checkRoleByID(
        @RequestParam(required = true) int id) {

        Role role = userService.selectRoleByID(id);
        List<User> lst = userService.selectUserByRoleID(id);
        Map<Object, Object> info = new HashMap<Object, Object>();
        info.put("role", role);
        info.put("user", lst);
        return info;
    }

    @ResponseBody
    @RequestMapping(value = "/delrole", produces = "application/json;charset=UTF-8")
    public int delRole(@RequestParam(required = true) int id) {

        int resultCode = userService.deleteRoleByID(id);
        shiroService.updatePermission();
        return resultCode;
    }

    @ResponseBody
    @RequestMapping(value = "/addrole", produces = "application/json;charset=UTF-8")
    public int addRole(@RequestParam(required = true) String name,
        @RequestParam(required = true) String remark,
        @RequestParam(required = false) String users) {

        Role role = new Role();
        role.setName(name);
        role.setRemark(remark);
        int resultCode =  userService.insertRole(role, ParameterHelper.StringToIntegerList(users, ","));

        shiroService.updatePermission();

        return resultCode;
    }

    @ResponseBody
    @RequestMapping(value = "/updaterole", produces = "application/json;charset=UTF-8")
    public int updateRole(
        @RequestParam(required = true) int id,
        @RequestParam(required = true) String name,
        @RequestParam(required = true) String remark,
        @RequestParam(required = true) String users) {

        Role role = new Role();
        role.setId(id);
        role.setName(name);
        role.setRemark(remark);

        int resultCode =  userService.updateRole(role, ParameterHelper.StringToIntegerList(users, ","));
        shiroService.updatePermission();
        return resultCode;
    }

    @ResponseBody
    @RequestMapping(value = "/resources", produces = "application/json;charset=UTF-8")
    public Object checkResourceList() {

        List<Resource> lst = userService.selectAllResource();
        Map<Object, Object> info = new HashMap<Object, Object>();
        info.put("data", lst);
        return info;
    }

    @ResponseBody
    @RequestMapping(value = "/delresource", produces = "application/json;charset=UTF-8")
    public int delResource(
        @RequestParam(required = true) int id) {

        int resultCode =  userService.deleteResourceByID(id);
        shiroService.updatePermission();
        return resultCode;
    }

    @ResponseBody
    @RequestMapping(value = "/queryresource", produces = "application/json;charset=UTF-8")
    public Object checkResourceByID(
        @RequestParam(required = true) int id) {

        Resource resource = userService.selectResourceByID(id);
        List<Role> lst = userService.selectRoleByResourceID(id);
        Map<Object, Object> info = new HashMap<Object, Object>();
        info.put("resource", resource);
        info.put("role", lst);
        return info;
    }


    @ResponseBody
    @RequestMapping(value = "/addresource", produces = "application/json;charset=UTF-8")
    public int addResource(
        @RequestParam(required = false) int id,
        @RequestParam(required = true) String name,
        @RequestParam(required = true) String url,
        @RequestParam(required = true) int type,
        @RequestParam(required = true) int parent_id,
        @RequestParam(required = false) String roles) {

        Resource resource = new Resource();
        resource.setName(name);
        resource.setResUrl(url);
        resource.setType(type);
        resource.setParentId(parent_id);
        int resultCode =  userService.insertResource(resource, ParameterHelper.StringToIntegerList(roles, ","));
        shiroService.updatePermission();
        return resultCode;
    }

    @ResponseBody
    @RequestMapping(value = "/updateresource", produces = "application/json;charset=UTF-8")
    public int updateResource(
        @RequestParam(required = true) int id,
        @RequestParam(required = true) String name,
        @RequestParam(required = true) String url,
        @RequestParam(required = true) int type,
        @RequestParam(required = true) int parent_id,
        @RequestParam(required = true) String roles) {

        Resource resource = new Resource();
        resource.setId(id);
        resource.setName(name);
        resource.setResUrl(url);
        resource.setType(type);
        resource.setParentId(parent_id);

        int resultCode =  userService.updateResource(resource, ParameterHelper.StringToIntegerList(roles, ","));
        shiroService.updatePermission();

        return resultCode;
    }


    @RequestMapping(value = "/role")
    public String checkRoleList(Model model) {

        // model.addAttribute("list", lst);
        return "backstage/role";
    }

    @RequestMapping(value = "/user")
    public String checkModelPermission(Model model) {

        // model.addAttribute("list", lst);
        return "backstage/user";
    }

    @RequestMapping(value = "/resource")
    public String checkResource(Model model) {

        // model.addAttribute("list", lst);
        return "backstage/resource";
    }


}
