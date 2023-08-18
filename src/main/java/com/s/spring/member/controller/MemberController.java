package com.s.spring.member.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.s.spring.member.domain.Member;
import com.s.spring.member.service.MemberService;

@Controller
public class MemberController {
	
	@Autowired
	private MemberService service;

	@RequestMapping(value="/member/register.kh", method = RequestMethod.GET)
	public String showRegisterForm() {
		return "member/register";
	}
	
	@RequestMapping(value="/member/register.kh", method = RequestMethod.POST)
	public String registerMember(
//			@RequestParam("memberId") String memberId 대신에 @ModelAttribute 를 쓴다
//			RequestParam 여러개 안써도 됨, Member클래스에 있는 필드값과 jsp 인풋타입 name 값이 같으면 쓸 수있다/
			@ModelAttribute Member member
			, Model model) {
			try {
				//INSERT INTO MEMBER_TBL
				int result = service.insertMembere(member);
				if(result > 0) {
					//성공 로그인페이지
					//home.jsp가 로그인 할 수 있는 페이지가 되면 됨.
					return "redirect:/index.jsp";
				}else {
					//실패 에러페이지
					model.addAttribute("msg", "회원가입이 완료되지 않았습니다.");
					model.addAttribute("error", "회원가입 실패");
					model.addAttribute("url", "/member/register.kh");
					return "common/errorPage";
				}
			} catch (Exception e) {
				model.addAttribute("msg", "관리자에게 문의바람");
				model.addAttribute("error", e.getMessage());
				model.addAttribute("url", "/member/register.kh");
				return "common/errorPage";
			}
	}
	
	@RequestMapping(value="/member/login.kh", method = RequestMethod.POST)
	public String memberLoginCheck(
			@ModelAttribute Member member
			, HttpSession session
			, Model model ) {
		try {
			//Member로 받아도 됨.
//			int result = service.checkMemberLogin(member);
			Member mOne = service.checkMemberLogin(member);
			if(mOne != null) {
				//성공 - 메인페이지(세션에 아이디 이름 저장해주기)
				session.setAttribute("memberId", mOne.getMemberId());
				session.setAttribute("memberName", mOne.getMemberName());
				return "redirect:/index.jsp";
			} else {
				//실패 - 에러페이지
				model.addAttribute("msg", "로그인이 완료되지 않았습니다.");
				model.addAttribute("error", "로그인 실패");
				model.addAttribute("url", "/member/register.kh");
				return "common/errorPage";
			}
		} catch (Exception e) {
			model.addAttribute("msg", "관리자에게 문의바람");
			model.addAttribute("error", e.getMessage());
			model.addAttribute("url", "/member/register.kh");
			return "common/errorPage";
		}
	}
	
	
	@RequestMapping(value = "/member/logout.kh", method = RequestMethod.GET)
	public String memberLogout(
			HttpSession session
			, Model model) {
		if(session != null) {
			session.invalidate();
			return "redirect:/index.jsp";
		}else {
			model.addAttribute("msg","로그아웃 완료 실패");
			model.addAttribute("error","로그아웃 실패");
			model.addAttribute("url","/index.jsp");
			return "common/errorPage";
		}
	}
	
	
	@RequestMapping(value = "/member/mypage.kh", method = RequestMethod.GET)
	public String showMyPage(
			//쿼리스트링 받기 위해서 @RequestParam 사용
			@RequestParam("memberId") String memberId
			//Model에 키와 값으로 데이터를 넣어주면 jsp에서 꺼내서 사용가능
			, Model model) {
		try {
			Member member = service.getMemberById(memberId);
			if(member != null) {
				model.addAttribute("member", member);
				return "member/mypage";
			}else {
				model.addAttribute("msg", "마이페이지 실행 실패.");
				model.addAttribute("error", "마이페이지 실패");
				model.addAttribute("url", "/index.jsp");
				return "common/errorPage";
			}
		} catch (Exception e) {
			model.addAttribute("msg", "관리자에게 문의바람");
			model.addAttribute("error", e.getMessage());
			model.addAttribute("url", "/index.jsp");
			return "common/errorPage";
		}
	}
	
	
	
	@RequestMapping(value = "/member/update.kh", method = RequestMethod.GET)
	public String showModifyForm(
			String memberId
			,Model model) {
		Member member = service.getMemberById(memberId);
		model.addAttribute("member", member);
		return "member/modify";
	}

	
	@RequestMapping(value = "/member/update.kh", method = RequestMethod.POST)
	public String modifyMember(
			@ModelAttribute Member member
			, Model model) {
		try {
			int result = service.updateMember(member);
			if(result > 0) {
				return "redirect:/index.jsp";
			} else {
				model.addAttribute("msg", "회원정보수정 실행 실패.");
				model.addAttribute("error", "회원정보수정 실패");
				model.addAttribute("url", "/index.jsp");
				return "common/errorPage";
			}
		} catch (Exception e) {
			model.addAttribute("msg", "관리자에게 문의바람");
			model.addAttribute("error", e.getMessage());
			model.addAttribute("url", "/index.jsp");
			return "common/errorPage";
		}
	}
	
	
	
	@RequestMapping(value = "/member/delete.kh", method = RequestMethod.GET)
	public String outServiceMember(
			@RequestParam("memberId") String memberId
			, Model model) {
		try {
			int result = service.deleteMember(memberId);
			if(result > 0) {
				return "redirect:/member/logout.kh";
			} else {
				model.addAttribute("msg", "회원탈퇴 실행 실패.");
				model.addAttribute("error", "회원탈퇴 실패");
				model.addAttribute("url", "/index.jsp");
				return "common/errorPage";
			}
		} catch (Exception e) {
			model.addAttribute("msg", "관리자에게 문의바람");
			model.addAttribute("error", e.getMessage());
			model.addAttribute("url", "/index.jsp");
			return "common/errorPage";
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
}
