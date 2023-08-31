package com.s.spring.board.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.s.spring.board.domain.Board;
import com.s.spring.board.domain.PageInfo;
import com.s.spring.board.domain.Reply;
import com.s.spring.board.service.BoardService;
import com.s.spring.board.service.ReplyService;

@Controller
public class BoardController {
	
	@Autowired
	private BoardService bService;
	
	@Autowired
	private ReplyService rService;
	
	
	@RequestMapping(value = "/board/detail.kh", method = RequestMethod.GET)
	public ModelAndView showBoardDetail(
			ModelAndView mv
			, @RequestParam("boardNo") Integer boardNo) {
		
		try {
			Board boardOne = bService.selectBoardByNo(boardNo);
			
			if(boardOne != null) {
				List<Reply> rList = rService.selectReplyList(boardNo);
				if(rList.size() > 0) {
					mv.addObject("rList", rList);
				}
				mv.addObject("board", boardOne);
				mv.setViewName("board/detail");
			} else {
				mv.addObject("msg", "게시글 조회 실패");
				mv.addObject("error", "게시글 조회 실패");
				mv.addObject("url", "/board/list.kh");
				mv.setViewName("common/errorPage");
			}
			
		} catch (Exception e) {
			mv.addObject("msg", "게시글 조회 실패");
			mv.addObject("error", e.getMessage());
			mv.addObject("url", "/board/list.kh");
			mv.setViewName("common/errorPage");
		}
		
		return mv;
	}
	
	@RequestMapping(value = "/board/list.kh", method = RequestMethod.GET)
	public ModelAndView showBoardList(
			ModelAndView mv
			,@RequestParam(value = "page", required=false, defaultValue = "1") Integer currentPage) {
		
		Integer totalCount = bService.getListCount();
		PageInfo pInfo = this.getPageInfo(currentPage, totalCount);
		List<Board> bList = bService.selectBoardList(pInfo);
		
		mv.addObject("bList", bList).addObject("pInfo", pInfo).setViewName("/board/list");
		
//		mv.addObject("bList", bList);
//		mv.addObject("pInfo", pInfo);
//		mv.setViewName("/board/list");
		
		return mv;
	}

	

	@RequestMapping(value = "/board/write.kh", method = RequestMethod.GET)
	//void 대신 ModelAndView를 사용함, 데이터도 넣고 스트링도 넣고..? 페이지 이동도 하고 리턴할 수있음
	public ModelAndView showWriterForm(
			ModelAndView mv) {
		
		mv.setViewName("board/write");   //= return "board/write"
		return mv;
	}
	
	
	@RequestMapping(value = "/board/write.kh", method = RequestMethod.POST)
	//void 대신 ModelAndView를 사용함, 데이터도 넣고 스트링도 넣고..? 페이지 이동도 하고 리턴할 수있음
	public ModelAndView boardRegister(
			ModelAndView mv
			, @ModelAttribute Board board
			//첨부파일 사용 시 사용해야 하는 클래스
			,@RequestParam(value = "uploadFile", required = false) MultipartFile uploadFile
			,HttpServletRequest request
			, HttpSession session) { 
			
		try {
			String boardWriter = (String)session.getAttribute("memberId");  //session에서 id 가져와서 쓰는방법
			if(boardWriter != null && !boardWriter.equals("")) {
				board.setBoardWriter(boardWriter);
				
				if(uploadFile != null && !uploadFile.getOriginalFilename().equals("")) {
					//파일정보(이름, 리네임, 경로, 크기) 및 파일저장
					//여러가지 데이터를 받아서 리턴하기 위해 Map사용
					Map<String, Object> bMap = this.saveFile(request, uploadFile);
					
					//Object는 long타입이 있어서 사용했는데 따라서 형변환이 필요함
					board.setBoardFilename((String)bMap.get("fileName"));  //fileName 은 밑에 saveFile에 파일정보 리턴값과 일치해야함
					board.setBoardFileRename((String)bMap.get("fileRename"));
					board.setBoardFilepath((String)bMap.get("filePath"));
					board.setBoardFileLength((long)bMap.get("fileLength"));
				}
				int result = bService.insertBoard(board);
				mv.setViewName("redirect:/board/list.kh");
			} else {
				mv.addObject("msg", "로그인 정보 존재하지 않습니다");
				mv.addObject("error", "로그인이 필요합니다.");
				mv.addObject("url", "/index.jsp");
				mv.setViewName("common/errorPage");
			}
		} catch (Exception e) {
			//model.addAttribute("msg", "게시글 등록 실패");와 같은 기능
			mv.addObject("msg", "게시글 등록 실패");
			mv.addObject("error", e.getMessage());
			mv.addObject("url", "/board/writer.kh");
			mv.setViewName("common/errorPage");
	}
		return mv;
	
	
	}


