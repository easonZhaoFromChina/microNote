package com.asiainfo.domain.entity.user;

public class User {
	private Long id;
	private String username;
	private Integer age;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getAge() {
		return age;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
}
