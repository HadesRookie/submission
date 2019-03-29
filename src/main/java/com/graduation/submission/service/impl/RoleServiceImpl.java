package com.graduation.submission.service.impl;

import com.graduation.submission.dao.RoleMapper;
import com.graduation.submission.dao.RolePermissionMapper;
import com.graduation.submission.pojo.Role;
import com.graduation.submission.pojo.RolePermission;
import com.graduation.submission.pojo.vo.RoleVO;
import com.graduation.submission.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * @ClassName RoleServiceImpl
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/5 1:14
 * @Version 1.0
 **/
@Service
public class RoleServiceImpl implements RoleService {
    private static final Logger logger = LoggerFactory
            .getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public List<Role> findUserRole(Integer userId) {
        return this.roleMapper.getRolesByUserId(userId);
    }

    @Override
    public List<Role> roleList() {
        return this.roleMapper.findList();
    }

    @Override
    public List<Role> getRoles() {
        return this.roleMapper.getRoles();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=30000,rollbackFor={RuntimeException.class, Exception.class})
    public String addRole(Role role, String permIds) {
        this.roleMapper.insert(role);
        int roleId=role.getId();
        String[] arrays=permIds.split(",");
        logger.debug("权限id =arrays="+arrays.toString());
        setRolePerms(roleId, arrays);
        return "ok";
    }

    @Override
    public String updateRole(Role role, String permIds) {
        int roleId=role.getId();
        String[] arrays=permIds.split(",");
        logger.debug("权限id =arrays="+arrays.toString());
        //1，更新角色表数据；
        int num=this.roleMapper.updateByPrimaryKeySelective(role);
        if(num<1){
            //事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "操作失败";
        }
        //2，删除原角色权限；
        batchDelRolePerms(roleId);
        //3，添加新的角色权限数据；
        setRolePerms(roleId, arrays);
        return "ok";
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=30000,rollbackFor={RuntimeException.class, Exception.class})
    public String delRole(int id) {
        //1.删除角色对应的权限
        batchDelRolePerms(id);
        //2.删除角色
        int num=this.roleMapper.deleteByPrimaryKey(id);
        if(num<1){
            //事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "操作失败";
        }
        return "ok";
    }

    @Override
    public RoleVO findRoleAndPerms(Integer id) {
        return this.roleMapper.findRoleAndPerms(id);
    }


    /**
     * 批量删除角色权限中间表数据
     * @param roleId
     */
    private void batchDelRolePerms(int roleId) {
        List<RolePermission> rps=this.rolePermissionMapper.findByRole(roleId);
        if(null!=rps && rps.size()>0){
            for (RolePermission rp : rps) {
                this.rolePermissionMapper.deleteByPrimaryKey(rp);
            }
        }
    }

    /**
     * 给当前角色设置权限
     * @param roleId
     * @param arrays
     */
    private void setRolePerms(int roleId, String[] arrays) {
        for (String permid : arrays) {
            RolePermission rp=new RolePermission();
            rp.setRoleId(roleId);
            rp.setPermitId(Integer.valueOf(permid));
            this.rolePermissionMapper.insert(rp);
        }
    }

    @Override
    public List<Role> getRoleByUser(Integer userId) {
        return this.roleMapper.getRoleByUserId(userId);
    }
}
