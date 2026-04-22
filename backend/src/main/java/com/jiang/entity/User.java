package com.jiang.entity;

public class User {
	private Integer id;
	private String email;
	private String password;
	private int is_admin;

	public User() {
		super();
	}

	public User(Integer id, String email, String password, int is_admin) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.is_admin = is_admin;
	}

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getIs_admin() {
		return is_admin;
	}

	public void setIs_admin(int is_admin) {
		this.is_admin = is_admin;
	}

}
