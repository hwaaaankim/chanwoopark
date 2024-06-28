package com.dev.Pt_CWP06.controller;


import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.EncoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dev.Pt_CWP06.jwt.JwtCreation;
import com.dev.Pt_CWP06.model.Board;
import com.dev.Pt_CWP06.model.Member;
import com.dev.Pt_CWP06.model.Notice;
import com.dev.Pt_CWP06.model.Pointdetail;
import com.dev.Pt_CWP06.model.Videodetail;
import com.dev.Pt_CWP06.model.bankda.Order;
import com.dev.Pt_CWP06.model.video.Video;
import com.dev.Pt_CWP06.repository.MemberRepository;
import com.dev.Pt_CWP06.repository.NoticeRepository;
import com.dev.Pt_CWP06.repository.OrderRepository;
import com.dev.Pt_CWP06.repository.PointdetailRepository;
import com.dev.Pt_CWP06.repository.VideoRepository;
import com.dev.Pt_CWP06.repository.VideodetailRepository;
import com.dev.Pt_CWP06.service.BoardService;
import com.dev.Pt_CWP06.service.MemberService;
import com.dev.Pt_CWP06.service.OrderService;
import com.dev.Pt_CWP06.service.PointdetailService;
import com.dev.Pt_CWP06.service.VideoService;
import com.dev.Pt_CWP06.service.VideodetailService;
import com.dev.Pt_CWP06.smsService.ChargeSmsService;

import lombok.extern.slf4j.Slf4j;

// 페이지 이동 담당
@Controller
@Slf4j
public class HomeController {
	
	@Autowired
	VideoRepository videoRepository;
	
	@Autowired
	VideoService videoService;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	VideodetailRepository videodetailRepository;
	
	@Autowired
	VideodetailService videodetailService;
	
	@Autowired
	PointdetailService pointdetailService;
	
	@Autowired
	PointdetailRepository pointdetailRepository;
	
	@Autowired
	BoardService boardService;
	
	@Autowired
	NoticeRepository noticeRepository;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	ChargeSmsService chargeSmsService;
	
	@GetMapping({"/","/home"})
	public String home(Model model,  
			@PageableDefault(size=3) @Qualifier("3") Pageable recentpageable, @PageableDefault(size=10) @Qualifier("ten") Pageable tenpageable,
			HttpServletRequest request , Authentication authentication) throws ParseException {
		log.info("홈화면 접속");
		Member member = memberService.findByUsername(authentication.getName());
		HttpSession session = request.getSession();
		session.setAttribute("userId", member.getId());
		session.setAttribute("username", member.getUsername());
		session.setAttribute("name", member.getName());
		session.setAttribute("phone", member.getPhone());
		
		Page<Videodetail> myVideo = videodetailRepository.findAllByUsernameOrderByVideodateDesc(tenpageable, authentication.getName());
		
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		String to = f.format(today);
		Date standard = f.parse(to);
		if(myVideo.getContent().size()>0) {
			for(int a=0;a<myVideo.getContent().size();a++) {
				String change = f.format(myVideo.getContent().get(a).getVideodate());
				myVideo.getContent().get(a).setDate(change);
				if((standard.getTime() - myVideo.getContent().get(a).getVideodate().getTime())/(60*60*1000)<72) {
					myVideo.getContent().get(a).setSign(true);
				}else {
					myVideo.getContent().get(a).setSign(false);
				}
				
			}
		}
		Page<Video> videos = videoRepository.findAllByOrderByClicksDesc(tenpageable);
		Page<Video> recent = videoRepository.findAllByOrderByIdDesc(tenpageable);
		Page<Video> topVideos = videoRepository.findAllByOrderByClicksDesc(recentpageable);
		model.addAttribute("myVideo", myVideo);
		model.addAttribute("videos", videos);
		model.addAttribute("recent", recent);
		model.addAttribute("topVideos", topVideos);
		
		return "front/home";
	}
	
