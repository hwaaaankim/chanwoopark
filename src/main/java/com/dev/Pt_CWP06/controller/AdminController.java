package com.dev.Pt_CWP06.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dev.Pt_CWP06.model.Member;
import com.dev.Pt_CWP06.model.bankda.Order;
import com.dev.Pt_CWP06.model.video.Video;
import com.dev.Pt_CWP06.repository.BoardRepository;
import com.dev.Pt_CWP06.repository.MemberRepository;
import com.dev.Pt_CWP06.repository.OrderRepository;
import com.dev.Pt_CWP06.repository.VideoRepository;
import com.dev.Pt_CWP06.service.BoardService;
import com.dev.Pt_CWP06.service.MemberService;
import com.dev.Pt_CWP06.service.VideoService;
import com.dev.Pt_CWP06.smsService.IdSMSService;
import com.dev.Pt_CWP06.smsService.PwSMSService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class AdminController {
	
	@Autowired
	VideoService videoService;
	
	@Autowired
	VideoRepository videoRepository;
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	IdSMSService idsmsService;
	
	@Autowired
	PwSMSService pwsmsService;
	
	@Autowired
	BoardRepository boardRepository;
	
	@Autowired
	BoardService boardService;
	
	@Autowired
	OrderRepository orderRepository;
	
	@GetMapping("/admin")
	public String admin() {
		log.info("admin 접속");
		return "admin/index";
	}
	
	@GetMapping("/videoManager")
	public String videoManager(Model model, @PageableDefault(size=10) Pageable pageable) {
		Page<Video> videos = videoRepository.findAllByOrderByIdDesc(pageable);
		int startPage = Math.max(1, videos.getPageable().getPageNumber()-4);
		int endPage = Math.min(videos.getTotalPages(), videos.getPageable().getPageNumber()+4); 
		model.addAttribute("startPage",startPage);
		model.addAttribute("endPage",endPage);
		model.addAttribute("videos",videos);
		return "admin/video/videoManager";
	}
	
	@GetMapping("/videoRegisterF")
	public String videoRegisterF() {
		return "admin/video/videoRegisterF";
	}
	
	@GetMapping("/videoDetail")
	public String videoDetail(Video video, Model model) {
		Video detail = videoRepository.findAllById(video.getId());
		model.addAttribute("detail", detail);
		return "admin/video/videoDetail";
	}
	
	@PostMapping("/videoUpdate")
	public String videoUpdate(Video video, Model model) throws IllegalStateException, IOException {
//		System.out.println(video.getId());
//		System.out.println(video);
		videoService.videoUpdate(video);
		Video detail = videoRepository.findAllById(video.getId());
		model.addAttribute("detail", detail);
		model.addAttribute("UpCode", "S");
		return "admin/video/videoDetail";
	}
	
	@GetMapping("/videoDelete")
	@ResponseBody
	public String videoDelete(Video video) {
		int deleteCnt = videoRepository.deleteAllById(video.getId());
		if(deleteCnt>=1) {
			String msg = "삭제가 완료 되었습니다";
			StringBuilder sb = new StringBuilder();
			sb.append("alert('"+msg+"');");
			sb.append("location.href='/videoManager'");
			sb.insert(0, "<script>");
			sb.append("</script>");
			
			return sb.toString();
		}else {
			String msg = "삭제가 실패 하였습니다. 다시 시도하세요";
			StringBuilder sb = new StringBuilder();
			sb.append("alert('"+msg+"');");
			sb.append("location.href='/videoManager'");
			sb.insert(0, "<script>");
			sb.append("</script>");
			
			return sb.toString();
		}
	}
	
	@PostMapping("/videoRegister")
	public String videoRegister(Video video, Model model) throws IllegalStateException, IOException {
		if(videoService.save(video)!=null) {
			model.addAttribute("RgCode","S");
			return "admin/video/videoRegisterF";
		}else {
			model.addAttribute("RgCode","F");
			return "admin/video/videoRegisterF";
		}
	}
	
	@GetMapping("/memberManager")
	public String memberManager(Model model, @PageableDefault(size=10) Pageable pageable, @RequestParam(required=false, defaultValue="") String contentSearch, @RequestParam(required=false, defaultValue="none") String searchCondition) {
		if("none".equals(searchCondition)) {
			
			Page<Member> members = memberRepository.findAllByOrderByJoindateAsc(pageable);
			int startPage = Math.max(1, members.getPageable().getPageNumber()-4);
			int endPage = Math.min(members.getTotalPages(), members.getPageable().getPageNumber()+4); 
			model.addAttribute("startPage",startPage);
			model.addAttribute("endPage",endPage);
			model.addAttribute("members",members);
			model.addAttribute("searchCondition", "none");
			
		}else if("name".equals(searchCondition)) {
			Page<Member> members = memberRepository.findAllByNameContainingOrderByJoindateAsc(contentSearch, pageable);
			int startPage = Math.max(1, members.getPageable().getPageNumber()-4);
			int endPage = Math.min(members.getTotalPages(), members.getPageable().getPageNumber()+4); 
			model.addAttribute("startPage",startPage);
			model.addAttribute("endPage",endPage);
			model.addAttribute("members",members);
			model.addAttribute("searchCondition", "name");
			
		}else if("phone".equals(searchCondition)) {
			Page<Member> members = memberRepository.findAllByPhoneContainingOrderByJoindateAsc(contentSearch, pageable);
			int startPage = Math.max(1, members.getPageable().getPageNumber()-4);
			int endPage = Math.min(members.getTotalPages(), members.getPageable().getPageNumber()+4); 
			model.addAttribute("startPage",startPage);
			model.addAttribute("endPage",endPage);
			model.addAttribute("members",members);
			model.addAttribute("searchCondition", "phone");
			
		}else if("username".equals(searchCondition)) {
			
			Page<Member> members = memberRepository.findAllByUsernameContainingOrderByJoindateAsc(contentSearch, pageable);
			int startPage = Math.max(1, members.getPageable().getPageNumber()-4);
			int endPage = Math.min(members.getTotalPages(), members.getPageable().getPageNumber()+4); 
			model.addAttribute("startPage",startPage);
			model.addAttribute("endPage",endPage);
			model.addAttribute("members",members);
			model.addAttribute("searchCondition", "username");
			
		}else if("nickname".equals(searchCondition)) {
			Page<Member> members = memberRepository.findAllByNicknameContainingOrderByJoindateAsc(contentSearch, pageable);
			int startPage = Math.max(1, members.getPageable().getPageNumber()-4);
			int endPage = Math.min(members.getTotalPages(), members.getPageable().getPageNumber()+4); 
			model.addAttribute("startPage",startPage);
			model.addAttribute("endPage",endPage);
			model.addAttribute("members",members);
			model.addAttribute("searchCondition", "nickname");
		}
		
		return "admin/member/memberManager";
	}
	
	@GetMapping("/memberDetail")
	public String memberDetail(Member member, Model model) {
		Member detail = memberRepository.findAllById(member.getId());
		model.addAttribute("detail", detail);
		return "admin/member/memberDetail";
	}
	
	@PostMapping("/memberUpdate")
	public String memberUpdate(Member member, Model model) throws IllegalStateException, IOException {
//		System.out.println(member.getId());
//		System.out.println(member);
		memberService.memberUpdate(member);
		Member detail = memberRepository.findAllById(member.getId());
		model.addAttribute("detail", detail);
		model.addAttribute("UpCode", "S");
		return "admin/member/memberDetail";
	}
	
	@GetMapping("/memberDelete")
	@ResponseBody
	public String memberDelete(Member member) {
		int deleteCnt = memberRepository.deleteAllById(member.getId());
		if(deleteCnt>=1) {
			String msg = "삭제가 완료 되었습니다";
			StringBuilder sb = new StringBuilder();
			sb.append("alert('"+msg+"');");
			sb.append("location.href='/memberManager'");
			sb.insert(0, "<script>");
			sb.append("</script>");
			
			return sb.toString();
		}else {
			String msg = "삭제가 실패 하였습니다. 다시 시도하세요";
			StringBuilder sb = new StringBuilder();
			sb.append("alert('"+msg+"');");
			sb.append("location.href='/memberManager'");
			sb.insert(0, "<script>");
			sb.append("</script>");
			
			return sb.toString();
		}
	}
	
	@PostMapping("/memberRegister")
	public String memberRegister(Member member, Model model) throws IllegalStateException, IOException {
		if(memberService.save(member)!=null) {
			model.addAttribute("RgCode","S");
			return "admin/member/memberRegisterF";
		}else {
			model.addAttribute("RgCode","F");
			return "admin/member/memberRegisterF";
		}
	}
	
	@GetMapping("/orderManager")
	public String orderManager(Model model, @PageableDefault(size=10) Pageable pageable, @RequestParam(required=false, defaultValue="") String contentSearch, @RequestParam(required=false, defaultValue="none") String searchCondition) {
		if("none".equals(searchCondition)) {
			
			Page<Order> order = orderRepository.findAllByOrderByIdDesc(pageable);
			int startPage = Math.max(1, order.getPageable().getPageNumber()-4);
			int endPage = Math.min(order.getTotalPages(), order.getPageable().getPageNumber()+4); 
			model.addAttribute("startPage",startPage);
			model.addAttribute("endPage",endPage);
			model.addAttribute("order",order);
			model.addAttribute("searchCondition", "none");
			
		}else if("name".equals(searchCondition)) {
			Page<Order> order = orderRepository.findByNameQuery(contentSearch, pageable);
			int startPage = Math.max(1, order.getPageable().getPageNumber()-4);
			int endPage = Math.min(order.getTotalPages(), order.getPageable().getPageNumber()+4); 
			model.addAttribute("startPage",startPage);
			model.addAttribute("endPage",endPage);
			model.addAttribute("order",order);
			model.addAttribute("searchCondition", "name");
			
		}else if("phone".equals(searchCondition)) {
			Page<Order> order = orderRepository.findByPhoneQuery(contentSearch, pageable);
			int startPage = Math.max(1, order.getPageable().getPageNumber()-4);
			int endPage = Math.min(order.getTotalPages(), order.getPageable().getPageNumber()+4); 
			model.addAttribute("startPage",startPage);
			model.addAttribute("endPage",endPage);
			model.addAttribute("order",order);
			model.addAttribute("searchCondition", "phone");
			
		}else if("email".equals(searchCondition)) {
			
			Page<Order> order = orderRepository.findByEmailQuery(contentSearch, pageable);
			int startPage = Math.max(1, order.getPageable().getPageNumber()-4);
			int endPage = Math.min(order.getTotalPages(), order.getPageable().getPageNumber()+4); 
			model.addAttribute("startPage",startPage);
			model.addAttribute("endPage",endPage);
			model.addAttribute("order",order);
			model.addAttribute("searchCondition", "email");
			
		}else if("sign".equals(searchCondition)) {
			Boolean sign = true;
			if("false".equals(contentSearch)) {
				sign=false;
			}
			
			Page<Order> order = orderRepository.findBySignQuery(sign, pageable);
			int startPage = Math.max(1, order.getPageable().getPageNumber()-4);
			int endPage = Math.min(order.getTotalPages(), order.getPageable().getPageNumber()+4); 
			model.addAttribute("startPage",startPage);
			model.addAttribute("endPage",endPage);
			model.addAttribute("order",order);
			model.addAttribute("searchCondition", "sign");
		}
		
		return "admin/order/orderManager";
	}
	
	
	@GetMapping("/orderDelete")
	@ResponseBody
	public String orderDelete(Order order) {
		int deleteCnt = orderRepository.deleteAllById(order.getId());
		if(deleteCnt>=1) {
			String msg = "삭제가 완료 되었습니다";
			StringBuilder sb = new StringBuilder();
			sb.append("alert('"+msg+"');");
			sb.append("location.href='/orderManager'");
			sb.insert(0, "<script>");
			sb.append("</script>");
			
			return sb.toString();
		}else {
			String msg = "삭제가 실패 하였습니다. 다시 시도하세요";
			StringBuilder sb = new StringBuilder();
			sb.append("alert('"+msg+"');");
			sb.append("location.href='/orderManager'");
			sb.insert(0, "<script>");
			sb.append("</script>");
			
			return sb.toString();
		}
	}
	
}













