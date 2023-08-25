package com.s.spring.board.store;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.s.spring.board.domain.Board;
import com.s.spring.board.domain.PageInfo;

public interface BoardStore {

	/**
	 * 게시글 등록 Store
	 * @param session
	 * @param board
	 * @return
	 */
	int insertBoard(SqlSession session, Board board);

	/**
	 * 전체 게시물 갯수 Store
	 * @param session
	 * @return
	 */
	int selectListCount(SqlSession session);

	/**
	 * 게시글 전체 조회 Store
	 * @param session
	 * @param pInfo
	 * @return
	 */
	List<Board> selectBoardList(SqlSession session, PageInfo pInfo);

	/**
	 * 게시글 상세 조회
	 * @param session
	 * @param boardNo
	 * @return
	 */
	Board selectBoardByNo(SqlSession session, Integer boardNo);

}
