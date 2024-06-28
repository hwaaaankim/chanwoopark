package com.dev.Pt_CWP06.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dev.Pt_CWP06.model.Board;

public interface BoardRepository extends JpaRepository<Board, Long>{

	Page<Board> findAllByOrderByIdDesc(Pageable pageable);
	
	Board findAllById(Long id);
	
	@Transactional
	int deleteAllById(Long id);
	
	
	@Transactional
	@Modifying
	@Query("UPDATE Board b SET b.sign = 1 WHERE id = ?1")
	int updateSign(Long id);
	
	Optional<Board> findById(Long id);
}
