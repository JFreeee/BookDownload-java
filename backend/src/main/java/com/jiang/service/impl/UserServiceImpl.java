package com.jiang.service.impl;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiang.dao.UserMapper;
import com.jiang.entity.User;
import com.jiang.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;

	@Override
	public Boolean verifyUser(User user) {
		// TODO Auto-generated method stub
		String email = user.getEmail();
		String password = user.getPassword();
		User user2 = userMapper.getUser(email);
		if (user2 == null) {
			return false;
		} else if (password.equals(user2.getPassword())) {
			return true;
		} else {
			return false;
		}

	}

	// 注册
	@Override
	public boolean signupUser(User user) {
		int result = userMapper.insertUser(user);
		return result > 0;
	}

	// 获取所有用户
	@Override
	public List<User> getAllUsers() {
		return userMapper.getAllUsers();
	}

// ===== 判断是否管理员 =====
	@Override
	public boolean isAdmin(String email) {
		User user = userMapper.getUser(email);
		return user != null && user.getIs_admin() == 1;
	}

// ===== 设置 / 取消管理员 =====
	@Override
	public void toggleAdmin(Integer id) {

		User user = userMapper.getUserById(id);

		if (user == null) {
			throw new RuntimeException("用户不存在");
		}

		int newStatus = (user.getIs_admin() == 1) ? 0 : 1;

		userMapper.updateAdmin(id, newStatus);
	}

	// ===== 删除用户 =====
	@Override
	public void deleteUser(String email) {

		User user = userMapper.getUser(email);

		if (user == null) {
			throw new RuntimeException("用户不存在");
		}

		userMapper.deleteUser(email);
	}

	// 根据邮箱查询账号信息
	@Override
	public User findByEmail(String email) {
		return userMapper.getUser(email);
	}

	// 修改密码
	@Override
	public Map<String, Object> changePassword(String email, String oldPwd, String newPwd, String newPwd2) {

		Map<String, Object> res = new HashMap<>();

		// 参数校验
		if (oldPwd == null || newPwd == null || newPwd2 == null) {
			res.put("code", 0);
			res.put("msg", "参数不能为空");
			return res;
		}

		if (!newPwd.equals(newPwd2)) {
			res.put("code", 0);
			res.put("msg", "两次密码不一致");
			return res;
		}

		// 查用户
		User user = userMapper.getUser(email);

		if (user == null) {
			res.put("code", 0);
			res.put("msg", "用户不存在");
			return res;
		}

		// 校验旧密码
		if (!user.getPassword().equals(oldPwd)) {
			res.put("code", 0);
			res.put("msg", "旧密码错误");
			return res;
		}

		// 更新密码
		userMapper.updatePassword(email, newPwd);

		res.put("code", 1);
		res.put("msg", "修改成功");

		return res;

	}

}
