package com.s.spring.notice.store.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.s.spring.notice.domain.Notice;
import com.s.spring.notice.domain.PageInfo;
import com.s.spring.notice.store.NoticeStore;

@Repository
public class NoticeStoreLogic implements NoticeStore{

	@Override
	public int insertNotice(SqlSession session, Notice notice) {
		int result = session.insert("NoticeMapper.insertNotice", notice);
		return result;
	}

	@Override
	public List<Notice> selectNoticeList(SqlSession session, PageInfo pInfo) {
		
		int limit = pInfo.getRecordCountPerPage();  //한 페이지에 가져오는 행의 갯수(리스트개수)
		int offset = (pInfo.getCurrentPage()-1)*limit;;  //변하는 디폴드값 
		RowBounds rowBounds = new RowBounds(offset, limit);
		List<Notice> nList 
			= session.selectList("NoticeMapper.selectNoticeList", null, rowBounds);
		return nList;
	}
	
//	public void generatePageNavi() {
//		
//	}

	@Override
	public int selectListCount(SqlSession session) {
		int result = session.selectOne("NoticeMapper.selectListCount");
		return result;
	}

	
	@Override
	public List<Notice> searchNoticeByAll(SqlSession session, String searchKeyword) {
		List<Notice> searchList = session.selectList("NoticeMapper.searchNoticeByAll", searchKeyword) ;
		return searchList;
	}

	@Override
	public List<Notice> searchNoticeByWriter(SqlSession session, String searchKeyword) {
		List<Notice> searchList = session.selectList("NoticeMapper.searchNoticeByWriter", searchKeyword) ;
		return searchList;
	}
	
	@Override
	public List<Notice> searchNoticeByTitle(SqlSession session, String searchKeyword) {
		List<Notice> searchList = session.selectList("NoticeMapper.searchNoticeByTitle", searchKeyword) ;
		return searchList;
	}

	@Override
	public List<Notice> searchNoticeByContent(SqlSession session, String searchKeyword) {
		List<Notice> searchList = session.selectList("NoticeMapper.searchNoticeByContent", searchKeyword) ;
		return searchList;
	}

	@Override
	public List<Notice> searchNoticesByKeyword(SqlSession session, PageInfo pInfo, Map<String, String> paramMap) {
		
//		//두가지 값을 하나의 변수로 사용하는법 (searchCondition, searchKeyword)
//		//1.VO클래스 만들기(이미해봄)
//		//2. HashMap 사용(안해봄)
//		Map<String, String> paramMap = new HashMap<String, String>();
//		//put() 메소드를 사용해서 key-value 설정을 하는데 key값(파란색)이 mapper.xml에서 사용된다.
//		paramMap.put("searchCondition", searchCondition);
//		paramMap.put("searchKeyword", searchKeyword);
		
		int limit = pInfo.getRecordCountPerPage();
		int offset = (pInfo.getCurrentPage()-1)*limit;
		
		RowBounds rowBounds = new RowBounds(offset, limit);
		List<Notice> searchList = session.selectList("NoticeMapper.searchNoticesByKeyword", paramMap, rowBounds);
		return searchList;
	}

	@Override
	public int selectListCount(SqlSession session, Map<String, String> paramMap) {
		int result = session.selectOne("NoticeMapper.selectListByKeywordCount", paramMap);
		return result;
	}

	@Override
	public Notice selectNoticeByNo(SqlSession session, Integer noticeNo) {
		Notice noticeOne = session.selectOne("NoticeMapper.selectNoticeByNo", noticeNo);
		return noticeOne;
	}

	@Override
	public int updateNotice(SqlSession session, Notice notice) {
		int result = session.update("NoticeMapper.updateNotice",notice);
		return result;
	}

}
