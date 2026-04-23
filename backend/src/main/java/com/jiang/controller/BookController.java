package com.jiang.controller;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.server.ObjID;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;

import com.alibaba.fastjson.JSONObject;
import com.jiang.entity.Book;
import com.jiang.service.BookService;
import com.jiang.service.CloudinaryService;
import com.jiang.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/book")
public class BookController {
	@Autowired
	private BookService bookService;
	@Autowired
	private UserService userService;
	@Autowired
	private CloudinaryService cloudinaryService;
//=====获取书籍详情=====
	@PostMapping("/book_info")
	public Map<String, Object> getBook(@RequestBody JSONObject params) {
		Integer id = params.getInteger("id");
		Book book = bookService.getBookById(id);
		Map<String, Object> map = new HashMap<>();
		map.put("book", book);
		return map;
	}

//=====获取所有书籍======
	@PostMapping("/allbooks")
	public Map<String, Object> getAllBooks( HttpSession session){
		Map<String, Object> res = new HashMap<>();
		
		String email = (String) session.getAttribute("email");
		if (email == null || !userService.isAdmin(email)) {
            res.put("code", 0);
            res.put("msg", "无权限");
            return res;
	}
		
		res.put("code", 1);
        res.put("data", bookService.getAllBooks());
        return res;
        }
	
//=====下载=====
	@GetMapping("/download")
	public ResponseEntity<?> download(Integer id, HttpSession session) {

	    // 登录校验
	    String email = (String) session.getAttribute("email");
	    if (email == null) {
	        return ResponseEntity.status(403).body("未登录");
	    }

	    // 查书
	    Book book = bookService.getBookById(id);
	    if (book == null) {
	        return ResponseEntity.badRequest().body("书不存在");
	    }

	    //  URL
	    String fileUrl = book.getFile_path();

	    //  直接跳转到云文件
	    return ResponseEntity
	            .status(302)
	            .header(HttpHeaders.LOCATION, fileUrl)
	            .build();
	}

//=====上传=====
	@PostMapping("/upload")
	public Map<String, Object> upload(
			@RequestParam("title") String title, 
			@RequestParam("pic") MultipartFile pic,
			@RequestParam("bookfile") MultipartFile bookfile,
			HttpSession session) {

		Map<String, Object> res = new HashMap<>();

		try {
			
			// ===== 登录校验 =====
	        String email = (String) session.getAttribute("email");
	        if (email == null) {
	            res.put("code", 0);
	            res.put("msg", "请先登录");
	            return res;
	        }
	        
	        //=====判断书籍是否已经存在=====
			if(bookService.existsByTitle(title)) {
				res.put("code", 0);
				res.put("msg", "上传失败：书籍已经存在");
				return res;				
			}
	         
			
			//  上传到云
			// 上传图片（默认 image）
			String picUrl = cloudinaryService.upload(pic, "images");

			// 上传PDF（必须指定 raw）
			String bookUrl = cloudinaryService.uploadRaw(bookfile, "books");

			// 组装对象
			Book book = new Book();
			book.setEmail(email);
			book.setTitle(title);
			book.setPic(picUrl); //存url
			book.setFile_path(bookUrl);  //存url

			// 调用Service保存数据库
			bookService.addBook(book);

			res.put("code", 1);
			res.put("msg", "上传成功");

		} catch (Exception e) {
			e.printStackTrace();
			res.put("code", 0);
			res.put("msg", "上传失败");
		}
		return res;
	}
	
	//=====删除图书======
	@PostMapping("/delete")
	public Map<String, Object> deleteBook(
	        @RequestBody Map<String, Integer> params,
	        HttpSession session) {

	    String email = (String) session.getAttribute("email");
	    Integer id = params.get("id");
	    Map<String, Object> res = new HashMap<>();

	    if (email == null) {
	        res.put("code", 0);
	        res.put("msg", "未登录");
	        return res;
	    }

	    return bookService.deleteBook(id, email);
	}

}
