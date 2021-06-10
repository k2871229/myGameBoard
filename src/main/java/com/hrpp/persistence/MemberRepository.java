package com.hrpp.persistence;

import org.springframework.data.repository.CrudRepository;

import com.hrpp.domain.Member;

public interface MemberRepository 
	extends CrudRepository<Member, String>{

}
