package com.jiang.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import com.alibaba.fastjson.JSONObject;
import com.jiang.entity.Book;
import com.jiang.entity.ReturnObj;
import com.jiang.entity.User;
import com.jiang.service.BookService;
import com.jiang.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/user") // 统一前缀
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private BookService bookService;

	// ================= 登录 =================
	@PostMapping("/login")
	public ReturnObj login(@RequestBody JSONObject params, HttpSession session) {
		// System.out.println("进入login接口");
		String email = params.getString("email");
		String password = params.getString("password");

		User user = new User();
		user.setEmail(email);
		user.setPassword(password);

		boolean result = userService.verifyUser(user);

		if (result) {

			session.setAttribute("email", email); // 保存登陆状态
			// System.out.println("没问题");
			return ReturnObj.success(200, "登录成功");
		} else {
			return ReturnObj.success(400, "邮箱或密码错误");
		}
	}

	// ================= 退出 =================
	@PostMapping("/logout")
	public Map<String, Object> logout(HttpSession session) {

		session.invalidate();

		Map<String, Object> map = new HashMap<>();
		map.put("success", true);

		return map;
	}

	// ================= 注册 =================
	@PostMapping("/signup")
	public ReturnObj signup(@RequestBody JSONObject params) {

		String email = params.getString("email").trim();
		String password = params.getString("password").trim();
		String password2 = params.getString("password2").trim();

		// ===== 校验 =====
		if (email == null || !email.matches("^[a-zA-Z0-9_.@]+$")) {
			return ReturnObj.success(0, "邮箱格式不正确");
		}

		if (email.length() < 6 || email.length() > 50) {
			return ReturnObj.success(0, "邮箱长度在6~50位之间");
		}

		if (password == null || password.isEmpty()) {
			return ReturnObj.success(1, "密码不能为空");
		}

		if (password.length() < 6 || password.length() > 50) {
			return ReturnObj.success(1, "密码长度在6~50位之间");
		}

		if (!password.matches("^[a-zA-Z0-9_.]+$")) {
			return ReturnObj.success(1, "密码格式不正确");
		}

		if (!password.equals(password2)) {
			return ReturnObj.success(2, "两次密码不一致");
		}

		// ===== 查重 =====
		User exist = userService.findByEmail(email);
		if (exist != null) {
			return ReturnObj.success(0, "邮箱已被注册");
		}

		// ===== 注册 =====
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		user.setIs_admin(0);

		boolean result = userService.signupUser(user);

		if (result) {
			return ReturnObj.success(100, "注册成功,正在跳转到登陆页面...");
		} else {
			return ReturnObj.success(1, "注册失败");
		}
	}

	// =========修改密码=========
	@PostMapping("/changePassword")
	public Map<String, Object> changePassword(@RequestBody Map<String, String> params, HttpSession session) {
		String email = (String) session.getAttribute("email");

		Map<String, Object> res = new HashMap<>();

		if (email == null) {
			res.put("code", 0);
			res.put("msg", "未登录");
			return res;
		}
		String oldPwd = params.get("oldPwd");
		String newPwd = params.get("newPwd");
		String newPwd2 = params.get("newPwd2");

		return userService.changePassword(email, oldPwd, newPwd, newPwd2);
	}

	// 用户登录
	@GetMapping("/mypage")
	public Map<String, Object> mypage(HttpSession session) {

		Map<String, Object> res = new HashMap<>();

		// 1️⃣ 登录校验
		String email = (String) session.getAttribute("email");
		if (email == null) {
			res.put("code", 0);
			res.put("msg", "未登录");
			return res;
		}

		// 2️⃣ 查询用户信息
		User user = userService.findByEmail(email);

		// 3️⃣ 查询该用户上传的书
		List<Book> books = bookService.getBooksByEmail(email);

		// 4️⃣ 组装返回数据
		Map<String, Object> data = new HashMap<>();
		data.put("email", user.getEmail());
		data.put("books", books);

		res.put("code", 1);
		res.put("data", data);

		return res;
	}

}