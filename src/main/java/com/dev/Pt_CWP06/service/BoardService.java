package com.dev.Pt_CWP06.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev.Pt_CWP06.model.Board;
import com.dev.Pt_CWP06.repository.BoardRepository;

@Service
public class BoardService {

	@Autowired
	BoardRepository boardRepository;
	
	public Board save(Board board) {
		Date today = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = format.format(today);
		board.setIndate(now);
		board.setSign(false);
		return boardRepository.save(board);
	}
	
	public void boardUpdate(Board board) {
		Optional<Board> bo = boardRepository.findById(board.getId());
		bo.ifPresent(selectBoard ->{
			selectBoard.setMessage(board.getMessage());
			boardRepository.save(selectBoard);
		});
	}
}
