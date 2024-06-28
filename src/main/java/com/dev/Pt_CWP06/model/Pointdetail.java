package com.dev.Pt_CWP06.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Entity
public class Pointdetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="username")
	private String username;
	
	@Column(name="sign")
	private Boolean sign;
	
	@Column(name="point")
	private int point;
	
	@Column(name="pointdate")
	private String pointdate;
	
	@Column(name="pointcontent")
	private String pointcontent;
	
	@Transient
	private String pointC;
	
}
