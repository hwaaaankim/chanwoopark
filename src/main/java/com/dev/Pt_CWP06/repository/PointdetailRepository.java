package com.dev.Pt_CWP06.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.Pt_CWP06.model.Pointdetail;


public interface PointdetailRepository extends JpaRepository<Pointdetail, Long>{

	Page<Pointdetail> findAllByUsernameOrderByPointdateDesc(String username, Pageable pageable);
}
