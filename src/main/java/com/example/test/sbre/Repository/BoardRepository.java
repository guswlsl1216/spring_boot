package com.example.test.sbre.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.test.sbre.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Integer> {

}
