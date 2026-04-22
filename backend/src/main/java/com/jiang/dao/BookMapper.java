package com.jiang.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jiang.entity.Book;

@Mapper
public interface BookMapper {
	//查询全部图书
	List<Book> getAllBooks();
	
	//搜索图书
	List<Book> searchBooks(@Param("keyword") String keyword);
	
	//根据图书ID返回图书
	Book getBookById(Integer id);
	
	//上传图书
	Book findByTitle(String title);
	void insertBook(Book book);
	
	//删除图书
	void deleteById(Integer id);
	
	//通过账号查上传的图书
	List<Book> getBooksByEmail(String email);
}
