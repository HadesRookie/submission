package com.graduation.submission.dao;

import com.graduation.submission.pojo.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName UserRoleMapper
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/18 21:33
 * @Version 1.0
 **/
@Mapper
@Component
public interface UserRoleMapper {

    int deleteByPrimaryKey(UserRole key);

    int insert(UserRole record);

    int insertSelective(UserRole record);

    /**
     * 根据用户获取用户角色中间表数据
     * @param userId
     * @return
     */
    List<UserRole> findByUserId(int userId);
}
