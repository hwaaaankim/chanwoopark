package com.dev.Pt_CWP06.model.video;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import com.dev.Pt_CWP06.model.Videodetail;

import lombok.Data;

@Data
@Entity
public class Video {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="image01")
	private String image01;
	
	@Transient
	private MultipartFile imagef01;
	
	@Column(name="image02")
	private String image02;
	
	@Transient
	private MultipartFile imagef02;
	
	@Column(name="image03")
	private String image03;
	
	@Transient
	private MultipartFile imagef03;
	
	@Column(name="videocode")
	private String videocode;
	
	@Column(name="price")
	private int price;
	
	@Column(name="indate")
	private String indate;
	
	@Column(name="hashtag")
	private String hashtag;
	
	@Column(name="comment")
	private String comment;
	
	@Column(name="clicks")
	private int clicks;
	
	@Column(name="sell")
	private int sell;
	
	@Column(name="good")
	private int good;
	
	@Transient
	private Boolean charged;
	
	@OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Videodetail> details = new ArrayList<>();
	
}
