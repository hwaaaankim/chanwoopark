package com.dev.Pt_CWP06.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.Pt_CWP06.model.Videodetail;
import com.dev.Pt_CWP06.repository.VideodetailRepository;

@Service
public class VideodetailService{

	@Autowired
	VideodetailRepository videodetailrepository;
	
	public Videodetail save(String username, Long videoid) {
		Videodetail videodetail = new Videodetail();
		videodetail.setUsername(username);
		videodetail.setVideoid(videoid);
		Date date = new Date();
		videodetail.setVideodate(date);
		return videodetailrepository.save(videodetail);
	};
}
