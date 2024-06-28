package com.dev.Pt_CWP06.service;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.Pt_CWP06.model.Notice;
import com.dev.Pt_CWP06.repository.NoticeRepository;

@Service
public class NoticeService {

	@Autowired
	NoticeRepository noticeRepository;
	
	public Notice save(Notice notice) {
		
		Date today = new Date();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String indate = f.format(today);
		notice.setIndate(indate);
		
		return noticeRepository.save(notice);
	}
	
	public void noticeUpdate(Notice notice) {
		Optional<Notice> n = noticeRepository.findById(notice.getId());
		Date day = new Date();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String today = f.format(day); 
				
		n.ifPresent(selectNotice->{
			selectNotice.setContent(notice.getContent());
			selectNotice.setSubject(notice.getSubject());
			selectNotice.setSign(notice.getSign());
			selectNotice.setIndate(today);
			
			noticeRepository.save(selectNotice);
		});
	}
	
}














