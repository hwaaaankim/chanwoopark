package com.dev.Pt_CWP06.repository;


import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dev.Pt_CWP06.model.Videodetail;


public interface VideodetailRepository extends JpaRepository<Videodetail, Long>{
	
	@Transactional
	@Query("SELECT v from Videodetail v WHERE v.username = ?1 and v.videoid = ?2 and v.videodate > ?3")
	Videodetail record(String id, Long videoid, Date today);
	
	Page<Videodetail> findAllByUsernameOrderByVideodateDesc(Pageable pageable, String username);
	
}
