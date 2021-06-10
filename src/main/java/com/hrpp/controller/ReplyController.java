package com.hrpp.controller;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hrpp.domain.GameBoard;
import com.hrpp.domain.Reply;
import com.hrpp.persistence.ReplyRepository;

import lombok.extern.java.Log;

@RestController
@RequestMapping("/replies")
@Log
public class ReplyController {
	
	@Autowired
	private ReplyRepository repo;
	
	//댓글 추가
	@PostMapping("/{bno}")
	public ResponseEntity<List<Reply>> addReply(
			@PathVariable("bno") Long bno,
			@RequestBody Reply reply){
		log.info("addReply..........................");
		log.info("bno :" +bno);
		log.info("reply :" +reply);
		
		GameBoard board = new GameBoard();
		board.setBno(bno);
		reply.setBoard(board);
		repo.save(reply);//댓글 저장
		
		return new ResponseEntity<>(getListByBoard(board),
				HttpStatus.CREATED);
	}
	
	//댓글 리스트
	private List<Reply> getListByBoard(GameBoard board)
	throws RuntimeException{
		log.info("getListByBoard....." +board);
		return repo.getRepliesOfBoard(board);
	}
	
	//댓글 삭제
	@Transactional
	@DeleteMapping("/{bno}/{rno}")
	public ResponseEntity<List<Reply>> remove(
			@PathVariable("bno")Long bno,
			@PathVariable("rno")Long rno
			){
		log.info("delete reply :"+rno);
		repo.deleteById(rno);
		GameBoard board = new GameBoard();
		board.setBno(bno);
		
		return new ResponseEntity<>(getListByBoard(board),
				HttpStatus.OK);
	}
	
	//댓글 수정 modify
	@Transactional
	@PutMapping("/{bno}")
	public ResponseEntity<List<Reply>> modify(
			@PathVariable("bno") Long bno,
			@RequestBody Reply reply
			){
		log.info("modify reply :"+ reply);
		// 만약 조건에 만족하는 댓글 레코드가 있다면
		repo.findById(reply.getRno()).ifPresent(orgin ->{
			orgin.setReplyText(reply.getReplyText());
			orgin.setReplyer(reply.getReplyer());
			repo.save(orgin);
		});
		
		GameBoard board = new GameBoard();
		board.setBno(bno);
		
		return new ResponseEntity<>(getListByBoard(board),
				HttpStatus.OK);
	}
	
	@GetMapping("/{bno}")
	public ResponseEntity<List<Reply>> getReplies(
			@PathVariable("bno") Long bno){
		log.info("get all Replies...........");
		GameBoard board = new GameBoard();
		board.setBno(bno);
		return new ResponseEntity<>(getListByBoard(board),
				HttpStatus.OK);
	}
}






 
