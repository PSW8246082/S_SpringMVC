package com.s.spring.member.store;

import org.apache.ibatis.session.SqlSession;

import com.s.spring.member.domain.Member;

public interface MemberStore {

	int insertMembere(SqlSession session, Member member);

//	int checkMemberLogin(SqlSession session, Member member);
	Member checkMemberLogin(SqlSession session, Member member);

	Member getMemberById(SqlSession session, String memberId);

	int updateMember(SqlSession session, Member member);

	int deleteMember(SqlSession session, String memberId);

}
