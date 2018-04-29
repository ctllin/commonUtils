package com.ctl.utils.thread;

public class User {
	private int id;
	private String username;
	private String name;
	private String password;
	private String gender;

	public User() {
	}

	public int getId() {
		return id;
	}

	public User(int id, String username, String name, String password) {
		super();
		this.id = id;
		this.username = username;
		this.name = name;
		this.password = password;
	}

	public User(int id, String username, String name) {
		super();
		this.id = id;
		this.username = username;
		this.name = name;
	}

	public User(int id, String username) {
		super();
		this.id = id;
		this.username = username;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User(int id, String username, String name, String password,
			String gender) {
		super();
		this.id = id;
		this.username = username;
		this.name = name;
		this.password = password;
		this.gender = gender;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String toString() {
		return "id:" + this.id + "  username:" + this.username + "  name:"
				+ this.name + "  password:" + this.password + "  gender:"
				+ this.gender;
	}
}
