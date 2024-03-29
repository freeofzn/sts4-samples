package com.spring.boot.web;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.boot.model.BoardDTO;
import com.spring.boot.model.SearchCriteria;
import com.spring.boot.service.BoardService;
import com.spring.boot.util.MyUtil;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/board")
public class BoardController {
	
	@Resource
	private BoardService boardService;

	@Autowired
	MyUtil myUtil; 
	
	@GetMapping("/")
	public String index(Model model) throws Exception{
		return "index";
	}

	/**
	 * 게시물 목록을 조회한다.
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */	
	@GetMapping("/list")
	public String list(@ModelAttribute("scri") SearchCriteria cond, Model model) throws Exception{
		String pageNum = cond.getPageNum(); // 페이지 번호
		
		/*----- pagination 관련 하드코딩 S -----*/ 
		int currentPageNo	   = 1  ; // 현재 페이지 번호	 
		int recordCountPerPage = 5 	; // 한 페이지당 게시되는 게시물 건 수 
		int pageSize	       = 10 ; // 페이지 리스트에 게시되는 페이지 건수	 
		int totalRecordCount   = 0  ; // 전체 게시물 건 수
		
		if(pageNum!=null) currentPageNo = Integer.parseInt(pageNum);		
		/*----- pagination 관련 하드코딩 E -----*/
		
		/*--- 1. 게시물 검색 ---*/
		String searchKey = cond.getSearchKey();
		String searchValue = cond.getSearchValue();		
		
		if(searchValue == null) {
			searchKey = "subject";
			searchValue = "";
		}
		
		int firstRecordIndex = (currentPageNo - 1 ) * recordCountPerPage + 1;
		int lastRecordIndex = currentPageNo * recordCountPerPage;		
		List<BoardDTO> lists = boardService.selectBoardList(firstRecordIndex, lastRecordIndex, searchKey, searchValue);

		/*--- 2. paginationInfo 생성 ---*/
		String param = "";
		
		if(searchValue!=null&&!searchValue.equals("")) { 
			param = "searchKey=" + searchKey;
			param+= "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
		}
		
		String listUrl = "/board/list";
		if(!param.equals("")) listUrl += "?" + param;

		totalRecordCount = boardService.selectBoardListCount(searchKey, searchValue);
		String paginationInfo = myUtil.paginationInfo(currentPageNo, recordCountPerPage, pageSize, totalRecordCount, listUrl);

		/*--- 3. 상세 url 생성 ---*/
		String articleUrl = "/board/view?pageNum=" + currentPageNo;
		if(!param.equals("")) {
			articleUrl += "&" + param;
		}
	
		/*--- 4. 화면 return ---*/
		model.addAttribute("lists", lists);
		model.addAttribute("articleUrl", articleUrl);  
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("totalRecordCount", totalRecordCount);
		
        return "bbs/boardList";
	}
		
	/**
	 * 게시물 상세내역을 조회한다.
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */		
	@RequestMapping(value = "/view", method = {RequestMethod.GET,RequestMethod.POST})
	public String article(HttpServletRequest request, Model model) throws Exception{
		int num = Integer.parseInt(request.getParameter("num"));
		String pageNum = request.getParameter("pageNum");
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
	
		if(searchValue!=null) {
			searchValue = URLDecoder.decode(searchValue, "UTF-8");
		}
		
		boardService.updateBoardHitCount(num);
		BoardDTO dto = boardService.selectBoard(num);
		
		if(dto == null) {
			return "redirect:/board/list"; 
		}
		
		int lineSu = dto.getContent().split("\n").length;
		
		String param = "pageNum=" + pageNum;
		if(searchValue!=null&&!searchValue.equals("")) { //검색을 했다는뜻
			param += "&searchKey=" + searchKey;
			param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
    	}
		
		model.addAttribute("dto", dto);
		model.addAttribute("params", param);
		model.addAttribute("lineSu", lineSu);
		model.addAttribute("pageNum", pageNum);		
		
		return "bbs/boardView";
	}
	
	/**
	 * 신규등록 화면으로 이동한다.
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */		
	@GetMapping("/go-new")
	public String created(Model model) throws Exception{
		return "bbs/boardNew";
	}
	
	/**
	 * 게시물을 신규등록 한다.
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */		
	@PostMapping("/saveNew")
	public String created_ok(BoardDTO dto, HttpServletRequest request) throws Exception{
		int maxNum = boardService.maxNum();
		dto.setNum(maxNum + 1);
		dto.setIpAddr(request.getRemoteAddr());
		boardService.insertBoard(dto);
		return "redirect:/board/list";
	}
		
	/**
	 * 수정화면으로 이동한다.
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */			
	@RequestMapping(value = "/go-edit", method = {RequestMethod.GET,RequestMethod.POST})
	public String updated(HttpServletRequest request, Model model) throws Exception{
		
		int num = Integer.parseInt(request.getParameter("num"));
	  	String pageNum = request.getParameter("pageNum");
        String searchKey = request.getParameter("searchKey");
	  	String searchValue = request.getParameter("searchValue");
	  	if(searchValue != null) {
		  	searchValue = URLDecoder.decode(searchValue, "UTF-8");
	  	}
		
	  	BoardDTO dto = boardService.selectBoard(num);
		
		if(dto == null) {
			return "redirect:/board/list?pageNum=" + pageNum;
		}
		
		String param = "pageNum=" + pageNum;
		if(searchValue != null && !searchValue.equals("")) {
			param += "&searchKey=" +searchKey;
			param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");		
		}

		model.addAttribute("dto", dto);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("params", param);
		model.addAttribute("searchKey", searchKey);
		model.addAttribute("searchValue", searchValue);		
		
		return "bbs/boardEdit";		
		
	}
	
	/**
	 * 게시물을 수정한다.
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */			
	@RequestMapping(value = "/saveEdit", method = {RequestMethod.GET,RequestMethod.POST})
	public String updated_ok(BoardDTO dto, HttpServletRequest request) throws Exception{
		String pageNum = request.getParameter("pageNum");
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		dto.setContent(dto.getContent().replaceAll( "<br/>", "\r\n"));
		
		boardService.updateBoard(dto);
		
		String param = "?pageNum=" + pageNum;
		if(searchValue!=null&&!searchValue.equals("")) {
			param += "&searchKey=" + searchKey;
			param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
		}
		
		return "redirect:/board/list" + param;
    }
	
	/**
	 * 게시물을 삭제한다.
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */			
	@RequestMapping(value = "/delete", method = {RequestMethod.GET,RequestMethod.POST})
	public String deleted_ok(HttpServletRequest request) throws Exception{
		int num = Integer.parseInt(request.getParameter("num"));
		String pageNum = request.getParameter("pageNum");
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		boardService.deleteBoard(num);
		
		String param = "?pageNum=" + pageNum;
		if(searchValue!=null&&!searchValue.equals("")) {
			param += "&searchKey=" + searchKey;
			param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8");
		}
 		
		return "redirect:/board/list" + param;		
	}
		
}
