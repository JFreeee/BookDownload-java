package com.jiang.service;

import java.util.List;
import java.util.Map;

import com.jiang.entity.Book;

public interface BookService {
	
	List<Book> getBooks(String keyword);
	
	Book getBookById(Integer id);
	
	boolean existsByTitle(String title);
	
	void addBook(Book book);
	
	//删除图书
	Map<String, Object> deleteBook(Integer id, String email);
	
	//通过账号查询上传的图书
	List<Book> getBooksByEmail(String email);
	
	//所有图书
	List<Book> getAllBooks();
}
