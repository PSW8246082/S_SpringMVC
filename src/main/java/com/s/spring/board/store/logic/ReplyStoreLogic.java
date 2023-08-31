package com.s.spring.board.store.logic;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.s.spring.board.domain.Reply;
import com.s.spring.board.store.ReplyStore;

@Repository
public class ReplyStoreLogic implements ReplyStore{

	@Override
	public int insertReply(SqlSession session, Reply reply) {
		int result = session.insert("ReplyMapper.insertReply", reply);
		return result;
	}
	
	@Override
	public int updateReply(SqlSession session, Reply reply) {
		int result = session.update("ReplyMapper.updateReply", reply);
		return result;
	}

	@Override
	public List<Reply> selectReplyList(int refBoardNo, SqlSession session) {
		List<Reply> rList = session.selectList("ReplyMapper.selectReplyList",refBoardNo);
		return rList;
	}

	//삭제 방법1)
//	@Override
//	public int deleteReply(SqlSession session, Reply reply) {
//	int result = session.delete("ReplyMapper.deleteReply", reply);
//	return result;
//	}
	
	//삭제 방법2)
	public int deleteReply(SqlSession session, Reply reply) {
	int result = session.update("ReplyMapper.deleteReply", reply);
	return result;
	}
	

}
