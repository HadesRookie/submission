package com.graduation.submission.shiro;

import com.graduation.submission.pojo.Permission;
import com.graduation.submission.pojo.Role;
import com.graduation.submission.pojo.User;
import com.graduation.submission.service.PermissionService;
import com.graduation.submission.service.RoleService;
import com.graduation.submission.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName ShiroRealm
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/4 1:37
 * @Version 1.0
 **/
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserService userService;


    /**
     * 授权模块，获取用户角色和权限
     *
     * @param principals principal
     * @return AuthorizationInfo 权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //授权
        // 添加权限 和 角色信息
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 获取当前登陆用户
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (user.getMobile().equals("15622722079")) {
            // 超级管理员，添加所有角色、添加所有权限
            authorizationInfo.addRole("*");
            authorizationInfo.addStringPermission("*");
        } else {
            // 普通用户，查询用户的角色，根据角色查询权限
            Integer userId = user.getId();
            List<Role> roles = this.roleService.getRoleByUser(userId);
            if (null != roles && roles.size() > 0) {
                for (Role role : roles) {
                    authorizationInfo.addRole(role.getCode());
                    // 角色对应的权限数据
                    List<Permission> perms = this.permissionService.findPermsByRoleId(role
                            .getId());
                    if (null != perms && perms.size() > 0) {
                        // 授权角色下所有权限
                        for (Permission perm : perms) {
                            authorizationInfo.addStringPermission(perm
                                    .getPermitCode());
                        }
                    }
                }
            }
        }
        return authorizationInfo;
    }

    /**
     * 用户认证
     *
     * @param token AuthenticationToken 身份认证 token
     * @return AuthenticationInfo 身份认证信息
     * @throws AuthenticationException 认证相关异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 获取用户输入的用户名和密码
        String userName = (String) token.getPrincipal();

        // 通过用户名到数据库查询用户信息
        User user = this.userService.getUser(userName);
        if (user == null) {
            // 用户不存在
            return null;
        }else {
            // 密码存在
            // 第一个参数 ，登陆后，需要在session保存数据
            // 第二个参数，查询到密码(加密规则要和自定义的HashedCredentialsMatcher中的HashAlgorithmName散列算法一致)
            // 第三个参数 ，realm名字
            return new SimpleAuthenticationInfo(user, DigestUtils.md5Hex(user.getPassword()), getName());
        }


    }

    /**
     * 清除所有缓存【实测无效】
     */
    public void clearCachedAuth(){
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }
}
