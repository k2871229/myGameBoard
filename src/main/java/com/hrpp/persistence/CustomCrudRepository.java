package com.hrpp.persistence;

import org.springframework.data.repository.CrudRepository;

import com.hrpp.domain.GameBoard;

public interface CustomCrudRepository 
extends CrudRepository<GameBoard, Long>, CustomGameBoard{

}
