package com.example.test.sbre.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.test.sbre.Repository.BoardRepository;
import com.example.test.sbre.domain.Board;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class BoardService {
	private final BoardRepository boardRepository;
	
	
	public void InsertBoard (Board board) {
		
		boardRepository.save(board);
	}

	//boarderRepository에서 만든거 호출! 
	public List<Board> getBoardList() {
		return boardRepository.findAllByOrderByIdDesc();
	}
	
							// board 엔티티 id의 자료형 // 매개변수  
	public Board getBoard(Integer id) {
	
		return boardRepository.findById(id).get() // 바로 가져오게 하는거 
;	
	}
	// id를 받아서 삭제하는
	public void deleteBoard(Integer id) {
		boardRepository.deleteById(id);
		
	}
}
