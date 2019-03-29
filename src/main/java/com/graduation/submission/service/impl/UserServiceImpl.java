package com.graduation.submission.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.graduation.submission.common.ResponseResult;
import com.graduation.submission.dao.RoleMapper;
import com.graduation.submission.dao.UserMapper;
import com.graduation.submission.dao.UserRoleMapper;
import com.graduation.submission.pojo.Role;
import com.graduation.submission.pojo.User;
import com.graduation.submission.pojo.UserRole;
import com.graduation.submission.pojo.dto.UserRoleDTO;
import com.graduation.submission.pojo.dto.UserSearchDTO;
import com.graduation.submission.pojo.vo.UserRoleVO;
import com.graduation.submission.service.UserService;
import com.graduation.submission.shiro.ShiroRealm;
import com.graduation.submission.utils.DateUtil;
import com.graduation.submission.utils.IStatusMessage;
import com.graduation.submission.utils.PageDataResult;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/5 1:42
 * @Version 1.0
 **/
@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory
            .getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ResponseResult register(User user, Integer roleId) {
        User existUser = this.userMapper.findUserByMobile(user.getMobile());
        ResponseResult responseResult = new ResponseResult();
        if (null != existUser) {
            responseResult.setCode(IStatusMessage.SystemStatus.EXIT_MOBILE.getCode());
            responseResult.setMessage(IStatusMessage.SystemStatus.EXIT_MOBILE.getMessage());
            return responseResult;
        }
        User exist = this.userMapper.findUserByName(user.getUsername());
        if (null != exist) {
            responseResult.setCode(IStatusMessage.SystemStatus.EXIT_USERNAME.getCode());
            responseResult.setMessage(IStatusMessage.SystemStatus.EXIT_USERNAME.getMessage());
            return responseResult;
        }
        user.setInsertTime(new Date());
        user.setIsUse(0);
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        this.userMapper.insert(user);
        Integer userId = user.getId();
        UserRole userRole = new UserRole();
        userRole.setRoleId(roleId);
        userRole.setUserId(userId);
        this.userRoleMapper.insert(userRole);
        responseResult.setCode(IStatusMessage.SystemStatus.SUCCESS
                .getCode());
        return responseResult;
    }

    @Override
    public User getUser(String username) {
        return this.userMapper.getUser(username);
    }

    @Override
    public PageDataResult getUsers(UserSearchDTO userSearch, int page, int limit) {
        // 时间处理
        if (null != userSearch) {
            if (StringUtils.isNotEmpty(userSearch.getInsertTimeStart())
                    && StringUtils.isEmpty(userSearch.getInsertTimeEnd())) {
                userSearch.setInsertTimeEnd(DateUtil.format(new Date()));
            } else if (StringUtils.isEmpty(userSearch.getInsertTimeStart())
                    && StringUtils.isNotEmpty(userSearch.getInsertTimeEnd())) {
                userSearch.setInsertTimeStart(DateUtil.format(new Date()));
            }
            if (StringUtils.isNotEmpty(userSearch.getInsertTimeStart())
                    && StringUtils.isNotEmpty(userSearch.getInsertTimeEnd())) {
                if (userSearch.getInsertTimeEnd().compareTo(
                        userSearch.getInsertTimeStart()) < 0) {
                    String temp = userSearch.getInsertTimeStart();
                    userSearch
                            .setInsertTimeStart(userSearch.getInsertTimeEnd());
                    userSearch.setInsertTimeEnd(temp);
                }
            }
        }
        PageDataResult pdr = new PageDataResult();
        PageHelper.startPage(page, limit);
        List<UserRoleDTO> urList = userMapper.getUsers(userSearch);
        // 获取分页查询后的数据
        PageInfo<UserRoleDTO> pageInfo = new PageInfo<>(urList);
        // 设置获取到的总记录数total：
        pdr.setTotals(Long.valueOf(pageInfo.getTotal()).intValue());
        // 将角色名称提取到对应的字段中
        if (null != urList && urList.size() > 0) {
            for (UserRoleDTO ur : urList) {
                List<Role> roles = roleMapper.getRolesByUserId(ur.getId());
                if (null != roles && roles.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < roles.size(); i++) {
                        Role r = roles.get(i);
                        sb.append(r.getRoleName());
                        if (i != (roles.size() - 1)) {
                            sb.append("，");
                        }
                    }
                    ur.setRoleNames(sb.toString());
                }
            }
        }
        pdr.setList(urList);
        return pdr;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 30000, rollbackFor = {
            RuntimeException.class, Exception.class })
    public String setUser(User user, String roleIds) {
        int userId;
        if (user.getId() != null) {
            // 判断用户是否已经存在
            User existUser = this.userMapper.findUserByMobile(user.getMobile());
            if (null != existUser
                    && !String.valueOf(existUser.getId()).equals(
                    String.valueOf(user.getId()))) {
                return "该手机号已经存在";
            }
            User exist = this.userMapper.findUserByName(user.getUsername());
            if (null != exist
                    && !String.valueOf(exist.getId()).equals(
                    String.valueOf(user.getId()))) {
                return "该用户名已经存在";
            }
            User dataUser = this.userMapper.selectByPrimaryKey(user.getId());
            // 版本不一致
            if (null != dataUser
                    && null != dataUser.getVersion()
                    && !String.valueOf(user.getVersion()).equals(
                    String.valueOf(dataUser.getVersion()))) {
                return "操作失败，请您稍后再试";
            }
            // 更新用户
            userId = user.getId();
            user.setUpdateTime(new Date());
            // 设置加密密码
            if (StringUtils.isNotBlank(user.getPassword())) {
                user.setPassword(DigestUtils.md5Hex(user.getPassword()));
            }
            this.userMapper.updateByPrimaryKeySelective(user);
            // 删除之前的角色
            List<UserRole> urs = this.userRoleMapper.findByUserId(userId);
            if (null != urs && urs.size() > 0) {
                for (UserRole ur : urs) {
                    this.userRoleMapper.deleteByPrimaryKey(ur);
                }
            }
            // 如果是自己，修改完成之后，直接退出；重新登录
            User adminUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (adminUser != null
                    && adminUser.getId().intValue() == user.getId().intValue()) {
                logger.debug("更新自己的信息，退出重新登录！adminUser=" + adminUser);
                SecurityUtils.getSubject().logout();
            }
            // 方案一【不推荐】：通过SessionDAO拿到所有在线的用户，Collection<Session> sessions =
            // sessionDAO.getActiveSessions();
            // 遍历找到匹配的，更新他的信息【不推荐，分布式或用户数量太大的时候，会有问题。】；
            // 方案二【推荐】：用户信息价格flag（或version）标记，写个拦截器，每次请求判断flag（或version）是否改动，如有改动，请重新登录或自动更新用户信息（推荐）；

            // 清除ehcache中所有用户权限缓存，必须触发鉴权方法才能执行授权方法doGetAuthorizationInfo
            RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils
                    .getSecurityManager();
            ShiroRealm authRealm = (ShiroRealm) rsm.getRealms().iterator()
                    .next();
            authRealm.clearCachedAuth();
            logger.debug("清除所有用户权限缓存！！！");
        } else {
            // 判断用户是否已经存在
            User existUser = this.userMapper.findUserByMobile(user.getMobile());
            if (null != existUser) {
                return "该手机号已经存在";
            }
            User exist = this.userMapper.findUserByName(user.getUsername());
            if (null != exist) {
                return "该用户名已经存在";
            }
            // 新增用户
            user.setInsertTime(new Date());
            user.setIsUse(0);
            // 设置加密密码
            if (StringUtils.isNotBlank(user.getPassword())) {
                user.setPassword(DigestUtils.md5Hex(user.getPassword()));
            } else {
                user.setPassword(DigestUtils.md5Hex("654321"));
            }
            this.userMapper.insert(user);
            userId = user.getId();
        }
        // 给用户授角色
        String[] arrays = roleIds.split(",");
        for (String roleId : arrays) {
            UserRole urk = new UserRole();
            urk.setRoleId(Integer.valueOf(roleId));
            urk.setUserId(userId);
            this.userRoleMapper.insert(urk);
        }
        return "ok";
    }

    @Override
    public String setUserUse(Integer id, Integer isUse, Integer version) {
        User dataUser = this.userMapper.selectByPrimaryKey(id);
        // 版本不一致
        if (null != dataUser
                && null != dataUser.getVersion()
                && !String.valueOf(version).equals(
                String.valueOf(dataUser.getVersion()))) {
            return "操作失败，请您稍后再试";
        }
        return this.userMapper.setUseUser(id,isUse) == 1 ? "ok"
                : "操作失败，请您稍后再试";
    }

    @Override
    public String delUser(Integer id) {
        return this.userMapper.deleteByPrimaryKey(id) == 1 ? "ok" : "删除失败，请您稍后再试";
    }

    @Override
    public UserRoleVO getUserAndRoles(Integer id) {
        // 获取用户及他对应的roleIds
        return this.userMapper.getUserAndRoles(id);
    }

    @Override
    public User findUserByMobile(String mobile) {
        return this.userMapper.findUserByMobile(mobile);
    }

    @Override
    public String sendMessage(String mobile) {
        return null;
    }

    @Override
    public int updatePwd(Integer id, String password) {
        return this.userMapper.updatePwd(id, password);
    }
}
