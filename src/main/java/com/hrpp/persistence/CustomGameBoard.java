package com.hrpp.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomGameBoard {

	public Page<Object[]> getCustomPage(String type, String keyword,
			Pageable page);
	
}
