package com.example.test.sbre.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.test.sbre.domain.Board;
import com.example.test.sbre.service.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BoardController  {

	private final BoardService boardService;
	
	@PostMapping("/board")
	public ResponseEntity<?> insertBoard (@RequestBody Board board) {
		
		boardService.InsertBoard(board);
		
	return new ResponseEntity<>("게시글 등록 완료", HttpStatus.OK); 	
	}
	
	//서비스에서 만든 리스트 목록 보여주는거 응답처리 ! 
	@GetMapping("/board")
	public ResponseEntity<?> getBoardList () {
		List<Board> boardList = boardService.getBoardList();
		
		return new ResponseEntity<>(boardList, HttpStatus.OK);
		
	}
	
	// 상세페이지 들어가게 
	
	@GetMapping("/board/{id}")
	public ResponseEntity<?> getBoard(@PathVariable Integer id) {
		Board board = boardService.getBoard(id);
		return new ResponseEntity<>(board, HttpStatus.OK);
	}
	
	@DeleteMapping("/board")				// 안넣어도 값은 잘 들어오지만 변수 명이 같아야 함! 
	public ResponseEntity<?> deleteBoard (Integer id) {
		
		boardService.deleteBoard(id);
		
		return new ResponseEntity<>(id + "번 게시글 삭제 완료", HttpStatus.OK);
	}
	
	
}
 