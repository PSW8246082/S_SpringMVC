package com.s.spring.notice.service;

import java.util.List;
import java.util.Map;

import com.s.spring.notice.domain.Notice;
import com.s.spring.notice.domain.PageInfo;

public interface NoticeService {

	/**
	 * 공지사항등록 service
	 * @param notice
	 * @return int
	 */
	int insertNotice(Notice notice);

	/**
	 * 공지사항 리스트 service
	 * @return List<Notice>
	 */
	List<Notice> selectNoticeList(PageInfo pInfo);

	/**
	 * 공지사항 전체 갯수 조회 service
	 * @return int
	 */
	int getListCount();

	

	/**
	 * 공지사항 all조회 service
	 * @param searchKeyword
	 * @return List<Notic>
	 */
	List<Notice> searchNoticeByAll(String searchKeyword);

	/**
	 * 공지사항 작성자 조회 service
	 * @param searchKeyword
	 * @return List<Notic>
	 */
	List<Notice> searchNoticeByWriter(String searchKeyword);
	
	/**
	 * 공지사항 제목으로 조회 service
	 * @param searchKeyword
	 * @return List<Notic>
	 */
	List<Notice> searchNoticeByTitle(String searchKeyword);

	/**
	 * 공지사항 내용 조회 service
	 * @param searchKeyword
	 * @return List<Notic>
	 */
	List<Notice> searchNoticeByContent(String searchKeyword);

	/**
	 * 공지사항 조건에 따라 키워드로 검색 service
	 * @param searchCondition
	 * @param searchKeyword
	 * @return
	 */
	List<Notice> searchNoticesByKeyword(PageInfo pInfo, Map<String, String> paramMap);

	/**
	 * 공지사항 검색게시물 전체 갯수 Service
	 * @param paramMap
	 * @return
	 */
	int getListCount(Map<String, String> paramMap);

	

	

}
