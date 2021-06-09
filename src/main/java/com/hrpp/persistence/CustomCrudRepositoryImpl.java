package com.hrpp.persistence;

import java.util.ArrayList;
import java.util.List;

import com.querydsl.core.Tuple;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;


import com.hrpp.domain.GameBoard;
import com.hrpp.domain.QGameBoard;
import com.hrpp.domain.QReply;
import com.querydsl.jpa.JPQLQuery;

import lombok.extern.java.Log;

@Log
public class CustomCrudRepositoryImpl extends
QuerydslRepositorySupport implements CustomGameBoard{

	public CustomCrudRepositoryImpl() {
		super(GameBoard.class);
	}

	@Override
	public Page<Object[]> getCustomPage(String type, String keyword, 
			Pageable page) {
			log.info("====================================");
			log.info("Type :"+type);
			log.info("Keyword :"+keyword);
			log.info("Page :"+page);
			log.info("====================================");
			
			QGameBoard b = QGameBoard.gameBoard;
			QReply r = QReply.reply;
			
			JPQLQuery<GameBoard> query = from(b);
			JPQLQuery<Tuple> tuple =
					query.select(b.bno, b.title, r.count(), b.writer, b.regdate);
			tuple.leftJoin(r);
			tuple.on(b.bno.eq(r.board.bno));
			tuple.where(b.bno.gt(0L));
			if(type != null){
				
				switch (type.toLowerCase()) {
				case "t":
					tuple.where(b.title.like("%" + keyword +"%"));
					break;
				case "c":
					tuple.where(b.content.like("%" + keyword +"%"));
					break;
				case "w":
					tuple.where(b.writer.like("%" + keyword +"%"));
					break;				
				}
			}
			tuple.groupBy(b.bno);
			tuple.orderBy(b.bno.desc());
			tuple.offset(page.getOffset());
			tuple.limit(page.getPageSize());
			
			List<Tuple> list = tuple.fetch();
			List<Object[]> resultList = new ArrayList<Object[]>();
			list.forEach(t ->{
				resultList.add(t.toArray());
			});
			long total = tuple.fetchCount();
			
		return new PageImpl<>(resultList, page, total);
	}	
}
