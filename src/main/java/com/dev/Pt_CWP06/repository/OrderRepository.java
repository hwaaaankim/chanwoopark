package com.dev.Pt_CWP06.repository;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dev.Pt_CWP06.model.bankda.Order;



public interface OrderRepository extends JpaRepository<Order, Long>{

	List<Order> findAllBySign(Boolean sign);
	
	@Transactional
	@Query("SELECT o from Order o WHERE o.order_id = ?1")
	Order idSearch(String order_id);
	
	@Transactional
	int deleteAllById(Long id);
	
	@Transactional
	@Modifying
	@Query("UPDATE Order o SET o.sign = 1 WHERE order_id = ?1")
	int updateSign(String order_id);
	
	Page<Order> findAllByOrderByIdDesc(Pageable pabeable);
	
	@Transactional
	@Query("SELECT o from Order o WHERE o.buyer_name = ?1")
	Page<Order> findByNameQuery(String buyer_name, Pageable pageable);
	
	@Transactional
	@Query("SELECT o from Order o WHERE o.buyer_cellphone = ?1")
	Page<Order> findByPhoneQuery(String buyer_cellphone, Pageable pageable);
	
	@Transactional
	@Query("SELECT o from Order o WHERE o.buyer_email = ?1")
	Page<Order> findByEmailQuery(String buyer_email, Pageable pageable);
	
	@Transactional
	@Query("SELECT o from Order o WHERE o.sign = ?1")
	Page<Order> findBySignQuery(Boolean sign, Pageable pageable);
	
}
