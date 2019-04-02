package com.graduation.submission.service;

import com.graduation.submission.common.ResponseResult;
import com.graduation.submission.pojo.User;
import com.graduation.submission.pojo.dto.UserSearchDTO;
import com.graduation.submission.pojo.vo.UserManuscriptVO;
import com.graduation.submission.pojo.vo.UserRoleVO;
import com.graduation.submission.utils.PageDataResult;

/**
 * @ClassName UserService
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/5 1:42
 * @Version 1.0
 **/
public interface UserService {

    /**
     * 注册用户
     * @param user
     * @param roleId
     * @return
     */
    ResponseResult register(User user, Integer roleId);



    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    User getUser(String username);

    /**
     * 分页查询用户列表
     * @param page
     * @param limit
     * @return
     */
    PageDataResult getUsers(UserSearchDTO userSearch, int page, int limit);

    /**
     *	设置用户【新增或更新】
     * @param user
     * @param roleIds
     * @return
     */
    String setUser(User user, String roleIds);

    /**
     * 设置用户是否可用
     * @param id
     * @param isUse
     * @return
     */
    String setUserUse(Integer id, Integer isUse,
                      Integer version);

    /**
     * 删除用户
     * @param id
     * @return
     */
    String delUser(Integer id);

    /**
     * 查询用户数据
     * @param id
     * @return
     */
    UserRoleVO getUserAndRoles(Integer id);

    /**
     * 根据手机号查询用户数据
     * @param mobile
     * @return
     */
    User findUserByMobile(String mobile);

    /**
     * 根据手机号发送短信验证码
     * @param mobile
     * @return
     */
    String sendMessage(String mobile);

    /**
     * 修改用户手机号
     * @param id
     * @param password
     * @return
     */
    int updatePwd(Integer id, String password);

    UserManuscriptVO getUserByManuscriptId(Integer id);

}
