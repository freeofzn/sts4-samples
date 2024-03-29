package com.spring.boot.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchCriteria {
	private String pageNum;
	private String searchKey;
	private String searchValue;
}