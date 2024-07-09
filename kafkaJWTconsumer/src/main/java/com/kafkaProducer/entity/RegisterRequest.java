package com.kafkaProducer.entity;


import jakarta.persistence.Column;

public class RegisterRequest {


	private String name;

	@Column(unique = true)
	private String email;

	private String password;

	private com.kafkaProducer.entity.Role role;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public com.kafkaProducer.entity.Role getRole() {
		return role;
	}
	public void setRole(com.kafkaProducer.entity.Role role) {
		this.role = role;
	}


}


