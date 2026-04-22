package com.jiang.service;

import java.util.List;
import java.util.Map;

import com.jiang.entity.User;

public interface UserService {
	// 获取所有用户
	List<User> getAllUsers();

	// 登陆验证
	Boolean verifyUser(User user);

	// 注册
	boolean signupUser(User user);

	// 查邮箱
	User findByEmail(String email);

	// 修改密码
	Map<String, Object> changePassword(String email, String oldPwd, String newPwd, String newPwd2);

	// 是否管理员
	boolean isAdmin(String email);

	// 切换管理员
	void toggleAdmin(Integer id);

	// 删除用户
	void deleteUser(String email);
}
