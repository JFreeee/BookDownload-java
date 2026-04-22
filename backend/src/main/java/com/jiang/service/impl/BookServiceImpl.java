package com.jiang.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiang.dao.BookMapper;
import com.jiang.dao.UserMapper;
import com.jiang.entity.Book;
import com.jiang.service.BookService;
import com.jiang.service.UserService;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookMapper bookMapper;
	
	@Autowired
	private UserService userService;

	@Override
	public List<Book> getBooks(String keyword) {
		if (keyword != null && !keyword.trim().isEmpty()) {
			return bookMapper.searchBooks(keyword);
		} else {
			return bookMapper.getAllBooks();
		}
	}

	@Override
	public Book getBookById(Integer id) {
		return bookMapper.getBookById(id);
	}
	
	@Override
	public boolean existsByTitle(String title) {
	    return bookMapper.findByTitle(title) != null;
	}
	
	@Override
	public void addBook(Book book) {
		bookMapper.insertBook(book);
	}
	
	//获取所有图书
	@Override
	public List<Book> getAllBooks(){
		return bookMapper.getAllBooks();
	}
	
	//删除图书
	@Override
	public Map<String, Object> deleteBook(Integer id, String email) {

	    Map<String, Object> res = new HashMap<>();

	    Book book = bookMapper.getBookById(id);

	    if (book == null) {
	        res.put("code", 0);
	        res.put("msg", "书不存在");
	        return res;
	    }
	    
	 // ===== 判断是否管理员 =====
	    boolean isAdmin = userService.isAdmin(email);

	    // 权限校验（核心）
	    if (!book.getEmail().equals(email) && !isAdmin) {
	        res.put("code", 0);
	        res.put("msg", "无权限删除");
	        return res;
	    }

	    // 删除数据库
	    bookMapper.deleteById(id);

	    res.put("code", 1);
	    res.put("msg", "删除成功");

	    return res;
	}
	
	//通过账号查询上传的电子书
	@Override
	public List<Book> getBooksByEmail(String email) {
	    return bookMapper.getBooksByEmail(email);
	}
}
