package com.s.spring.member.store.logic;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.s.spring.member.domain.Member;
import com.s.spring.member.store.MemberStore;

@Repository
public class MembereStoreLogic implements MemberStore{

	@Override
	public int insertMembere(SqlSession session, Member member) {
		int result = session.insert("MemberMapper.insertMember", member);
		return result;
	}

//	@Override
//	public int checkMemberLogin(SqlSession session, Member member) {
//		int result = session.selectOne("MemberMapper.checkMemberLogin",member);
//		return result;
//	}
	@Override
	public Member checkMemberLogin(SqlSession session, Member member) {
		Member result = session.selectOne("MemberMapper.checkMemberLogin",member);
		return result;
	}

	@Override
	public Member getMemberById(SqlSession session, String memberId) {
		Member result = session.selectOne("MemberMapper.getMemberById",memberId);
		return result;
	}

	@Override
	public int updateMember(SqlSession session, Member member) {
		int result = session.insert("MemberMapper.updateMember", member);
		return result;
	}

	@Override
	public int deleteMember(SqlSession session, String memberId) {
		int result = session.update("MemberMapper.deleteMember",memberId);
		return result;
	}

}
