package com.example.test.sbre.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.test.sbre.domain.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
					// 정렬추가 하고 싶거나 할 때 이렇게 생성하면 만들어짐
	List<Board> findAllByOrderByIdDesc();
					// findAll 다음에 오는게 필드 명임  (Title, Content 원하는 필드 명, 필드명 기준으로 정렬해줌 )



}
