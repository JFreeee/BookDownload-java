package com.jiang.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.type.filter.AbstractClassTestingTypeFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiang.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	public UserController userController;
	@Autowired
	public BookController bookController;
	@Autowired
	private UserService userService;
	
	//获取所有用户
	@GetMapping("/users")
	public Map<String, Object> getAllUsers(HttpSession session){
		Map<String, Object> res = new HashMap<>();

        String email = (String) session.getAttribute("email");

        if (email == null || !userService.isAdmin(email)) {
            res.put("code", 0);
            res.put("msg", "无权限");
            return res;
        }

        res.put("code", 1);
        res.put("data", userService.getAllUsers());
        return res;
	}
	
	// 设置 / 取消管理员
    @PostMapping("/setAdmin")
    public Map<String, Object> setAdmin(@RequestBody Map<String, Object> params, HttpSession session) {
        Map<String, Object> res = new HashMap<>();

        Integer id = (Integer) params.get("id");

      //当前登录用户
    	String loginEmail = (String) session.getAttribute("email");
        
        if (loginEmail == null || !userService.isAdmin(loginEmail)) {
            res.put("code", 0);
            res.put("msg", "无权限");
            return res;
        }

        userService.toggleAdmin(id);

        res.put("code", 1);
        res.put("msg", "操作成功");
        return res;
    }

    // 删除用户
    @PostMapping("/deleteUser")
    public Map<String, Object> deleteUser(@RequestBody Map<String, Object> params, HttpSession session) {
        
    	Map<String, Object> res = new HashMap<>();
        
    	//从JSON中取
    	String email = (String) params.get("email");
        
    	//当前登录用户
    	String loginEmail = (String) session.getAttribute("email");
    	
    	//权限校验
        if (loginEmail == null || !userService.isAdmin(loginEmail)) {
            res.put("code", 0);
            res.put("msg", "无权限");
            return res;
        }

        userService.deleteUser(email);

        res.put("code", 1);
        res.put("msg", "删除成功");
        return res;
    }
}
