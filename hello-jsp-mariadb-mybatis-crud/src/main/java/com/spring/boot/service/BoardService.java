package com.spring.boot.service;

import java.util.List;

import com.spring.boot.model.BoardDTO;

public interface BoardService {
	public int selectBoardListCount(String searchKey, String searchValue) throws Exception;
	public List<BoardDTO> selectBoardList(int start,int end, String searchKey, String searchValue) throws Exception;
	public BoardDTO selectBoard(int num) throws Exception;
	
	public int maxNum() throws Exception;
	
	public void insertBoard(BoardDTO dto) throws Exception;
	public void updateBoardHitCount(int num) throws Exception;
	public void updateBoard(BoardDTO dto) throws Exception;
	public void deleteBoard(int num) throws Exception;
}
