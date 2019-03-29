package com.graduation.submission.dao;

import com.graduation.submission.pojo.Permission;
import com.graduation.submission.pojo.vo.PermissionVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName PermissionMapper
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/4 22:48
 * @Version 1.0
 **/
@Mapper
@Component
public interface PermissionMapper {

    List<Permission> getPermitByUserId(Integer userId);

    int deleteByPrimaryKey(Integer id);

    int insert(Permission  record);

    int insertSelective(Permission record);

    Permission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Permission record);


    /**
     * 查找所有权限数据
     * @return
     */
    List<Permission> findAll();

    /**
     * 查找所有子节点
     * @param pid
     * @return
     */
    List<Permission> findChildPerm(int pid);

    /**
     * 查询权限树列表
     * @return
     */
    List<PermissionVO> findPerms();

    /**
     * 根据角色id获取权限数据
     * @param roleId
     * @return
     */
    List<Permission> findPermsByRole(Integer roleId);


}


