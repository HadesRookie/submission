package com.graduation.submission.service.impl;

import com.graduation.submission.dao.PermissionMapper;
import com.graduation.submission.pojo.Permission;
import com.graduation.submission.pojo.vo.PermissionVO;
import com.graduation.submission.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName PermissionServiceImpl
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/5 1:15
 * @Version 1.0
 **/
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<Permission> findUserPermissions(Integer userId) {
        return permissionMapper.getPermitByUserId(userId);
    }

    @Override
    public int addPermission(Permission permission) {
            return this.permissionMapper.insert(permission);
    }

    @Override
    public List<Permission> permList() {
        return this.permissionMapper.findAll();
    }

    @Override
    public int updatePerm(Permission permission) {
        return this.permissionMapper.updateByPrimaryKeySelective(permission);
    }

    @Override
    public Permission getPermission(int id) {
        return this.permissionMapper.selectByPrimaryKey(id);
    }

    @Override
    public String delPermission(int id) {
        //查看该权限是否有子节点，如果有，先删除子节点
        List<Permission> childPerm = this.permissionMapper.findChildPerm(id);
        if(null != childPerm && childPerm.size()>0){
            return "删除失败，请您先删除该权限的子节点";
        }
        if(this.permissionMapper.deleteByPrimaryKey(id)>0){
            return "ok";
        }else{
            return "删除失败，请您稍后再试";
        }
    }

    @Override
    public List<PermissionVO> findPerms() {
        return this.permissionMapper.findPerms();
    }


    @Override
    public List<Permission> findPermsByRoleId(Integer id) {
        return this.permissionMapper.findPermsByRole(id);
    }
}
