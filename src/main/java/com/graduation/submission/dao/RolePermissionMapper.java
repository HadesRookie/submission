package com.graduation.submission.dao;

import com.graduation.submission.pojo.RolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName RolePermissionMapper
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/18 21:32
 * @Version 1.0
 **/
@Mapper
@Component
public interface RolePermissionMapper {

    int deleteByPrimaryKey(RolePermission key);

    int insert(RolePermission record);

    int insertSelective(RolePermission record);

    List<RolePermission> findByRole(int roleId);
}
