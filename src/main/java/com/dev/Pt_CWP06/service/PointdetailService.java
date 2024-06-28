package com.dev.Pt_CWP06.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.Pt_CWP06.model.Pointdetail;
import com.dev.Pt_CWP06.repository.PointdetailRepository;


@Service
public class PointdetailService{

	@Autowired
	PointdetailRepository pointdetailRepository;
	
	public Pointdetail save(Pointdetail pointdetail) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date pdate = new Date();
		String pointdate = format.format(pdate);
		pointdetail.setPointdate(pointdate);
		return pointdetailRepository.save(pointdetail);
	};
}
