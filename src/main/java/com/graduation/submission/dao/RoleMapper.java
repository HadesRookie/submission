package com.graduation.submission.dao;

import com.graduation.submission.pojo.Role;
import com.graduation.submission.pojo.vo.RoleVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName RoleMapper
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/4 22:48
 * @Version 1.0
 **/
@Mapper
@Component
public interface RoleMapper {

    List<Role> getRolesByUserId(Integer userId);

    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    /**
     * 分页查询所有的角色列表
     * @return
     */
    List<Role> findList();

    /**
     * 获取角色相关的数据
     * @param id
     * @return
     */
    RoleVO findRoleAndPerms(Integer id);

    List<Role> getRoleByUserId(Integer userId);

    List<Role> getRoles();

}
