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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dev.Pt_CWP06.model.Board;
import com.dev.Pt_CWP06.model.Notice;
import com.dev.Pt_CWP06.repository.BoardRepository;
import com.dev.Pt_CWP06.repository.NoticeRepository;
import com.dev.Pt_CWP06.service.BoardService;
import com.dev.Pt_CWP06.service.NoticeService;

@Controller
@RequestMapping("/community")
public class CommunityController {

	@Autowired
	BoardService boardService;
	
	@Autowired
	BoardRepository boardRepository;
	
	@Autowired
	NoticeRepository noticeRepository;
	
	@Autowired
	NoticeService noticeService;
	
	

	@GetMapping("/boardManager")
	public String boardManager(@PageableDefault(size=10) Pageable pageable, Model model) {
		Page<Board> boards = boardRepository.findAllByOrderByIdDesc(pageable);
		int startPage = Math.max(1, boards.getPageable().getPageNumber()-4);
		int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber()+4); 
		model.addAttribute("startPage",startPage);
		model.addAttribute("endPage",endPage);
		model.addAttribute("boards", boards);
		return "admin/board/boardManager";
	}
	
	
	@GetMapping("/boardDetail")
	public String boardDetail(Board board, Model model) {
		Board detail = boardRepository.findAllById(board.getId());
		if(!detail.getSign()) {
			boardRepository.updateSign(detail.getId());
			model.addAttribute("detail", detail);
			return "admin/board/boardDetail";
		}
		model.addAttribute("detail", detail);
		return "admin/board/boardDetail";
	}
	
	@PostMapping("/boardUpdate")
	public String boardUpdate(Board board, Model model) throws IllegalStateException, IOException {
//		System.out.println(board.getId());
//		System.out.println(board);
		boardService.boardUpdate(board);
		Board detail = boardRepository.findAllById(board.getId());
		model.addAttribute("detail", detail);
		model.addAttribute("UpCode", "S");
		return "admin/board/boardDetail";
	}
	
	@GetMapping("/boardDelete")
	@ResponseBody
	public String boardDelete(Board board) {
		int deleteCnt = boardRepository.deleteAllById(board.getId());
		if(deleteCnt>=1) {
			String msg = "삭제가 완료 되었습니다";
			StringBuilder sb = new StringBuilder();
			sb.append("alert('"+msg+"');");
			sb.append("location.href='/boardManager'");
			sb.insert(0, "<script>");
			sb.append("</script>");
			
			return sb.toString();
		}else {
			String msg = "삭제가 실패 하였습니다. 다시 시도하세요";
			StringBuilder sb = new StringBuilder();
			sb.append("alert('"+msg+"');");
			sb.append("location.href='/boardManager'");
			sb.insert(0, "<script>");
			sb.append("</script>");
			
			return sb.toString();
		}
	}
	
	@GetMapping("/noticeManager")
	public String noticeManager(Model model, @PageableDefault(size=10) Pageable pageable) {
		
		Page<Notice> notice = noticeRepository.findAllByOrderBySignDescIdDesc(pageable);
		
		int startPage = Math.max(1, notice.getPageable().getPageNumber()-4);
		int endPage = Math.min(notice.getTotalPages(), notice.getPageable().getPageNumber()+4); 
		model.addAttribute("startPage",startPage);
		model.addAttribute("endPage",endPage);
		model.addAttribute("notice", notice);
		return "admin/notice/noticeManager";
	}
	
	@GetMapping("/noticeRegisterF")
	public String noticeRegisterF() {
		return "admin/notice/noticeRegisterF";
	}
	
	@GetMapping("/noticeDetail")
	public String noticeDetail(Model model, Notice notice) {
		
		model.addAttribute("detail", noticeRepository.findAllById(notice.getId()));
		
		return "admin/notice/noticeDetail";
	}
	
	@PostMapping("/noticeUpdate")
	@ResponseBody
	public String noticeUpdate(Notice notice) {
		
		noticeService.noticeUpdate(notice);
		String msg = "수정 되었습니다";
		StringBuilder sb = new StringBuilder();
		sb.append("alert('"+msg+"');");
		sb.append("self.close();");
		sb.append("opener.location.reload();");
		sb.insert(0, "<script>");
		sb.append("</script>");
		
		return sb.toString();
	}
	
	@GetMapping("/noticeDelete")
	@ResponseBody
	public String noticeDelete(Notice notice) {
		int deleteCnt = noticeRepository.deleteAllById(notice.getId());
		if(deleteCnt>=1) {
			String msg = "삭제가 완료 되었습니다";
			StringBuilder sb = new StringBuilder();
			sb.append("alert('"+msg+"');");
			sb.append("location.href='/community/noticeManager'");
			sb.insert(0, "<script>");
			sb.append("</script>");
			
			return sb.toString();
		}else {
			String msg = "삭제가 실패 하였습니다. 다시 시도하세요";
			StringBuilder sb = new StringBuilder();
			sb.append("alert('"+msg+"');");
			sb.append("location.href='/community/noticeManager'");
			sb.insert(0, "<script>");
			sb.append("</script>");
			
			return sb.toString();
		}
	}
	
	@PostMapping("/noticeRegister")
	public String noticeRegister(Notice notice, Model model) {
		
		if(noticeService.save(notice)!=null) {
			model.addAttribute("RgCode","S");
			return "admin/notice/noticeRegisterF";
		}else {
			model.addAttribute("RgCode","F");
			return "admin/notice/noticeRegisterF";
		}
		
	}
	
}
































