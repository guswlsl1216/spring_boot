package com.example.test.sbre.service;

import org.springframework.stereotype.Service;

import com.example.test.sbre.Repository.BoardRepository;
import com.example.test.sbre.domain.Board;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class BoardService {
	private final BoardRepository boardRepository;
	
	
	public void InsertBaord (Board board) {
		
		boardRepository.save(board);
	}
}
