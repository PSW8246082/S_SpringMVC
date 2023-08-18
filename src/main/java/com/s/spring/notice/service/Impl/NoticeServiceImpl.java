package com.s.spring.notice.service.Impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.s.spring.notice.domain.Notice;
import com.s.spring.notice.domain.PageInfo;
import com.s.spring.notice.service.NoticeService;
import com.s.spring.notice.store.NoticeStore;

@Service
public class NoticeServiceImpl implements NoticeService{

	@Autowired
	private SqlSession session;
	
	
	@Autowired
	private NoticeStore nStore;
	
	
	@Override
	public int insertNotice(Notice notice) {
		int result = nStore.insertNotice(session,notice);
		return result;
	}


	@Override
	public List<Notice> selectNoticeList(PageInfo pInfo) {
		List<Notice> nList = nStore.selectNoticeList(session, pInfo);
		return nList;
	}


	@Override
	public int getListCount() {
		int result = nStore.selectListCount(session);
		return result;
	}


	

	@Override
	public List<Notice> searchNoticeByAll(String searchKeyword) {
		List<Notice> nList = nStore.searchNoticeByAll(session, searchKeyword);
		return nList;
	}


	@Override
	public List<Notice> searchNoticeByWriter(String searchKeyword) {
		List<Notice> nList = nStore.searchNoticeByWriter(session, searchKeyword);
		return nList;
	}

	@Override
	public List<Notice> searchNoticeByTitle(String searchKeyword) {
		List<Notice> nList = nStore.searchNoticeByTitle(session, searchKeyword);
		return nList;
	}


	@Override
	public List<Notice> searchNoticeByContent(String searchKeyword) {
		List<Notice> nList = nStore.searchNoticeByContent(session, searchKeyword);
		return nList;
	}


	@Override
	public List<Notice> searchNoticesByKeyword(PageInfo pInfo , Map<String, String> paramMap) {
		List<Notice> nList = nStore.searchNoticesByKeyword(session, pInfo, paramMap);
		return nList;
	}


	@Override
	public int getListCount(Map<String, String> paramMap) {
		int result = nStore.selectListCount(session,paramMap);
		return result;
	}

}
