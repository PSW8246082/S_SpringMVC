package com.s.spring.board.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.s.spring.board.domain.Board;
import com.s.spring.board.domain.Reply;
import com.s.spring.board.service.ReplyService;

@Controller
@RequestMapping("/reply")
public class ReplyController {
	
	@Autowired
	private ReplyService rService;

	@RequestMapping(value = "/add.kh", method = RequestMethod.POST)
	public ModelAndView insertReply(
			ModelAndView mv
			, @ModelAttribute Reply reply
			, HttpSession session) {
		
		String url = "";
		
		try {
			String replyWriter = (String)session.getAttribute("memberId");
			reply.setReplyWriter(replyWriter);
			int result = rService.insertReply(reply);
			url = "/board/detail.kh?boardNo="+ reply.getRefBoardNo();
			
			if(result>0) {
				mv.setViewName("redirect:" + url);
			} else {
				mv.addObject("msg", "댓글등록 조회 실패");
				mv.addObject("error", "댓글등록 조회 실패");
				mv.addObject("url", url);
				mv.setViewName("common/errorPage");
			}
		} catch (Exception e) {
			mv.addObject("msg", "관리자에게 문의 바랍니다");
			mv.addObject("error", e.getMessage());
			mv.addObject("url", url);
			mv.setViewName("common/errorPage");
		}
		return mv;
	}
	
	
	
	@RequestMapping(value = "/update.kh", method = RequestMethod.POST)
	public ModelAndView updateReply(
			ModelAndView mv
			,@ModelAttribute Reply reply
			, HttpSession session) {
		
		String url = "";
		
		try {
			String replyWriter = (String)session.getAttribute("memberId");
			if(replyWriter != null && !replyWriter.equals("")) {
				reply.setReplyWriter(replyWriter);
				url = "/board/detail.kh?boardNo="+ reply.getRefBoardNo();
				int result = rService.updateReply(reply);
				mv.setViewName("redirect:" + url);
			} else {
				mv.addObject("msg", "댓글수정 실패");
				mv.addObject("error", "댓글수정 실패");
				mv.addObject("url", "/index.jsp");
				mv.setViewName("common/errorPage");
			}
		} catch (Exception e) {
			mv.addObject("msg", "관리자에게 문의 바랍니다");
			mv.addObject("error", e.getMessage());
			mv.addObject("url", url);
			mv.setViewName("common/errorPage");
		}
		return mv;
	}
	
	
	
	
	//댓글삭제
	@RequestMapping(value = "/delete.kh", method = RequestMethod.GET)
	public ModelAndView deleteReply(
			ModelAndView mv
			, @ModelAttribute Reply reply
//			, @RequestParam("replyNo") Integer replyNo  //Integer null체크 가능함
//			, @RequestParam("replyWriter") String replyWriter
			, HttpSession session) {  //세션에서 아이디 가져오기 1
		
		String url = "";
		
		try {
			String memberId = (String)session.getAttribute("memberId"); //세션에서 아이디 가져오기 2
			String replyWriter = reply.getReplyWriter();
			url = "/board/detail.kh?boardNo=" + reply.getRefBoardNo();
			
			if(replyWriter != null && replyWriter.equals(memberId)) {
//				Reply reply = new Reply();
//				reply.setReplyNo(replyNo);
//				reply.setReplyWriter(replyWriter);
				
				int result = rService.deleteReply(reply);
				
				if(result > 0) {
					//성공
					mv.setViewName("redirect:" + url);
				} else {
					mv.addObject("msg", "댓글삭제 실패");
					mv.addObject("error", "댓글삭제 실패");
					mv.addObject("url", url);
					mv.setViewName("common/errorPage");
				}
			} else {
				mv.addObject("msg", "자신의 댓글만 삭제할 수 있습니다.");
				mv.addObject("error", "댓글삭제 불가");
				mv.addObject("url", url);
				mv.setViewName("common/errorPage");
			}
			
			
		} catch (Exception e) {
			mv.addObject("msg", "관리자에게 문의 바랍니다");
			mv.addObject("error", e.getMessage());
//			mv.addObject("url", "board/detail.kh?boardNo=");
			mv.addObject("url", url);
			mv.setViewName("common/errorPage");
		}
 		
		return mv;
	}
	
	
	
	
	
	
	
}
