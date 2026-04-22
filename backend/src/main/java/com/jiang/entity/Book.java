package com.jiang.entity;

import java.util.Date;

public class Book {
	private Integer id;
	private String email;
	private String title;
	private String pic;
	private String file_path;
	private Date upload_time;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getFile_path() {
		return file_path;
	}
	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}
	public Date getUpload_time() {
		return upload_time;
	}
	public void setUpload_time(Date upload_time) {
		this.upload_time = upload_time;
	}
	
	
	public Book() {
		super();
	}
	
	public Book(Integer id, String email, String title, String pic, String file_path, Date upload_time) {
		super();
		this.id = id;
		this.email = email;
		this.title = title;
		this.pic = pic;
		this.file_path = file_path;
		this.upload_time = upload_time;
	}
	
	
}
