package com.graduation.submission.service;

import com.graduation.submission.pojo.Role;
import com.graduation.submission.pojo.vo.RoleVO;

import java.util.List;

/**
 * @ClassName RoleService
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/5 1:14
 * @Version 1.0
 **/
public interface RoleService {

    /**
     * 根据用户获取角色列表
     * @param userId
     * @return
     */
    List<Role> findUserRole(Integer userId);

    /**
     * 查询所有角色
     * @return
     */
    List<Role> roleList();

    /**
     * 查找所有角色
     * @return
     */
    List<Role> getRoles();

    /**
     * 添加角色及其权限
     * @param role
     * @param permIds
     * @return
     */
    String addRole(Role role, String permIds);

    /**
     * 更新角色并授权
     * @param role
     * @param permIds
     * @return
     */
    String updateRole(Role role, String permIds);

    /**
     * 删除角色以及它对应的权限
     * @param id
     * @return
     */
    String delRole(int id);


    RoleVO findRoleAndPerms(Integer id);

    /**
     * 根据用户获取角色列表
     * @param userId
     * @return
     */
    List<Role> getRoleByUser(Integer userId);

}
