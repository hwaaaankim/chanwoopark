package com.dev.Pt_CWP06.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.dev.Pt_CWP06.model.video.Video;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
public class Videodetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="username")
	private String username;
	
	@Column(name="videoid")
	private Long videoid;
	
	@Column(name="videodate")
	private Date videodate;
	
	@Transient
	private Boolean sign;
	
	@Transient
	private String date;
	
	@ManyToOne
	@JoinColumn(name = "videoid", referencedColumnName = "id",insertable = false, updatable = false)
	@JsonIgnore
	private Video video;
	
}

























