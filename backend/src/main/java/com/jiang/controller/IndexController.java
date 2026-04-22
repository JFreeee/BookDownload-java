package com.jiang.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.jiang.entity.Book;
import com.jiang.entity.User;
import com.jiang.service.BookService;
import com.jiang.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/index")
public class IndexController {
	@Autowired
	private BookService bookService;

	@Autowired
	private UserService userService;
	
	//导航栏
	@PostMapping("/nav")
	public Map<String, Object> nav(HttpSession session){
		Map<String, Object> map = new HashMap<>();
		
		String email = (String) session.getAttribute("email");
		
		if(email != null) {
			map.put("loggedIn", true);
			map.put("email", email);
			
			User user = userService.findByEmail(email);
			map.put("is_admin", user.getIs_admin());
		}else {
			map.put("loggedIn", false);
		}
		
		return map;
	}

	//图书
	@PostMapping("/books")
	public Map<String, Object> getBooks(@RequestBody JSONObject params, HttpSession session) {

		String keyword = params.getString("keyword");

// ===== 查询图书 =====
		List<Book> books = bookService.getBooks(keyword);

// ===== 登录状态 =====
		String email = (String) session.getAttribute("email");
		boolean loggedIn = (email != null);

		int is_admin = 0;

		if (loggedIn) {
			User user = userService.findByEmail(email);
			if (user != null) {
				is_admin = user.getIs_admin();
			}
		}

// ===== 返回 =====
		Map<String, Object> result = new HashMap<>();
		result.put("loggedIn", loggedIn);
		result.put("email", email);
		result.put("is_admin", is_admin);
		result.put("books", books);

		return result;
	}
}
