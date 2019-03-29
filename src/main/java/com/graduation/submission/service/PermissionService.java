package com.graduation.submission.service;

import com.graduation.submission.pojo.Permission;
import com.graduation.submission.pojo.vo.PermissionVO;

import java.util.List;

/**
 * @ClassName PermissionService
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/5 1:14
 * @Version 1.0
 **/
public interface PermissionService {

    /**
     * 根据用户id获取权限数据
     * @param userId
     * @return
     */
    List<Permission> findUserPermissions(Integer userId);

    int addPermission(Permission permission);

    List<Permission> permList();

    int updatePerm(Permission permission);

    Permission getPermission(int id);

    String delPermission(int id);

    /**
     * 关联查询权限树列表
     * @return
     */
    List<PermissionVO> findPerms();

    /**
     * 根据角色id获取权限数据
     * @param id
     * @return
     */
    List<Permission> findPermsByRoleId(Integer id);

}