	private Map<String, Object> saveFile(HttpServletRequest request, MultipartFile uploadFile) throws Exception {
		
		Map<String, Object> fileMap = new HashMap<String, Object>();
		
		//resources 경로 구하기
		String root = request.getSession().getServletContext().getRealPath("resources");
		
		//파일 저장 경로 구하기
		String savePath = root + "\\buploadFile";
		
		//파일 이름 구하기
		String fileName = uploadFile.getOriginalFilename();
		
		//파일 확장자 구하기
		String extension 
		= fileName.substring(fileName.lastIndexOf(".")+1);
		
		//시간으로 파일 리네임하기
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileRename = sdf.format(new Date(System.currentTimeMillis()));
		
		//파일 저장 전 폴더 만들기
		File saveFolder = new File(savePath);
		if(!saveFolder.exists()) {
			saveFolder.mkdir();
		}
		
		//파일저장
		File saveFile = new File(savePath + "\\" + fileRename);
		uploadFile.transferTo(saveFile);
		long fileLength = uploadFile.getSize();
		
		//파일정보 리턴
		fileMap.put("fileName", fileName);
		fileMap.put("fileRename", fileRename);
		fileMap.put("filePath", "../resources/buploadFiles/" + fileRename);
		fileMap.put("fileLength", fileLength);
		
		return fileMap;
	}	
	
	
	
	
	private PageInfo getPageInfo(Integer currentPage, Integer totalCount) {
		
		int recordCountPerPage = 10;
		int naviCountPerPage = 10;
		
		int naviTotalCount;
		naviTotalCount 
			= (int)Math.ceil((double)totalCount/recordCountPerPage);   //6.2 -> 7 올림해주는 식
		
		int startNavi = ((int)((double)currentPage/naviCountPerPage+0.9)-1)*naviCountPerPage+1;  //이거뭐냐...
		
		int endNavi = startNavi + naviCountPerPage - 1;
		
		if(endNavi > naviTotalCount) {
			endNavi = naviTotalCount;
		}
		
		
		
		PageInfo pInfo = new PageInfo(currentPage, totalCount, naviTotalCount, recordCountPerPage, naviCountPerPage, startNavi, endNavi);
		
		return pInfo;
	}
	
	
	
	//게시글 삭제
	@RequestMapping(value = "/board/delete.kh", method = RequestMethod.GET)
	public ModelAndView deleteBoard(
			ModelAndView mv
			, @RequestParam("boardNo") Integer boardNo
			, @RequestParam("boardWriter") String boardWriter
			, HttpSession session) {
		
		try {
			String memberId = (String)session.getAttribute("memberId");
			
			Board board = new Board();
			board.setBoardNo(boardNo);
			board.setBoardWriter(memberId);
			
			if(boardWriter != null && boardWriter.equals(memberId)) {
				int result = bService.deleteBoard(board);
				
				if(result > 0) {
					mv.setViewName("redirect:/board/list.kh");
				} else {
					mv.addObject("msg", "게시글 삭제가 완료되지 않았습니다.");
					mv.addObject("error", "게시글 삭제 불가");
					mv.addObject("url", "/board/List.kh");
					mv.setViewName("common/errorPage");
				}
				
			} else {
				mv.addObject("msg", "본인이 작성한 글만 삭제할 수 있습니다.");
				mv.addObject("error", "게시글 삭제 불가");
				mv.addObject("url", "/board/list.kh");
				mv.setViewName("common/errorPage");
			}
		} catch (Exception e) {
			mv.addObject("msg", "관리자에게 문의 바랍니다.");
			mv.addObject("error", e.getMessage());
//				mv.addObject("url", "board/detail.kh?boardNo=");
			mv.addObject("url", "/board/List.kh");
			mv.setViewName("common/errorPage");
		}
		return mv;
	}
	
	
	

