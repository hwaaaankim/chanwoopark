package com.dev.Pt_CWP06.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dev.Pt_CWP06.model.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long>{
	
	Page<Notice> findAllByOrderBySignDescIdDesc(Pageable pageable);
	
	Notice findAllById(Long id);
	
	Optional<Notice> findById(Long id);
	
	@Transactional
	int deleteAllById(Long id);
	
	@Transactional
	@Modifying
	@Query("UPDATE Notice n SET n.clicks = n.clicks+1 WHERE id = ?1")
	int noticeClick(Long id);
	
}
