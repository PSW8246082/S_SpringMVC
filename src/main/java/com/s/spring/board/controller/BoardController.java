package com.s.spring.board.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
			,HttpServletRequest request) { 
			
		try {
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
