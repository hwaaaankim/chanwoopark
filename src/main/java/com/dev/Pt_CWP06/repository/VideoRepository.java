package com.dev.Pt_CWP06.repository;


import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dev.Pt_CWP06.model.video.Video;


public interface VideoRepository extends JpaRepository<Video, Long>{
		
	Page<Video> findAllByOrderByIdDesc(Pageable pageable);
	
	Page<Video> findAllByOrderByClicksDesc(Pageable pageable);
	
	Video findAllById(Long id);
	
	@Transactional
	int deleteAllById(Long id);
	
	@Transactional
	@Modifying
	@Query("UPDATE Video v SET v.clicks = v.clicks+1 WHERE id = ?1")
	int updateClicks(Long id);
	
	@Transactional
	@Modifying
	@Query("UPDATE Video v SET v.sell = v.sell+1 WHERE id = ?1")
	int updateSell(Long id);
	
	Optional<Video> findById(Long id);
}