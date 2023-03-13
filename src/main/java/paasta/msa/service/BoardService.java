package paasta.msa.service;

import java.util.List;
import java.util.Map;

public interface BoardService {

	public Map<String, Object> getBoardCount(Map<String, Object> paramMap) throws Exception;
	
	public List<Object> getBoardList(Map<String, Object> paramMap) throws Exception;
	
	public List<Object> getBoard(Map<String, Object> paramMap) throws Exception;

	public int postBoard(Map<String, Object> paramMap) throws Exception;

	public int putBoard(Map<String, Object> paramMap) throws Exception;

	public int deleteBoard(Map<String, Object> paramMap) throws Exception;
}
