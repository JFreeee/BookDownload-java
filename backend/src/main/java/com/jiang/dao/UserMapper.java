package com.jiang.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jiang.entity.User;

@Mapper
public interface UserMapper {

	// 根据邮箱查询用户
	User getUser(String email);

	// 通过ID查用户
	User getUserById(Integer id);

	// 获取所有用户
	List<User> getAllUsers();

	// 新增用户
	int insertUser(User user);

	// 修改密码
	void updatePassword(@Param("email") String email, @Param("password") String password);

	// 删除用户
	void deleteUser(String email);

	// 设置取消管理员
	void updateAdmin(@Param("id") Integer id, 
			@Param("is_admin") Integer is_admin);
}
