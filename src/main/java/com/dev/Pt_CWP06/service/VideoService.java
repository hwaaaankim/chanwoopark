package com.dev.Pt_CWP06.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dev.Pt_CWP06.model.video.Video;
import com.dev.Pt_CWP06.repository.VideoRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VideoService {

	@Autowired
	private VideoRepository videoRepository;
	
	public Video save(Video video) throws IllegalStateException, IOException {
		log.info("새 영상 등록");
		MultipartFile imagef01 = video.getImagef01();
		String str01 = "No Image";
		String image01  = "No Image";
		if(!imagef01.isEmpty()) {
//			str01 = "D:/BigData/MySource/Pt_CWP04/src/main/resources/static/library/admin/upload/"+imagef01.getOriginalFilename();
//			imagef01.transferTo(new File(str01));
//			image01 = "/library/admin/upload/" + imagef01.getOriginalFilename();
			
			str01 = "/home/hosting_users/noddaknet/tomcat/webapps/upload/"
					+imagef01.getOriginalFilename();
			imagef01.transferTo(new File(str01));
			image01 = "/library/admin/upload/"+imagef01.getOriginalFilename();
			video.setImage01(image01);
		}else {
			System.out.println(str01);
		}
		
		MultipartFile imagef02 = video.getImagef02();
		String str02 = "No Image";
		String image02  = "No Image";
		if(!imagef02.isEmpty()) {
//			str02 = "D:/BigData/MySource/Pt_CWP04/src/main/resources/static/library/admin/upload/"+imagef02.getOriginalFilename();
//			imagef02.transferTo(new File(str02));
//			image02 = "/library/admin/upload/" + imagef02.getOriginalFilename();
			
			str02 = "/home/hosting_users/noddaknet/tomcat/webapps/upload/"
					+imagef02.getOriginalFilename();
			imagef02.transferTo(new File(str02));
			image02 = "/library/admin/upload/"+imagef02.getOriginalFilename();
			video.setImage02(image02);
		}else {
			System.out.println(str02);
		}
		
		MultipartFile imagef03 = video.getImagef02();
		String str03 = "No Image";
		String image03  = "No Image";
		if(!imagef03.isEmpty()) {
//			str03 = "D:/BigData/MySource/Pt_CWP04/src/main/resources/static/library/admin/upload/"+imagef03.getOriginalFilename();
//			imagef03.transferTo(new File(str03));
//			image03 = "/library/admin/upload/" + imagef03.getOriginalFilename();
			
			str03 = "/home/hosting_users/noddaknet/tomcat/webapps/upload/"
					+imagef03.getOriginalFilename();
			imagef03.transferTo(new File(str03));
			image03 = "/library/admin/upload/"+imagef03.getOriginalFilename();
			video.setImage03(image03);
		}else {
			System.out.println(str03);
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date in_Date = new Date();
		String inDate = format.format(in_Date);
		video.setIndate(inDate);
		video.setClicks(0);
		video.setSell(0);
		video.setGood(0);
		
		return videoRepository.save(video);
	}
	
	public void videoUpdate(Video video) throws IllegalStateException, IOException {
		MultipartFile imagef01 = video.getImagef01();
		String str01 = "No Image";
		String image01  = "No Image";
		if(!imagef01.isEmpty()) {
//			str01 = "D:/BigData/MySource/Pt_CWP05/src/main/resources/static/library/admin/upload/"+imagef01.getOriginalFilename();
//			imagef01.transferTo(new File(str01));
//			image01 = "/library/admin/upload/" + imagef01.getOriginalFilename();
			str01 = "/home/hosting_users/noddaknet/tomcat/webapps/upload/"
					+imagef01.getOriginalFilename();
			imagef01.transferTo(new File(str01));
			image01 = "/library/admin/upload/"+imagef01.getOriginalFilename();
			video.setImage01(image01);
		}else {
			System.out.println(str01);
		}
		
		MultipartFile imagef02 = video.getImagef02();
		String str02 = "No Image";
		String image02  = "No Image";
		if(!imagef02.isEmpty()) {
//			str02 = "D:/BigData/MySource/Pt_CWP05/src/main/resources/static/library/admin/upload/"+imagef02.getOriginalFilename();
//			imagef02.transferTo(new File(str02));
//			image02 = "/library/admin/upload/" + imagef02.getOriginalFilename();
			
			str02 = "/home/hosting_users/noddaknet/tomcat/webapps/upload/"
					+imagef02.getOriginalFilename();
			imagef02.transferTo(new File(str02));
			image02 = "/library/admin/upload/"+imagef02.getOriginalFilename();
			video.setImage02(image02);
		}else {
			System.out.println(str02);
		}
		
		MultipartFile imagef03 = video.getImagef03();
		String str03 = "No Image";
		String image03  = "No Image";
		if(!imagef03.isEmpty()) {
//			str03 = "D:/BigData/MySource/Pt_CWP05/src/main/resources/static/library/admin/upload/"+imagef03.getOriginalFilename();
//			imagef03.transferTo(new File(str03));
//			image03 = "/library/admin/upload/" + imagef03.getOriginalFilename();
			
			str03 = "/home/hosting_users/noddaknet/tomcat/webapps/upload/"
					+imagef03.getOriginalFilename();
			imagef03.transferTo(new File(str03));
			image03 = "/library/admin/upload/"+imagef03.getOriginalFilename();
			video.setImage03(image03);
		}else {
			System.out.println(str03);
		}
		Optional<Video> vi = videoRepository.findById(video.getId());
		vi.ifPresent(selectVideo ->{
			selectVideo.setName(video.getName());
			selectVideo.setImage01(video.getImage01());
			selectVideo.setImage02(video.getImage02());
			selectVideo.setImage03(video.getImage03());
			selectVideo.setVideocode(video.getVideocode());
			selectVideo.setPrice(video.getPrice());
			selectVideo.setHashtag(video.getHashtag());
			selectVideo.setComment(video.getComment());
//			Video newVi =
			videoRepository.save(selectVideo);
//			System.out.println("NewVi = " + newVi);
		});
	}
	
}
