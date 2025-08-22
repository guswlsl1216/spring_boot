package com.example.test.sbre.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.test.sbre.domain.Board;
import com.example.test.sbre.service.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BoardController  {

	private final BoardService boardService;
	
	@PostMapping
	public ResponseEntity<?> insertBoard (@RequestBody Board board) {
		
		boardService.InsertBaord(board);
		
	return new ResponseEntity<>("게시글 등록 완료", HttpStatus.OK); 	
	}
	
	
}
 