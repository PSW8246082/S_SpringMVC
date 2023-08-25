package com.s.spring.notice.store;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.s.spring.notice.domain.Notice;
import com.s.spring.notice.domain.PageInfo;

public interface NoticeStore {

	/**
	 * 공지사항 등록 Store
	 * @param session
	 * @param notice
	 * @return int
	 */
	int insertNotice(SqlSession session, Notice notice);

	/**
	 * 공지사항 리스트 갯수 조회 store
	 * @param session
	 * @return List<Notice>
	 */
	int selectListCount(SqlSession session);

	/**
	 * 공지사항 목록조회 store
	 * @param session
	 * @param pInfo
	 * @return List<Notice>
	 */
	List<Notice> selectNoticeList(SqlSession session, PageInfo pInfo);

	

	/**
	 * 공지사항 all 조회 service
	 * @param searchKeyword
	 * @return List<Notic>
	 */
	List<Notice> searchNoticeByAll(SqlSession session, String searchKeyword);

	/**
	 * 공지사항 작성자 조회 service
	 * @param searchKeyword
	 * @return List<Notic>
	 */
	List<Notice> searchNoticeByWriter(SqlSession session, String searchKeyword);
	
	/**
	 * 공지사항 제목으로 조회 store
	 * @param session
	 * @param searchKeyword
	 * @return
	 */
	List<Notice> searchNoticeByTitle(SqlSession session, String searchKeyword);

	/**
	 * 공지사항 내용 조회 Store
	 * @param searchKeyword
	 * @return List<Notic>
	 */
	List<Notice> searchNoticeByContent(SqlSession session, String searchKeyword);

	/**
	 * 공지사항 조건에 따라 키워드로 조회 Store
	 * @param session
	 * @param searchCondition all, writer, title. content
	 * @param searchKeyword
	 * @return List
	 */
	List<Notice> searchNoticesByKeyword(SqlSession session, PageInfo pInfo, Map<String, String> paramMap);

	/**
	 * 공지사항 검색 게시물 전체 갯수 Store
	 * @param session
	 * @param paramMap
	 * @return
	 */
	int selectListCount(SqlSession session, Map<String, String> paramMap);

	/**
	 * 공지사항 번호로 조회
	 * @param session
	 * @param noticeNo
	 * @return
	 */
	Notice selectNoticeByNo(SqlSession session, Integer noticeNo);

	/**
	 * 공지사항 수정 Store
	 * @param session
	 * @param notice
	 * @return
	 */
	int updateNotice(SqlSession session, Notice notice);

	

}