	@GetMapping("/videoSecurity")
	public String videoSecurity(@PageableDefault(size=6) Pageable pageable, Model model) throws ParseException {
		log.info("핫 비디오 접근");
		Page<Video> videos = videoRepository.findAllByOrderByIdDesc(pageable);
 		int startPage = Math.max(1, videos.getPageable().getPageNumber()-3);
		int endPage = Math.min(videos.getTotalPages(), videos.getPageable().getPageNumber()+3); 
		model.addAttribute("startPage",startPage);
		model.addAttribute("endPage",endPage);
		model.addAttribute("videos",videos);
		return "front/video/video-security";
	}
	
	@GetMapping("/videoCheck")
	@ResponseBody
	public String videoCheck(Authentication authentication, Video video) throws ParseException {
//		System.out.println(video.getId());
		
		Calendar date = Calendar.getInstance();
		date.add(Calendar.DATE, -3);
		String before = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date.getTime());
		Date threeDay = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(before);
		if(videodetailRepository.record(authentication.getName(), video.getId(), threeDay)!=null) {
			return "P";
		}else {
			return "F";
		}
	}
	
	@GetMapping("/securityVideoDetail")
	public String securityVIdeoDetail(Video video, HttpServletRequest request, Model model, Videodetail videodetail,
			Authentication authentication, @PageableDefault(size=10) @Qualifier("ten") Pageable pageable) throws InvalidKeyException, NoSuchAlgorithmException, ParseException {
		log.info("Video 상세접근");
		HttpSession session = request.getSession();
		// 세션 존재 여부 파악
		if(session.getAttribute("userId")!=null) {
			String userId = session.getAttribute("userId").toString();
			Long id = Long.parseLong(userId);
			Member mem = memberRepository.findAllById(id);
			Video vi = videoRepository.findAllById(video.getId());
			Calendar date = Calendar.getInstance();
			date.add(Calendar.DATE, -3);
			String before = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date.getTime());
			Date threeDay = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(before);
			
			// videodetail에 3일 이내에 동일한 영상을 구매한 적 있는지 조회
			if(videodetailRepository.record(mem.getUsername(), vi.getId(), threeDay)==null) {
				if(mem.getPoint()>=vi.getPrice()) {
					
					// 영상의 가격보다 더 높은 포인트를 보유하는지 조회
					// 포인트 상세 내역에 입력
					Pointdetail pd = new Pointdetail();
					pd.setUsername(mem.getUsername());
					pd.setSign(false);
					pd.setPoint(vi.getPrice());
					pd.setPointcontent("영상 구매");
					pointdetailService.save(pd);
					
					// 비디오 상세내역에 조회수 및 첫 구매 내역 입력
					videodetailService.save(mem.getUsername(), vi.getId());
					videoRepository.updateSell(vi.getId());
					videoRepository.updateClicks(vi.getId());
					memberRepository.pointUse(mem.getId(), vi.getPrice());
					
					//웹토큰 생성하여 영상 로드
					JwtCreation jc = new JwtCreation();
					String jwt = jc.createToken(vi.getVideocode());
					String custom = "e8ef9888f25847876bc84146253c07ee932cff179beba5daacde044b24cc632a";
					String url = "https://v.kr.kollus.com/s?jwt=";
					String src = url+jwt+"&custom_key="+custom;
					Page<Video> recent = videoRepository.findAllByOrderByIdDesc(pageable);
					model.addAttribute("recent", recent);
					model.addAttribute("src", src);
					model.addAttribute("video",vi);
					return "front/video/movieDetail";
				}else {
					return "redirect:pointLack";
				}
			}else {
				Page<Video> recent = videoRepository.findAllByOrderByIdDesc(pageable);
				model.addAttribute("recent", recent);
				JwtCreation jc = new JwtCreation();
				videoRepository.updateClicks(vi.getId());
				String jwt = jc.createToken(vi.getVideocode());
				String custom = "e8ef9888f25847876bc84146253c07ee932cff179beba5daacde044b24cc632a";
				String url = "https://v.kr.kollus.com/s?jwt=";
				String src = url+jwt+"&custom_key="+custom;
				model.addAttribute("src", src);
				model.addAttribute("video",vi);
				return "front/video/movieDetail";
			}
		}else {
			
			// 세션 미 존재시 세션 종료 페이지로 redirect
			return "redirect:sessionInvalidate";
		}
	}
	
	@GetMapping("/sessionInvalidate")
	@ResponseBody
	public String sessionInvalidate() {
		String msg = "세션이 만료되었습니다. 다시 시도 해 주세요";
		StringBuilder sb = new StringBuilder();
		sb.append("alert('"+msg+"');");
		sb.append("location.href='/home'");
		sb.insert(0, "<script>");
		sb.append("</script>");
		
		return sb.toString();
	}
	
	@GetMapping("/pointLack")
	@ResponseBody
	public String pointLack() {
		String msg = "포인트가 부족합니다 충전 후 이용하세요";
		StringBuilder sb = new StringBuilder();
		sb.append("alert('"+msg+"');");
		sb.append("location.href='/home'");
		sb.insert(0, "<script>");
		sb.append("</script>");
		
		return sb.toString();
	}
	
	@GetMapping("/logout")
	@ResponseBody
	public String logout(){
		log.info("로그아웃발생");
		String msg = "로그아웃 되었습니다";
		StringBuilder sb = new StringBuilder();
		
		sb.append("alert('"+msg+"');");
		sb.insert(0, "<script>");
		sb.append("</script>");
		
		return sb.toString();
	}
	@GetMapping("/accessDenied")
	public String accessDenied() {
		log.info("제한된 접근");
		return "front/accessDenied";
	}
	
	@GetMapping("/charge")
	public String charge() {
		log.info("포인트충전");
		return "front/mypage/charge";
	}
	
	@GetMapping("/myVideo")
	public String myVideo(Authentication authentication, Model model, @PageableDefault(size=10) Pageable pageable) throws ParseException {
		log.info("myVideo");
		Member member = memberRepository.findByUsername(authentication.getName());
		Page<Videodetail> videoDetail = videodetailRepository.findAllByUsernameOrderByVideodateDesc(pageable, authentication.getName());
//		System.out.println(videoDetail.getContent().get(0).getVideo().getName());
//		Date date = videoDetail.getContent().get(0).getVideodate();
//		System.out.println(date);
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		String to = f.format(today);
		Date standard = f.parse(to);
		if(videoDetail.getContent().size()>0) {
			for(int a=0;a<videoDetail.getContent().size();a++) {
				String change = f.format(videoDetail.getContent().get(a).getVideodate());
				videoDetail.getContent().get(a).setDate(change);
				if((standard.getTime() - videoDetail.getContent().get(a).getVideodate().getTime())/(60*60*1000)<72) {
					videoDetail.getContent().get(a).setSign(true);
				}else {
					videoDetail.getContent().get(a).setSign(false);
				}
				
			}
		}
		DecimalFormat format = new DecimalFormat("###,###");
		member.setPointC(format.format(member.getPoint()));
		
		int startPage = Math.max(1, videoDetail.getPageable().getPageNumber()-4);
		int endPage = Math.min(videoDetail.getTotalPages(), videoDetail.getPageable().getPageNumber()+4); 
		model.addAttribute("startPage",startPage);
		model.addAttribute("endPage",endPage);
		
		model.addAttribute("detail", videoDetail);
		model.addAttribute("member", member);
		return "front/mypage/myVideo";
	}
	
	@GetMapping("/myPrivate")
	public String myPrivate(Authentication authentication, Model model, @PageableDefault(size=10) Pageable pageable) {
		log.info("myPrivate");
		
		Member member = memberRepository.findByUsername(authentication.getName());
		Page<Pointdetail> pages = pointdetailRepository.findAllByUsernameOrderByPointdateDesc(member.getUsername(), pageable);
		DecimalFormat format = new DecimalFormat("###,###");
		member.setPointC(format.format(member.getPoint()));
		if(pages.getContent().size()>0) {
			for(int a=0; a<pages.getContent().size();a++) {
				pages.getContent().get(a).setPointC(format.format(pages.getContent().get(a).getPoint()));
			}
		}
		int startPage = Math.max(1, pages.getPageable().getPageNumber()-4);
		int endPage = Math.min(pages.getTotalPages(), pages.getPageable().getPageNumber()+4); 
		model.addAttribute("startPage",startPage);
		model.addAttribute("endPage",endPage);
		model.addAttribute("page", pages);
		model.addAttribute("member", member);
		return "front/myPage/myPrivate";
	}
	
	@GetMapping("/board")
	public String board(HttpServletRequest request, Model model) {
		log.info("board");
		HttpSession session = request.getSession();
		if(session.getAttribute("username")!=null) {
			model.addAttribute("username", session.getAttribute("username"));
			model.addAttribute("name", session.getAttribute("name"));
			model.addAttribute("phone", session.getAttribute("phone"));
			return "front/community/board";
		}else {
			return "redirect:/sessionInvalidate"; 
		}
	}
	
	@GetMapping("/notice")
	public String notice(Model model, @PageableDefault(size=10) Pageable pageable) {
		log.info("notice");
		Page<Notice> notice = noticeRepository.findAllByOrderBySignDescIdDesc(pageable);
		int startPage = Math.max(1, notice.getPageable().getPageNumber()-4);
		int endPage = Math.min(notice.getTotalPages(), notice.getPageable().getPageNumber()+4); 
		model.addAttribute("startPage",startPage);
		model.addAttribute("endPage",endPage);
		model.addAttribute("notice", notice);
		return "front/community/notice";
	}
	
	@GetMapping("/faq")
	public String faq() {
		log.info("faq");
		return "front/community/faq";
	}
	
	@PostMapping("/boardInsert")
	@ResponseBody
	public String boardInsert(Board board) {
		StringBuffer sb = new StringBuffer();
		if(boardService.save(board)!=null) {
			String msg = "정상적으로 문의가 접수 되었습니다";
			sb.append("alert('"+msg+"');");
			sb.append("location.href='/board'");
			sb.insert(0, "<script>");
			sb.append("</script>");
		}else {
			String msg = "오류가 발생하였습니다. 다시 시도 해 주세요.";
			sb.append("alert('"+msg+"');");
			sb.append("location.href='/board'");
			sb.insert(0, "<script>");
			sb.append("</script>");
		}
		return sb.toString();
	}
	
	@GetMapping("/noticeClick")
	@ResponseBody
	public String noticeClick(Notice notice) {
		int cnt = noticeRepository.noticeClick(notice.getId());
		if(cnt>0) {
			return "P";
		}else {
			return "F";
		}
	}
	
	@GetMapping("/pointCharge")
	public String pointCharge() {
		
		return "front/myPage/pointCharge";
	}
	
	@GetMapping("/orderInsert")
	@ResponseBody
	public String orderInsert(Order order, Authentication authentication) throws EncoderException {
		Member m = memberRepository.findByUsername(authentication.getName());
		order.setBuyer_name(m.getName());
		order.setBuyer_email(m.getEmail());
		order.setBuyer_cellphone(m.getPhone());
		String item = order.getOrder_price_amount() + "원";
		order.setItem(item);
		order.setBilling_name(m.getName());
		order.setBank_account_no("3021057292491");
		order.setBank_code_name("농협");
		if(orderService.save(order)!=null) {
			StringBuffer sb = new StringBuffer();
			String msg = "정상적으로 주문이 접수 되었습니다";
			String message = "*입금자명 : " + m.getName() + ", 입금계좌 : 3021057292491, " + "예금주 : 박찬우, 입금은행 : 농협";
			chargeSmsService.sendSMSAsync(message, m.getPhone());
			
			sb.append("alert('"+msg+"');");
			sb.append("location.href='/pointCharge'");
			sb.insert(0, "<script>");
			sb.append("</script>");
			return sb.toString();
		}else {
			StringBuffer sb = new StringBuffer();
			String msg = "오류가 발생하였습니다. 다시 시도 해 주세요";
			sb.append("alert('"+msg+"');");
			sb.append("location.href='/pointCharge'");
			sb.insert(0, "<script>");
			sb.append("</script>");
			return sb.toString();
		}
		
		
	}
}




























