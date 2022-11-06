package com.shah.fse.converter;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shah.fse.entity.EngineerSkillset;

public class SkillSetListConverter implements DynamoDBTypeConverter<String, List<EngineerSkillset>> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SkillSetListConverter.class);
	
	@Override
	public String convert(List<EngineerSkillset> object) {
		List<EngineerSkillset> skillSets = (List<EngineerSkillset>) object;
	        ObjectMapper objectMapper = new ObjectMapper();
	        String jsonStr = null;
	        try {
	            jsonStr = objectMapper.writeValueAsString(skillSets);
	        } catch (JsonProcessingException e) {
	        	LOGGER.error(e.getMessage());
	        }
	
	      return jsonStr;
	}

	@Override
	public List<EngineerSkillset> unconvert(String object) {
		 ObjectMapper objectMapper = new ObjectMapper();
		 List<EngineerSkillset> skillSets = new ArrayList<>();
	        try {
	        	skillSets = objectMapper.readerForListOf(EngineerSkillset.class).readValue(object);
	        } catch (Exception e) {
	        	LOGGER.error(e.getMessage());
	        }
	        return skillSets;
	}


}
