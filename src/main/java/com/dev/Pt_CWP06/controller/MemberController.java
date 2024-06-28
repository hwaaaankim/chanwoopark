package com.dev.Pt_CWP06.controller;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dev.Pt_CWP06.model.Member;
import com.dev.Pt_CWP06.model.Pointdetail;
import com.dev.Pt_CWP06.model.certification.CertConfig;
import com.dev.Pt_CWP06.repository.MemberRepository;
import com.dev.Pt_CWP06.repository.VideodetailRepository;
import com.dev.Pt_CWP06.service.MemberService;
import com.dev.Pt_CWP06.service.PointdetailService;

import lombok.extern.slf4j.Slf4j;

// 이용자의 회원가입, 로그인, 회원정보수정, 회원정보조회 
@Controller
@RequestMapping("/member")
@Slf4j
public class MemberController {

	@Autowired
	MemberService memberService;
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	VideodetailRepository videodetailRepository;
	
	@Autowired
	PointdetailService pointdetailService;
	
	@GetMapping("/login")
	public String loginProc(Model model, CertConfig cf) {
		log.info("로그인 프로세스");
		
		String ordr_idxx = "PARK" + (new SimpleDateFormat("yyyyMMddHHmmssSSSSSSS").format(new Date()));
		model.addAttribute("cf", cf);
		model.addAttribute("ordr_idxx",ordr_idxx);
		
		return "front/login/login";
	}
	
	@GetMapping("/dupLogin")
	@ResponseBody
	public String dupLogin() {
		String msg = "다른 장소에서 로그인이 발생하여 로그아웃 되었습니다. 다시 로그인 해 주세요";
		StringBuilder sb = new StringBuilder();
		sb.append("alert('"+msg+"');");
		sb.append("location.href='/login'");
		sb.insert(0, "<script>");
		sb.append("</script>");
		
		return sb.toString();
	}
	
	@GetMapping("/register")
	public String registerF(HttpServletRequest request, Model model, CertConfig cf) {
		log.info("회원가입 접근");
		HttpSession session = request.getSession();
		if(session.getAttribute("birth")!=null) {
			return "front/login/register";
		}else {
			String ordr_idxx = "PARK" + (new SimpleDateFormat("yyyyMMddHHmmssSSSSSSS").format(new Date()));
			model.addAttribute("cf",cf);
			model.addAttribute("ordr_idxx",ordr_idxx);
			model.addAttribute("msg","성인인증을 완료한 사용자만 회원가입이 가능합니다");
			return "front/login/login";
		}
	}
	
	@PostMapping("/register")
	@ResponseBody
	public String register(Member member) {
		if(memberService.save(member)!=null) {
			String msg = "회원 가입이 완료 되었습니다";
			StringBuilder sb = new StringBuilder();
			sb.append("alert('"+msg+"');");
			sb.append("location.href='/home'");
			sb.insert(0, "<script>");
			sb.append("</script>");
			
			return sb.toString();
		}else {
			String msg = "회원 가입에 실패 하였습니다. 다시 시도 해 주세요";
			StringBuilder sb = new StringBuilder();
			sb.append("alert('"+msg+"');");
			sb.append("location.href='/home'");
			sb.insert(0, "<script>");
			sb.append("</script>");
			
			return sb.toString();
		}
	}
	
	@PostMapping("/idCheck")
	@ResponseBody
	public String idCheck(Member member, Model model) {
		if(memberRepository.findByUsername(member.getUsername())!=null) {
			return "F";
		}else {
			return "P";
		}
	}
	
	@PostMapping("/nickCheck")
	@ResponseBody
	public String nickCheck(Member member, Model model) {
		if(memberRepository.findByNickname(member.getNickname())!=null) {
			return "F";
		}else {
			return "P";
		}
	}
	
	@PostMapping("/pointCharge")
	@ResponseBody
	public String pointCharge(Member member, Model model, String symbol) {
		if("plus".equals(symbol)) {
			if(memberRepository.pointCharge(member.getId(), member.getPoint())>0) {
				Pointdetail pointdetail = new Pointdetail();
				pointdetail.setPointcontent("포인트 충전");
				pointdetail.setSign(true);
				pointdetail.setPoint(member.getPoint());
				pointdetail.setUsername(member.getUsername());
				pointdetailService.save(pointdetail);
				return "P";
			}else {
				return "F";
			}
		}else {
			if(memberRepository.pointMinus(member.getId(), member.getPoint())>0) {
				Pointdetail pointdetail = new Pointdetail();
				pointdetail.setPointcontent("포인트 관리자 차감");
				pointdetail.setSign(false);
				pointdetail.setPoint(member.getPoint());
				pointdetail.setUsername(member.getUsername());
				pointdetailService.save(pointdetail);
				return "P";
			}else {
				return "F";
			}
		}
		
	}
	
	@GetMapping("/infoUpdateF")
	public String infoUpdate(Authentication authentication, Model model) {
		Member member = memberRepository.findByUsername(authentication.getName());
		model.addAttribute("member", member);
		return "front/mypage/infoUpdate";
	}
	
	@PostMapping("/infoUpdate")
	@ResponseBody
	public String memberUpdate(Member member, Model model) throws IllegalStateException, IOException {
//		System.out.println(member.getId());
//		System.out.println(member);
		memberService.infoUpdate(member);
		
		String msg = "변경이 완료 되었습니다";
		StringBuilder sb = new StringBuilder();
		sb.append("alert('"+msg+"');");
		sb.append("location.href='/myPrivate'");
		sb.insert(0, "<script>");
		sb.append("</script>");
		
		return sb.toString();
	}
	
	@GetMapping("/withdraw")
	@ResponseBody
	public String withdraw(Authentication authentication, HttpServletRequest request) {
		Member member = memberRepository.findByUsername(authentication.getName());
		memberService.withdraw(member);
		StringBuffer sb = new StringBuffer();
		HttpSession session = request.getSession();
		session.invalidate();
		authentication.setAuthenticated(false);
		String msg = "정상적으로 비활성화 되었습니다. 재사용을 위해서는 전화문의를 이용 바랍니다.";
		sb.append("alert('"+msg+"');");
		sb.append("location.href='/logout'");
		sb.insert(0, "<script>");
		sb.append("</script>");
		
		return sb.toString();
	}
	
}































