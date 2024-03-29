package com.spring.boot.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.boot.service.BoardService;
import com.spring.boot.mapper.BoardMapper;
import com.spring.boot.model.BoardDTO;

@Service
public class BoardServiceImpl implements BoardService{
	
	@Autowired
	private BoardMapper boardMapper;
	
	@Override
	public int selectBoardListCount(String searchKey, String searchValue) throws Exception {
		return boardMapper.selectBoardListCount(searchKey, searchValue);
	}
	
	@Override
	public List<BoardDTO> selectBoardList(int start, int end, String searchKey, String searchValue) throws Exception {
		return boardMapper.selectBoardList(start, end, searchKey, searchValue);
	}

	@Override
	public BoardDTO selectBoard(int num) throws Exception {
		return boardMapper.selectBoard(num);
	}

	@Override
	public int maxNum() throws Exception {
		return boardMapper.maxNum();
	}

	@Override
	public void insertBoard(BoardDTO dto) throws Exception {
		boardMapper.insertBoard(dto);
	}

	@Override
	public void updateBoardHitCount(int num) throws Exception {
		boardMapper.updateBoardHitCount(num);
	}

	@Override
	public void updateBoard(BoardDTO dto) throws Exception {
		boardMapper.updateBoard(dto);
	}

	@Override
	public void deleteBoard(int num) throws Exception {
		boardMapper.deleteBoard(num);
	}

}
