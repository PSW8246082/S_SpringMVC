package com.s.spring.notice.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.s.spring.notice.domain.Notice;
import com.s.spring.notice.domain.PageInfo;
import com.s.spring.notice.service.NoticeService;

@Controller
public class NoticeController {
	
	@Autowired
	private NoticeService service;

	@RequestMapping(value = "/notice/insert.kh", method = RequestMethod.GET)
	public String showInsertForm() {
		return "notice/insert";
	}
	
	@RequestMapping(value = "/notice/insert.kh", method = RequestMethod.POST)
	public String insertNotice(
			//@ModelAttribute을 사용하려면 JSP의 NAME값이 필드값과 같아야함
			@ModelAttribute Notice notice
			, @RequestParam(value="uploadFile", required=false) MultipartFile uploadFile
			, HttpServletRequest request
			, Model model) {
		try {
			if(uploadFile != null && ! uploadFile.getOriginalFilename().equals("")) {
				
				Map<String, Object> nMap = this.saveFile(uploadFile, request);
				String fileName = (String)nMap.get("fileName");
				String fileRename = (String)nMap.get("fileRename");
				String savePath = (String)nMap.get("filePath");
				long fileLength = (long)nMap.get("fileLength");
				
				
				//DB에 저장하기 위해 notice에 데이터를 Set하는 부분
				notice.setNoticeFilename(fileName);
				notice.setNoticeFileRename(fileRename);
				notice.setNoticeFilepath(savePath);
				notice.setNoticeFilelength(fileLength);
			}
			int result = service.insertNotice(notice);
			if(result >0) {
				return "redirect:/notice/list.kh";
			} else {
				model.addAttribute("msg", "공지사항 등록이 완료되지 않았습니다.");
				model.addAttribute("error", "공지사항 등록 실패");
				model.addAttribute("url", "/notice/insert.kh");
				return "common/errorPage";
			}
		} catch (Exception e) {
			model.addAttribute("msg", "관리자에게 문의바람");
			model.addAttribute("error", e.getMessage());
			model.addAttribute("url", "/member/register.kh");
			return "common/errorPage";
		}
	}
	
	
	@RequestMapping(value = "/notice/list.kh", method = RequestMethod.GET)
	public String showNoticeList(
			//required=false 필수가 아니어도 됨, Integer은 null 쓸 수 있음
			//주석처리된 삼항연산자와 같은 내용
			@RequestParam(value="page", required=false, defaultValue="1") Integer currentPage
			,Model model) {
		try {
//			int currentPage = page != 0 ? page : 1;
			//SELECT COUNT(*) FROM NOTICE_TBL
			int totalCount = service.getListCount();
			PageInfo pInfo = this.getPageInfo(currentPage, totalCount);
			
			List<Notice> nList = service.selectNoticeList(pInfo);
			//List 데이터의 유효성 체크 방법 2가지
			//1. isEmpty()
			//2. size()
			if(nList.size() > 0) {
				model.addAttribute("pInfo", pInfo);
				model.addAttribute("nList",nList);
				return "notice/list";
			} else {
				model.addAttribute("msg", "공지사항 목록조회 완료되지 않았습니다.");
				model.addAttribute("error", "공지사항 목록조회 실패");
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
	
	
	
	public PageInfo getPageInfo(int currentPage, int totalCount) {
		
		PageInfo pi = null;
		
		int recordCountPerPage = 10;
		int naviCountPerPage = 10;
		int naviTotalCount;
		int startNavi;
		int endNavi;
		
		//강제형변환,자동형변환
		naviTotalCount = (int)((double)totalCount/recordCountPerPage + 0.9);
		//Math.ceil((double)totalCount/recordCountPerPage) 도 사용가능함
		
		//currentPage값이 1~5d일떄 startNavi가 1로 고정되도록 구해주는 식
		startNavi 
			= ((int)((double)currentPage/naviCountPerPage + 0.9)-1)*naviCountPerPage +1;
		
		endNavi = startNavi + naviCountPerPage -1;
		//endNavi는 startNavi에 무조건 naviCountPerPager값을 더하므로 실제 최대값보다 커질 수 있음
		if(endNavi > naviTotalCount) {
			endNavi = naviTotalCount;
		}
		pi = new PageInfo(currentPage, recordCountPerPage, naviCountPerPage, startNavi, endNavi, totalCount, naviTotalCount);
		return pi;
	}
	
	
	
	
	@RequestMapping(value = "/notice/search.kh", method = RequestMethod.GET)
	public String searchNoticeList(
			@RequestParam("searchCondition") String searchCondition
			, @ RequestParam("searchKeyword") String searchKeyword
			, @ RequestParam(value="page", required=false, defaultValue="1") Integer currentPage
			, Model model) {
		
		//두가지 값을 하나의 변수로 사용하는법 (searchCondition, searchKeyword)
		//1.VO클래스 만들기(이미해봄)
		//2. HashMap 사용(안해봄)
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("searchCondition", searchCondition);
		paramMap.put("searchKeyword", searchKeyword);
		
		int totalCount = service.getListCount(paramMap);
		PageInfo pInfo = this.getPageInfo(currentPage, totalCount);
		//put() 메소드를 사용해서 key-value 설정을 하는데 key값(파란색)이 mapper.xml에서 사용된다.
		
				
		List<Notice> searchList = service.searchNoticesByKeyword(pInfo, paramMap);
		
//		List<Notice> searchList = new ArrayList<Notice>();
//		switch (searchCondition) {
//		case "all":
//			//SELECT * FROM NOTICE_TBL WHERE NOTICE_SUBJECT = ? OR NOTICE_CONTENT = ? OR NOTICE_WRITER =?
//			searchList = service.searchNoticeByAll(searchKeyword);
//			break;
//		case "writer":
//			//SELECT * FROM NOTICE_TBL WHERE NOTICE_WRITER = ?
//			searchList = service.searchNoticeByWriter(searchKeyword);
//			break;
//		case "title":
//			//SELECT * FROM NOTICE_TBL WHERE NOTICE_SUBJECT LIKE '%공지%';
//			searchList = service.searchNoticeByTitle(searchKeyword);
//			break;
//		case "content":
//			//SELECT * FROM NOTICE_TBL WHERE NOTICE_CONTENT = ?
//			searchList = service.searchNoticeByContent(searchKeyword);
//			break;
//		}
		if(!searchList.isEmpty()) {
//			model.addAttribute("searchCondition", searchCondition); paramMap으로 써도 됨
//			model.addAttribute("searchKeyword", searchKeyword);
			model.addAttribute("paramMap", paramMap);
			model.addAttribute("pInfo", pInfo);
			model.addAttribute("sList", searchList);
			return "notice/search";
		} else {
			model.addAttribute("msg", "데이터 조회가 완료되지 않았습니다.");
			model.addAttribute("error", "데이터 조회 실패");
			model.addAttribute("url", "/notice/list.kh");
			return "common/errorPage";
		}
		
	}
	
	
	@RequestMapping(value = "/notice/detail.kh", method = RequestMethod.GET)
	public String showNoticeDetail(
			@RequestParam("noticeNo") Integer noticeNo  //Integer은 null로 체크할 수 있게 해줌
			, Model model) {  //비지니스로직을 거쳐 jsp에서 사용할 수 있게 해줌
		
		Notice noticeOne = service.selectNoticeNo(noticeNo);
		model.addAttribute("notice", noticeOne); //notice jsp에서 쓸 수 있음
		return "notice/detail";
	}
	
	
	@RequestMapping(value="/notice/modify.kh", method = RequestMethod.GET)
	public String showModifyForm(
			@RequestParam("noticeNo") Integer noticeNo    // null체크를 위해 Integer 사용
			,Model model ) {  
			
		Notice noticeOne = service.selectNoticeNo(noticeNo);
		model.addAttribute("notice", noticeOne);
		return "notice/modify";
	}
	
	
	@RequestMapping(value="/notice/update.kh", method = RequestMethod.POST)
	public String updateNotice(
			@ModelAttribute Notice notice //jsp의 name의 값이 Notice의 필드명과 같아야함	제목 내용
			, @RequestParam(value="uploadFile", required=false) MultipartFile uploadFile  //첨부파일
			, Model model
			, HttpServletRequest request) {  //resources의 경로 가져올때 사용
	  
		try {
			if(uploadFile != null && !uploadFile.isEmpty()) {
				// 수정
				// 1.대체  2. 삭제 후 등록
				// 기존 업로드 된 파일의 존재여부 체크 후
				String fileName = notice.getNoticeFileRename();
				if(fileName != null) {
					// 있으면 기존 파일 삭제
					this.deleteFile(request, fileName);
				}
				// 없으면 새로 업로드 하려는 파일 저장
				Map<String, Object> infoMap = this.saveFile(uploadFile, request);
				
				String noticeFilename = (String)infoMap.get("fileName");
				long noticeFilelength = (long)infoMap.get("fileLength");
				String fileRename = (String)infoMap.get("fileRename");
				//set 하는 두가지 방법
				notice.setNoticeFilename(noticeFilename);
				notice.setNoticeFileRename(fileRename);
				notice.setNoticeFilepath((String)infoMap.get("filePath"));
				notice.setNoticeFilelength(noticeFilelength);
			}
			
			
			int result = service.updateNotice(notice);
			
			if(result > 0) {
				return "redirect:/notice/detail.kh?noticeNo=" + notice.getNoticeNo();
			} else {
				model.addAttribute("msg", "공지사항 목록조회 완료되지 않았습니다.");
				model.addAttribute("error", "공지사항 목록조회 실패");
				model.addAttribute("url", "/index.jsp");
				return "common/errorPage";
			}
			
			
		} catch (Exception e) {
			model.addAttribute("msg", "관리자에게 문의바랍니다.");
			model.addAttribute("error", e.getMessage());
			model.addAttribute("url", "/notice/list.kh");
			return "common/errorPage";
		}
		
	}
	
	
	
	
	
	
	
	
	
	public Map<String, Object> saveFile(
			MultipartFile uploadFile
			, HttpServletRequest request) throws Exception {
		
		
		//넘겨야하는 값, 리턴해야하는 값이 여러개일 때 사용하는 방법
		// 1.vo클래스를 만드는 방법
		// 2.HashMap을 사용하는 방법
		Map<String, Object> infoMap = new HashMap<String, Object>(); //long타입도 있으니 Object 사용..
		
		
		//================= 파일이름 =================
		String fileName = uploadFile.getOriginalFilename();
		//(내가 저장한 후에 그 경로르 DB에 저장하도록 준비하는 것)
		String root =
				request.getSession().getServletContext().getRealPath("resources");
		//폴더가 없을 경우 자동생성(내가 업로드한 파일을 저장할 폴더)
		String saveFolder = root + "\\nuploadFiles";
		File folder = new File(saveFolder);
		if(!folder.exists()) {
			folder.mkdir();
		}
		
		//================= 파일경로 =================
		
//		이미지 파일 이름 랜덤으로 생성
//		Random rand = new Random();
//		String strResult = "N";  //N으로 시작함
//		for(int i = 0; i<7; i++) {
//			int result = rand.nextInt(100)+1;
//			strResult += result+"";
//		}
		
		
//		이미지 파일 이름 시간으로 생성
		//나중에 대문자 ss, SS랑 비교하기
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String strResult = sdf.format(new Date(System.currentTimeMillis()));
		
		//.을 포함하지 않고 자름 +1
		String ext = fileName.substring(fileName.lastIndexOf(".")+1);  
		String fileRename = "N" + strResult + "." + ext;
		String savePath = saveFolder + "\\" + fileRename;
		File file = new File(savePath);
		// **************** 파일저장 *****************
		uploadFile.transferTo(file);
				
		//================= 파일크기 =================
		long fileLength = uploadFile.getSize();
		
		//파일이름, 경로, 크기를 넘겨주기 위해 Map에 정보를 저장한 후 return함
		//왜 return 하는가? DB에 저장하기 위해서 필요한 정보이기 때문에
		infoMap.put("fileName", fileName);
		infoMap.put("fileRename", fileRename);
		infoMap.put("filePath", savePath);
		infoMap.put("fileLength", fileLength);	
		
		return infoMap;
	}
	
	
	
	
	private void deleteFile(HttpServletRequest request, String fileName) {
		String root = request.getSession().getServletContext().getRealPath("resources");
		String delFilepath = root + "\\nuploadFiles\\" + fileName;
		File file = new File(delFilepath);
//		File file = new File("/S_SpringMVC/src/main/webapp/resources/nuploadFiles/image3.jpg");
		if(file.exists()) {
			file.delete();
		}
		
	}

	
	
	
	
	
	
	
	
	
}
