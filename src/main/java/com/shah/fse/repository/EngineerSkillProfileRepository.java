package com.shah.fse.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.shah.fse.entity.EngineerDetails;
import com.shah.fse.entity.EngineerId;

@EnableScan
public interface EngineerSkillProfileRepository extends CrudRepository<EngineerDetails, EngineerId> {
	
	
	public static final Logger LOGGER = LoggerFactory.getLogger(EngineerSkillProfileRepository.class);
	
	List<EngineerDetails> findByUserId(String userId);
	
	List<EngineerDetails> findByUserName(String userName);
}
