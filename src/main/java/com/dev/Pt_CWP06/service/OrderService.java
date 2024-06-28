package com.dev.Pt_CWP06.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.Pt_CWP06.model.bankda.Order;
import com.dev.Pt_CWP06.repository.OrderRepository;


@Service
public class OrderService{

	@Autowired
	OrderRepository orderRepository;
	
	public Order save(Order order) {
		Date day = new Date();
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat fo = new SimpleDateFormat("yyyyMMdd-HHmmssSS");
		
		String today = f.format(day);
		String order_id = fo.format(day);
		order.setOrder_id(order_id+order.getBuyer_cellphone().substring(9));
		order.setOrder_date(today);
		order.setSign(false);
		
		return orderRepository.save(order);
	}
	
	
	public int updateSign(String order_id) {
		
		if(orderRepository.idSearch(order_id).getSign()==false) {
			return orderRepository.updateSign(order_id);
		}else {
			return 0;
		}
	}
}
