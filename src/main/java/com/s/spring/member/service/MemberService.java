package com.s.spring.member.service;

import com.s.spring.member.domain.Member;

public interface MemberService {

	/**
	 * 회원가입 service
	 * @param member
	 * @return int
	 */
	int insertMembere(Member member);

	/**
	 * 로그인 service
	 * @param member
	 * @return int
	 */
//	int checkMemberLogin(Member member);
	Member checkMemberLogin(Member member);

	/**
	 * 마이페이지(회원정보조회) service
	 * @param memberId
	 * @return Member
	 */
	Member getMemberById(String memberId);

	/**
	 * 회원정보수정 service
	 * @param member
	 * @return int
	 */
	int updateMember(Member member);

	/**
	 * 회원탈퇴 service update로 할 예정
	 * @param memberId
	 * @return int
	 */
	int deleteMember(String memberId);

}
