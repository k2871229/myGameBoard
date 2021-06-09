package com.hrpp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.hrpp.domain.GameBoard;
import com.hrpp.persistence.CustomCrudRepository;
import com.hrpp.vo.PageMaker;
import com.hrpp.vo.PageVO;

import lombok.extern.java.Log;

@Controller
@RequestMapping("/boards/")
@Log
public class GameBoardController {

	@Autowired
	private CustomCrudRepository repo;
	
	@GetMapping("/list")
	public void list(@ModelAttribute("pageVO") PageVO vo, Model model) {
		Pageable page = vo.makePageable(0,"bno");
		Page<Object[]> result = 
				repo.getCustomPage(vo.getType(), vo.getKeyword(), page);
		model.addAttribute("result", new PageMaker(result));
	}
	
	@GetMapping("/register")
	public void registerGET(@ModelAttribute("vo") GameBoard vo) {
		log.info("registerGET....");
		vo.setTitle("샘플 게시물 제목입니다....");
		vo.setContent("샘플 게시물 내용입니다....");
		vo.setWriter("user00");
	}
	
	@PostMapping("/register")
	public String registerPOST(@ModelAttribute("vo")GameBoard vo,
			RedirectAttributes rttr) {
		log.info("registerPOST...");
		log.info(""+vo);
		repo.save(vo);
		rttr.addFlashAttribute("msg","success");
		return "redirect:/boards/list";
	}
	
	@GetMapping("/view")
	public void view(Long bno, 
		@ModelAttribute("pageVO")PageVO vo, Model model) {
		log.info("BNO :"+bno);
		repo.findById(bno).ifPresent(board->model.addAttribute("vo",board));
	}
	
	@Secured(value = {"ROLE_BASIC","ROLE_MANAGER","ROLE_ADMIN"})
	@GetMapping("/modify")
	public void modifyGET(Long bno, 
			@ModelAttribute("pageVO") PageVO vo, Model model) {
		log.info("MODIFY BNO :"+bno);
		repo.findById(bno).ifPresent(board->model.addAttribute("vo", board));
	}
	
	@PostMapping("/modify")
	public String modifyPOST(GameBoard board, PageVO vo, 
			RedirectAttributes rttr) {
		log.info("Modify WebBoard :"+board);
		repo.findById(board.getBno()).ifPresent(orgin->{
			orgin.setTitle(board.getTitle());
			orgin.setContent(board.getContent());
			repo.save(orgin);
			
			rttr.addFlashAttribute("msg","success");
			rttr.addAttribute("bno",orgin.getBno());
		});
		
		//페이징과 검색했던 결과로 이동하는 경우
		rttr.addAttribute("page",vo.getPage());
		rttr.addAttribute("size",vo.getSize());
		rttr.addAttribute("type",vo.getType());
		rttr.addAttribute("keyword",vo.getKeyword());
		return "redirect:/boards/view";
	}
	
	@PostMapping("/delete")
	public String deletePOST(Long bno,  PageVO vo, 
			RedirectAttributes rttr) {
		log.info("DELETE BNO :"+bno);
		repo.deleteById(bno);
		rttr.addFlashAttribute("msg","success");
		rttr.addAttribute("page",vo.getPage());
		rttr.addAttribute("size",vo.getSize());
		rttr.addAttribute("type",vo.getType());
		rttr.addAttribute("keyword",vo.getKeyword());
		return "redirect:/boards/list";
	}
}