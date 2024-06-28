package com.dev.Pt_CWP06.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.Pt_CWP06.model.Accessdetail;
import com.dev.Pt_CWP06.repository.AccessdetailRepository;

@Service
public class AccessdetailService{

	@Autowired
	AccessdetailRepository accessdetailRepository;
	
	public Accessdetail save(String username) {
		Accessdetail accessDetail = new Accessdetail();
		accessDetail.setUsername(username);
		Date date = new Date();
		accessDetail.setJoindate(date);
		
		return accessdetailRepository.save(accessDetail);
	}
	
}
