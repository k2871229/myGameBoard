package com.hrpp.persistence; 

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;


import com.hrpp.domain.GameBoard;
import com.hrpp.domain.QGameBoard;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

public interface GameBoardRepository 
extends CrudRepository<GameBoard, Long>,
QuerydslPredicateExecutor<GameBoard>{

	public default Predicate makePredicate(String type, String keyword) {
		BooleanBuilder builder = new BooleanBuilder();
		QGameBoard board = QGameBoard.gameBoard;
		
		//bno>0
		builder.and(board.bno.gt(0));
		
		if(type == null) {
			return builder;
		}
		
		//검색
		switch (type) {
		case "t":
			builder.and(board.title.like("%"+keyword+"%"));
			break;
		case "c":
			builder.and(board.content.like("%"+keyword+"%"));
			break;
		case "w":
			builder.and(board.writer.like("%"+keyword+"%"));
			break;
		}
		return builder;
	}
}