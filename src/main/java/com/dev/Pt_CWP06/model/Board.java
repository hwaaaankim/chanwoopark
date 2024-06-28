package com.dev.Pt_CWP06.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.sun.istack.Nullable;

import lombok.Data;

@Entity
@Data
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Nullable
	@Column(name="indate")
	private String indate;
	
	@Nullable
	@Column(name="username")
	private String username;
	
	@Nullable
	@Column(name="phone")
	private String phone;
	
	@Nullable
	@Column(name="name")
	private String name;
	
	@Nullable
	@Column(name="category")
	private String category;
	
	@Nullable
	@Column(name="message")
	private String message;
	
	@Nullable
	@Column(name="sign")
	private Boolean sign;
}











