package paasta.msa.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import paasta.msa.common.CommonDAO;

@Repository("boardDAO")
public class BoardDAO extends CommonDAO {
	
	public Map<String, Object> getBoardCount(Map<String, Object> paramMap) throws Exception {
		return getSqlSession().selectOne("paasta.msa.service.impl.BoardMapper.getBoardCount", paramMap);
	}

	public List<Object> getBoardList(Map<String, Object> paramMap) throws Exception {
		return getSqlSession().selectList("paasta.msa.service.impl.BoardMapper.getBoardList", paramMap);
	}

	public List<Object> getBoard(Map<String, Object> paramMap) throws Exception {
		return getSqlSession().selectList("paasta.msa.service.impl.BoardMapper.getBoard", paramMap);
	}

	public int postBoard(Map<String, Object> paramMap) throws Exception {

		return getSqlSession().insert("paasta.msa.service.impl.BoardMapper.postBoard", paramMap);
	}

	public int putBoard(Map<String, Object> paramMap) throws Exception {

		return getSqlSession().update("paasta.msa.service.impl.BoardMapper.putBoard", paramMap);
	}

	public int deleteBoard(Map<String, Object> paramMap) throws Exception {

		return getSqlSession().delete("paasta.msa.service.impl.BoardMapper.deleteBoard", paramMap);
	}

}