	//게시글 수정
	@RequestMapping(value = "/board/modify.kh", method = RequestMethod.GET)
	public ModelAndView showModifyForm(
			ModelAndView mv
			, @RequestParam("boardNo") Integer boardNo) {
		try {
			Board board = bService.selectBoardByNo(boardNo);
			mv.addObject("board", board);
			mv.setViewName("board/modify");
		} catch (Exception e) {
			mv.addObject("msg", "관리자에게 문의 바랍니다.");
			mv.addObject("error", e.getMessage());
			mv.addObject("url", "/board/write.kh");
			mv.setViewName("common/errorPage");
		}
		
		return mv;
	}
	
	
	
	@RequestMapping(value = "/board/modify.kh", method = RequestMethod.POST)
	public ModelAndView boardModify(
			ModelAndView mv
			, @ModelAttribute Board board
			, @RequestParam(value="uploadFile", required=false) MultipartFile uploadFile
			, HttpServletRequest request
			, HttpSession session) {
		
		try {
			String memberId = (String)session.getAttribute("memberId");
			String boardWriter = board.getBoardWriter();
			
			if(boardWriter != null && boardWriter.equals(memberId)) {
				//수정이라는 과정은 대체하는것, 대체하는 방법은 삭제 후 등록하는 것
				if(uploadFile != null && !uploadFile.getOriginalFilename().equals("")) {
					String fileRename = board.getBoardFileRename();
					if(fileRename != null) {
						this.deleteFile(fileRename, request);
					}
					Map<String, Object> bFileMap = this.saveFile(request, uploadFile);
					board.setBoardFilename((String)bFileMap.get("fileName"));
					board.setBoardFileRename((String)bFileMap.get("fileRename"));
					board.setBoardFilepath((String)bFileMap.get("filePath"));
					board.setBoardFileLength((long)bFileMap.get("fileLength"));
				}
				int result = bService.updateBoard(board);
				if(result > 0) {
					mv.setViewName("redirect:/board/detail.kh?boardNo=" + board.getBoardNo());
				} else {
					mv.addObject("msg", "본인이 작성한 글만 수정할 수 있습니다.");
					mv.addObject("error", "게시글 수정 불가");
					mv.addObject("url", "/board/modify.kh?boardNo=" + board.getBoardNo());
					mv.setViewName("common/errorPage");
				}	
			} else {
				mv.addObject("msg", "게시글 수정 권한이 없습니다.");
				mv.addObject("error", "게시글 수정 불가");
				mv.addObject("url", "/board/modify.kh?boardNo=" + board.getBoardNo());
				mv.setViewName("common/errorPage");
			}
			
			
		} catch (Exception e) {
			mv.addObject("msg", "관리자에게 문의 바랍니다.");
			mv.addObject("error", e.getMessage());
			mv.addObject("url", "/board/modify.kh");
			mv.setViewName("common/errorPage");
		}
		
		return mv;
	}
	
	
	
	public void deleteFile(String fileRename, HttpServletRequest request) {
		String root = request.getSession().getServletContext().getRealPath("resources");
		String delFilePath = root + "\\buploadFiles\\" +  fileRename;
		File file = new File(delFilePath);
		if(file.exists()) {
			file.delete();
		}
		
	}
	
	
	
	
}
