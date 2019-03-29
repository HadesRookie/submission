package com.graduation.submission.dao;

import com.graduation.submission.pojo.User;
import com.graduation.submission.pojo.dto.UserRoleDTO;
import com.graduation.submission.pojo.dto.UserSearchDTO;
import com.graduation.submission.pojo.vo.UserRoleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName UserMapper
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/4 22:47
 * @Version 1.0
 **/
@Component
@Mapper
public interface UserMapper {

    User getUser(String username);

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 分页查询用户数据
     * @return
     */
    List<UserRoleDTO> getUsers(@Param("userSearch") UserSearchDTO userSearch);

    /**
     * 删除用户
     * @param id
     * @return
     */
    int delUser(Integer id);

    /**
     * 设置用户是否可用
     * @param id
     * @param isUse
     * @return
     */
    int setUseUser(@Param("id")Integer id,@Param("isUse")Integer isUse);

    /**
     * 查询用户及对应的角色
     * @param id
     * @return
     */
    UserRoleVO getUserAndRoles(Integer id);

    /**
     * 根据用户名和密码查找用户
     * @param username
     * @param password
     * @return
     */
    User findUser(@Param("username") String username,
                  @Param("password") String password);

    /**
     *	根据手机号获取用户数据
     * @param mobile
     * @return
     */
    User findUserByMobile(String mobile);

    /**
     * 根据用户名获取用户数据
     * @param username
     * @return
     */
    User findUserByName(String username);

    /**
     * 修改用户密码
     * @param id
     * @param password
     * @return
     */
    int updatePwd(@Param("id") Integer id, @Param("password") String password);

}
