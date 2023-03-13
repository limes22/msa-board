package paasta.msa.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import paasta.msa.common.CommonUtil;
import paasta.msa.service.BoardService;

/**
 * @author JaemooSong
 *
 */
@RestController
public class BoardController {

	@Resource(name = "boardService")
	private BoardService boardService;

	@RequestMapping(value = "/board", method = RequestMethod.GET)
	public Map<String, Object> getBoards(@RequestParam(required = false) String searchType,
			@RequestParam(required = false) String searchValue, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer pagePerCount) {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();

		Map<String, Object> boardCount = null;
		List<Object> boardList = null;

		// parameter Setting
		try {
			paramMap.put("searchType", searchType);
			paramMap.put("searchValue", searchValue);
			paramMap.put("page", CommonUtil.nvl(page, 1));
			paramMap.put("pagePerCount", CommonUtil.nvl(pagePerCount, 15));
			paramMap.put("offset", ((int)paramMap.get("page") - 1) * (int)paramMap.get("pagePerCount"));
		} catch (Exception e) {
			result.put("result", "ERROR");
			result.put("errMsg", "input parameter error.");
			e.printStackTrace();
			return result;
		}

		// Select BoardList
		try {

			// select BoardCount
			boardCount = boardService.getBoardCount(paramMap);
			long count = 0;
			
			if(boardCount != null) {
				count = (Long)boardCount.get("COUNT");
				resultData.put("boardCount", count);
			}

			// select BoardList
			if(count != 0) {
				boardList = (List<Object>)boardService.getBoardList(paramMap);
				resultData.put("boardList", boardList);
			}

			resultData.put("page", paramMap.get("page"));
			resultData.put("pagePerCount", paramMap.get("pagePerCount"));
			
			result.put("result", "SUCCESS");
			result.put("resultData", resultData);
		} catch (Exception e) {
			result.put("result", "ERROR");
			result.put("errMsg", e.getMessage());
			e.printStackTrace();
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/board/{boardSeq}", method = RequestMethod.GET)
	public Map<String, Object> getBoard(@PathVariable("boardSeq") int boardSeq) {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();

		List<Object> boardList = null;

		// parameter Setting
		paramMap.put("boardSeq", boardSeq);

		try {
			// select Board
			boardList = (List<Object>)boardService.getBoard(paramMap);
			if(boardList != null && boardList.size() == 1) {
				resultData = (Map<String, Object>)boardList.get(0);

			} else {
				throw new Exception("일치하는 게시물이 없습니다.");
			}

			result.put("result", "SUCCESS");
			result.put("resultData", resultData);
		} catch (Exception e) {
			result.put("result", "ERROR");
			result.put("errMsg", e.getMessage());
			e.printStackTrace();
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/board", method = RequestMethod.POST)
	public Map<String, Object> postBoard(HttpEntity<String> httpEntity) {
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = httpEntity.getBody();

		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();

		// parameter Setting
		try {
			if(jsonString ==  null || "".equals(jsonString)) {
				jsonString = "{}";
			}
			Map<String, String> jsonMap = mapper.readValue(jsonString, Map.class);

			
			String boardTitle = jsonMap.get("boardTitle");
			String boardText = jsonMap.get("boardText");
			String writeUserId = jsonMap.get("writeUserId");
			String writeUserName = jsonMap.get("writeUserName");
			
			// null String check
			if(CommonUtil.isEmptyString(boardTitle)) {
				throw new Exception("게시물 제목은 필수 입력값입니다.");
			} else if(CommonUtil.isEmptyString(boardText)) {
				throw new Exception("게시물 내용은 필수 입력값입니다.");
			} else if(CommonUtil.isEmptyString(writeUserId)) {
				throw new Exception("사용자 ID는 필수 입력값입니다.");
			} else if(CommonUtil.isEmptyString(writeUserName)) {
				throw new Exception("사용자 명은 필수 입력값입니다.");
			}

			paramMap.put("boardTitle", boardTitle);
			paramMap.put("boardText", boardText);
			paramMap.put("writeUserId", writeUserId);
			paramMap.put("writeUserName", writeUserName);
		} catch (Exception e) {
			result.put("result", "ERROR");
			result.put("errMsg", e.getMessage());
			e.printStackTrace();

			return result;
		}
		
		try {
			
			int insertCount = boardService.postBoard(paramMap);
			if(insertCount != 1) {
				throw new Exception("게시물 생성에 실패하였습니다.");
			}
			
			result.put("result", "SUCCESS");
			result.put("boardSeq", paramMap.get("boardSeq"));
			result.put("resultData", resultData);
		} catch (Exception e) {
			result.put("result", "ERROR");
			result.put("errMsg", e.getMessage());
			e.printStackTrace();
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/board/{boardSeq}", method = RequestMethod.PUT)
	public Map<String, Object> putBoard(@PathVariable("boardSeq") int boardSeq, HttpEntity<String> httpEntity) {
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = httpEntity.getBody();

		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();

		// parameter Setting
		try {
			if(jsonString ==  null || "".equals(jsonString)) {
				jsonString = "{}";
			}
			Map<String, String> jsonMap = mapper.readValue(jsonString, Map.class);

			String boardTitle = jsonMap.get("boardTitle");
			String boardText = jsonMap.get("boardText");
			String writeUserId = jsonMap.get("writeUserId");
			
			// 수정 요청 Data가 없는 경우
			if(CommonUtil.isEmptyString(boardTitle) && CommonUtil.isEmptyString(boardText)) {
				result.put("result", "SUCCESS");
				return result;
			}
			
			paramMap.put("boardSeq", boardSeq);
			paramMap.put("boardTitle", boardTitle);
			paramMap.put("boardText", boardText);
			paramMap.put("writeUserId", writeUserId);
		} catch (Exception e) {
			result.put("result", "ERROR");
			result.put("errMsg", e.getMessage());
			e.printStackTrace();

			return result;
		}
		
		try {
			
			int updateCount = boardService.putBoard(paramMap);
			if(updateCount != 1) {
				throw new Exception("게시물 수정에 실패하였습니다.");
			}
			
			result.put("result", "SUCCESS");
			result.put("resultData", resultData);
		} catch (Exception e) {
			result.put("result", "ERROR");
			result.put("errMsg", e.getMessage());
			e.printStackTrace();
		}
		
		return result;
	}

	@RequestMapping(value = "/board/{boardSeq}/{writeUserId}", method = RequestMethod.DELETE)
	public Map<String, Object> deleteBoard(@PathVariable("boardSeq") int boardSeq, @PathVariable("writeUserId") String writeUserId, HttpEntity<String> httpEntity) {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> resultData = new HashMap<String, Object>();

		// parameter Setting
		paramMap.put("boardSeq", boardSeq);
		paramMap.put("writeUserId", writeUserId);
		
		try {
			
			int deleteCount = boardService.deleteBoard(paramMap);
			if(deleteCount == 0) {
				throw new Exception("게시물 삭제에 실패하였습니다.");
			}
			// TODO 게시물 코멘트 삭제
			result.put("result", "SUCCESS");
			result.put("resultData", resultData);
		} catch (Exception e) {
			result.put("result", "ERROR");
			result.put("errMsg", e.getMessage());
			e.printStackTrace();
		}
		
		return result;
	}
}
