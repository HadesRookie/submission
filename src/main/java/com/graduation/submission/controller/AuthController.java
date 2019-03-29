package com.graduation.submission.controller;

import com.graduation.submission.pojo.Permission;
import com.graduation.submission.pojo.Role;
import com.graduation.submission.pojo.RolePermission;
import com.graduation.submission.pojo.User;
import com.graduation.submission.pojo.vo.PermissionVO;
import com.graduation.submission.pojo.vo.RoleVO;
import com.graduation.submission.service.PermissionService;
import com.graduation.submission.service.RoleService;
import com.graduation.submission.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName AuthController
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/24 19:27
 * @Version 1.0
 **/
@Controller
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;


    /**
     * 跳转到角色列表
     * @return
     */
    @RequestMapping("/roleManage")
    public ModelAndView toPage() {
        return new ModelAndView("/auth/roleManage");
    }

    /**
     * 权限列表
     * @return ok/fail
     */
    @RequestMapping(value = "/permList", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView permList() {
        ModelAndView mav = new ModelAndView("/auth/permList");
        try {
            List<Permission> permList = permissionService.permList();
            mav.addObject("permList", permList);
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    /**
     * 获取权限
     * @param id
     * @return
     */
    @RequestMapping(value = "/getPerm", method = RequestMethod.GET)
    @ResponseBody
    public Permission getPerm(
            @RequestParam("id") int id) {
        try {
            if (id > 0) {
                Permission perm = this.permissionService.getPermission(id);
                return perm;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加权限
     * @param type [0：编辑；1：新增子节点权限]
     * @param permission
     * @return ModelAndView ok/fail
     */
    @RequestMapping(value = "/setPerm", method = RequestMethod.POST)
    @ResponseBody public String setPerm(
            @RequestParam("type") int type, Permission permission) {
        try {
            if (null != permission) {
                Date date = new Date();
                if (0 == type) {
                    permission.setUpdateTime(date);
                    //编辑权限
                    this.permissionService.updatePerm(permission);
                } else if (1 == type) {
                    permission.setInsertTime(date);
                    //增加子节点权限
                    this.permissionService.addPermission(permission);
                }
                return "ok";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "设置权限出错，请您稍后再试";
    }

    /**
     * 删除权限
     * @param id
     * @return
     */
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public String del(
            @RequestParam("id") int id) {
        try {
            if (id > 0) {
                return this.permissionService.delPermission(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "删除权限出错，请您稍后再试";
    }

    /**
     * 角色列表
     * @return ok/fail
     */
    @RequestMapping(value = "/getRoleList", method = RequestMethod.GET)
    @ResponseBody
    public List<Role> getRoleList() {
        List<Role> roleList = new ArrayList<>();
        try {
            roleList = roleService.roleList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roleList;
    }

    /**
     * 删除角色以及它对应的权限
     * @param id
     * @return
     */
    @RequestMapping(value = "/delRole", method = RequestMethod.POST)
    @ResponseBody
    public String delRole(
            @RequestParam("id") int id) {
        try {
            if (id > 0) {
                return this.roleService.delRole(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "删除角色出错，请您稍后再试";
    }

    /**
     * 根据id查询角色后进行编辑
     * @return PermTreeDTO
     */
    @RequestMapping(value = "/updateRole/{id}", method = RequestMethod.GET)
    //@ResponseBody
    public ModelAndView updateRole(@PathVariable("id") Integer id) {
        ModelAndView mv=new ModelAndView("/auth/roleManage");
        try {
            if(null==id){
                mv.addObject("msg","请求参数有误，请您稍后再试");
                return mv;
            }
            mv.addObject("flag","updateRole");
            RoleVO rvo=this.roleService.findRoleAndPerms(id);
            //角色下的权限
            List<RolePermission> rps = rvo.getRolePermissions();
            //获取全部权限数据
            List<PermissionVO> pvos = permissionService.findPerms();
            for (RolePermission rp : rps) {
                //设置角色下的权限checked状态为：true
                for (PermissionVO pvo : pvos) {
                    if(String.valueOf(rp.getPermitId()).equals(String.valueOf(pvo.getId()))){
                        pvo.setChecked(true);
                    }
                }
            }
            mv.addObject("perms", pvos.toArray());
            //角色详情
            mv.addObject("roleDetail",rvo);
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("msg","请求异常，请您稍后再试");
        }
        return mv;
    }

    /**
     * 添加角色并授权
     * @return PermTreeDTO
     */
    @RequestMapping(value = "/addRole", method = RequestMethod.POST)
    @ResponseBody
    public String addRole(@RequestParam("permIds") String permIds, Role role) {
        try {
            if(StringUtils.isEmpty(permIds)){
                return "未授权，请您给该角色授权";
            }
            if(null == role){
                return "请您填写完整的角色数据";
            }
            role.setInsertTime(new Date());
            return roleService.addRole(role,permIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "操作错误，请您稍后再试";
    }

    /**
     * 查询权限树数据
     * @return PermTreeDTO
     */
    @RequestMapping(value = "/findPerms", method = RequestMethod.GET)
    @ResponseBody
    public List<PermissionVO> findPerms() {
        List<PermissionVO> pvo = new ArrayList<>();
        try {
            pvo = permissionService.findPerms();
            //生成页面需要的json格式
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pvo;
    }

    /**
     * 查找所有角色
     * @return
     */
    @RequestMapping(value = "/getRoles", method = RequestMethod.GET)
    @ResponseBody
    public List<Role> getRoles() {
        List<Role> roles = new ArrayList<>() ;
        try {
            return this.roleService.getRoles();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roles;
    }

    /**
     * 更新角色并授权
     * @return PermTreeDTO
     */
    @RequestMapping(value = "/setRole", method = RequestMethod.POST)
    @ResponseBody
    public String setRole(@RequestParam("rolePermIds") String permIds, Role role) {
        try {
            if(StringUtils.isEmpty(permIds)){
                return "未授权，请您给该角色授权";
            }
            if(null == role){
                return "请您填写完整的角色数据";
            }
            role.setUpdateTime(new Date());
            return roleService.updateRole(role,permIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "操作错误，请您稍后再试";
    }

    /**
     * 根据用户id查询权限树数据
     * @return PermTreeDTO
     */
    @RequestMapping(value = "/getUserPerms", method = RequestMethod.GET)
    @ResponseBody
    public List<Permission> getUserPerms() {
        List<Permission> permissions = new ArrayList<>();
        User existUser= (User) SecurityUtils.getSubject().getPrincipal();
        log.info(existUser.getId().toString());
        if(null==existUser){
            return permissions;
        }
        try {
            permissions = permissionService.findUserPermissions(existUser.getId());
            log.info(permissions.toString());
            //生成页面需要的json格式
            return permissions;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return permissions;
    }

}
