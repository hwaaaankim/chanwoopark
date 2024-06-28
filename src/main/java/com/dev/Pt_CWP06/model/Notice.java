package com.dev.Pt_CWP06.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.lang.Nullable;

import lombok.Data;

@Data
@Entity
public class Notice {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Nullable
	@Column(name="subject")
	private String subject;
	
	@Nullable
	@Column(name="content")
	private String content;
	
	@Nullable
	@Column(name="indate")
	private String indate;
	
	@Nullable
	@Column(name="clicks")
	private int clicks;
	
	@Nullable
	@Column(name="sign")
	private Boolean sign;
}






















