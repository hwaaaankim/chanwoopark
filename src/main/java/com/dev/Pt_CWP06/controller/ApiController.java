package com.dev.Pt_CWP06.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.EncoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.Pt_CWP06.model.Member;
import com.dev.Pt_CWP06.model.Pointdetail;
import com.dev.Pt_CWP06.model.bankda.Order;
import com.dev.Pt_CWP06.model.bankda.Orders;
import com.dev.Pt_CWP06.model.bankda.Requests;
import com.dev.Pt_CWP06.model.bankda.Result;
import com.dev.Pt_CWP06.repository.MemberRepository;
import com.dev.Pt_CWP06.repository.OrderRepository;
import com.dev.Pt_CWP06.service.MemberService;
import com.dev.Pt_CWP06.service.OrderService;
import com.dev.Pt_CWP06.service.PointdetailService;
import com.dev.Pt_CWP06.smsService.ChargeSmsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@RestController
@RequestMapping("/api")
public class ApiController {

	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	PointdetailService pointdetailService;
	
	@Autowired
	ChargeSmsService chargeSmsService;
	
	@GetMapping("/orders")
	public Map<String, List<Orders>> orders() {
		
		Map<String, List<Orders>> orders = new HashMap<>();
		
		List<Orders> list = new ArrayList<Orders>();
		List<Order> order = orderRepository.findAllBySign(false);
		
		for(int x=0;x<order.size();x++) {
			Orders or = new Orders();
			or.setOrder_id(order.get(x).getOrder_id());
			or.setBuyer_name(order.get(x).getBuyer_name());
			or.setBuyer_email(order.get(x).getBuyer_email());
			or.setBuyer_cellphone(order.get(x).getBuyer_cellphone());
			List<Map<String,Object>> li = new ArrayList<>();
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("product_name", order.get(x).getItem());
			li.add(item);
			or.setItem(li);
			or.setBilling_name(order.get(x).getBilling_name());
			or.setBank_account_no(order.get(x).getBank_account_no());
			or.setBank_code_name(order.get(x).getBank_code_name());
			or.setOrder_date(order.get(x).getOrder_date());
			or.setOrder_price_amount(order.get(x).getOrder_price_amount());
			list.add(or);
		}
		
		orders.put("orders",list);
		return orders;
	}
	
	@PostMapping("/perOrder")
	public Map<String, Object> perOrder(@RequestBody(required = false) Map<String, Object> param) {
		Orders or = new Orders();
		Map<String, Object> orders = new HashMap<>();
		if(param!=null) {
			if(orderRepository.idSearch(param.get("order_id").toString())!=null) {
				or.setOrder_id(orderRepository.idSearch(param.get("order_id").toString()).getOrder_id());
				or.setBuyer_name(orderRepository.idSearch(param.get("order_id").toString()).getBuyer_name());
				or.setBuyer_email(orderRepository.idSearch(param.get("order_id").toString()).getBuyer_email());
				or.setBuyer_cellphone(orderRepository.idSearch(param.get("order_id").toString()).getBuyer_cellphone());
				List<Map<String,Object>> li = new ArrayList<>();
				Map<String, Object> item = new HashMap<String, Object>();
				item.put("product_name", orderRepository.idSearch(param.get("order_id").toString()).getItem());
				li.add(item);
				or.setItem(li);
				or.setBilling_name(orderRepository.idSearch(param.get("order_id").toString()).getBilling_name());
				or.setBank_account_no(orderRepository.idSearch(param.get("order_id").toString()).getBank_account_no());
				or.setBank_code_name(orderRepository.idSearch(param.get("order_id").toString()).getBank_code_name());
				or.setOrder_date(orderRepository.idSearch(param.get("order_id").toString()).getOrder_date());
				or.setOrder_price_amount(orderRepository.idSearch(param.get("order_id").toString()).getOrder_price_amount());
				
				orders.put("order",or);
			}else {
				orders.put("return_code","415");
				orders.put("description","order_id 오류");
			}
		}else {
			orders.put("return_code","400");
			orders.put("description","parameter 없음");
		}
		return orders;
	}
	
	@PostMapping("/confirm")
	public Map<String, Object> confirm(@RequestBody(required = false) String param) throws JsonMappingException, JsonProcessingException, EncoderException {
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, List<Requests>>>(){}.getType();
		
		System.out.println("param : " + param);
		
		
		List<Result> list = new ArrayList<Result>();
		Map<String, Object> result = new HashMap<String, Object>();
		
		int cnt=0;
		
		try {
			if(!"".equals(param)) {
				Map<String, List<Requests>> map = gson.fromJson(param, type);
				
				System.out.println("map : " + map);
				
				for(int a=0;a<map.get("requests").size();a++) {
					System.out.println("00");
					System.out.println(map.get("requests").get(a).getOrder_id());
					if(orderRepository.idSearch(map.get("requests").get(a).getOrder_id())!=null){
						System.out.println(orderService.updateSign(map.get("requests").get(a).getOrder_id()));
						if(orderService.updateSign(map.get("requests").get(a).getOrder_id())<=0) {
							Result re = new Result();
//							System.out.println("11");
							re.setOrder_id(map.get("requests").get(a).getOrder_id());
							re.setDescription("입금확인오류 - 이미 확인처리된 order_id");
							list.add(re);
							cnt++;
						}else {
							Order o = orderRepository.idSearch(map.get("requests").get(a).getOrder_id());
							Member m = memberRepository.findByPhone(o.getBuyer_cellphone());
							if(memberService.pointCharge(m,o)>0) {
								
								Pointdetail pointdetail = new Pointdetail();
								pointdetail.setPointcontent("포인트 뱅크다 충전");
								pointdetail.setSign(true);
								pointdetail.setPoint(Integer.parseInt(o.getOrder_price_amount()));
								pointdetail.setUsername(m.getUsername());
								pointdetailService.save(pointdetail);
								
								
								String msg = o.getOrder_price_amount() + "원이 충전 되었습니다.";
								chargeSmsService.sendSMSAsync(msg, m.getPhone());
								
								
								System.out.println("충전 성공");
							}else {
								System.out.println("충전 실패");
							}
							
						}
					}else {
//						System.out.println("22");
						Result re = new Result();
						re.setOrder_id(map.get("requests").get(a).getOrder_id());
						re.setDescription("존재하지 않는 order_id");
						list.add(re);
						cnt++;
					}
					
				}
				System.out.println(map.get("order_id"));
				
				if(list.isEmpty()) {
					result.put("return_code", "200");
					result.put("description", "정상");
				}else if(!list.isEmpty() && cnt>0 && cnt<map.get("requests").size()){
					result.put("return_code", "200");
					result.put("description", "정상");
					result.put("orders", list);
				}else if(cnt==map.get("requests").size()){
					result.put("return_code", "415");
					result.put("description", "오류 order_id체크");
					result.put("orders", list);
				}
			}else {
				result.put("return_code", "400");
				result.put("description", "parameter 없음");
			}
		}catch(com.google.gson.JsonParseException e){
			System.out.println("Json Exception");
			result.put("return_code", "400");
			result.put("description", "JSON format 오류");
			return result;
		}catch(NullPointerException ex){
			System.out.println("NullPointer Exception");
			result.put("return_code", "400");
			result.put("description", "parameter 없음");
		}
		System.out.println("param : "+param);
		return result;
	}
}






















