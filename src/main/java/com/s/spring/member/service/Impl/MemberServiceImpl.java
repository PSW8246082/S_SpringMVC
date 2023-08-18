package com.s.spring.member.service.Impl;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.s.spring.member.domain.Member;
import com.s.spring.member.service.MemberService;
import com.s.spring.member.store.MemberStore;

@Service
public class MemberServiceImpl implements MemberService{

	@Autowired
	private SqlSession session;
	
	@Autowired
	private MemberStore mStore;

	@Override
	public int insertMembere(Member member) {
		int result = mStore.insertMembere(session,member);
		return result;
	}

//	@Override
//	public int checkMemberLogin(Member member) {
//		int result = mStore.checkMemberLogin(session,member);
//		return result;
//	}
	@Override
	public Member checkMemberLogin(Member member) {
		Member result = mStore.checkMemberLogin(session,member);
		return result;
	}

	@Override
	public Member getMemberById(String memberId) {
		Member result = mStore.getMemberById(session, memberId);
		return result;
	}

	@Override
	public int updateMember(Member member) {
		int result = mStore.updateMember(session,member);
		return result;
	}

	@Override
	public int deleteMember(String memberId) {
		int result = mStore.deleteMember(session,memberId);
		return result;
	}
	
	
}
