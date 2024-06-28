package com.dev.Pt_CWP06.repository;


import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dev.Pt_CWP06.model.Member;


public interface MemberRepository extends JpaRepository<Member, Long>{
	
	Member findByUsername(String username);
	
	Member findByNickname(String nickname);
	
	Member findAllById(Long id);
	
	Member findAllByPhone(String phone);
	
	Page<Member> findAllByOrderByJoindateAsc(Pageable pageable);
	
	Page<Member> findAllByNameContainingOrderByJoindateAsc(String name, Pageable pageable);
	
	Page<Member> findAllByNicknameContainingOrderByJoindateAsc(String nickname, Pageable pageable);
	
	Page<Member> findAllByPhoneContainingOrderByJoindateAsc(String phone, Pageable pageable);
	
	Page<Member> findAllByUsernameContainingOrderByJoindateAsc(String username, Pageable pageable);
	
	@Transactional
	int deleteAllById(Long id);
	
	@Transactional
	@Modifying
	@Query("UPDATE Member m SET m.point = m.point - ?2 WHERE m.id = ?1")
	int pointUse(Long id, int point);
	
	@Transactional
	@Modifying
	@Query("UPDATE Member m SET m.point = m.point + ?2 WHERE m.id = ?1")
	int pointCharge(Long id, int point);
	
	@Transactional
	@Modifying
	@Query("UPDATE Member m SET m.point = m.point - ?2 WHERE m.id = ?1")
	int pointMinus(Long id, int point);
	
	Member findByPhone(String phone);
	
	@Transactional
	@Modifying
	@Query("UPDATE Member m SET m.point = m.point + ?1 WHERE phone = ?2")
	int pointCharge(int point, String phone);
}























