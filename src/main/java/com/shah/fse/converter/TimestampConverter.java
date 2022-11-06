package com.shah.fse.converter;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TimestampConverter implements DynamoDBTypeConverter<String, Timestamp> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TimestampConverter.class);
	
	@Override
	public String convert(Timestamp object) {
		Timestamp inputTimestamp = (Timestamp) object;
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = null;
        try {
            jsonStr = objectMapper.writeValueAsString(inputTimestamp);
        } catch (JsonProcessingException e) {
        	LOGGER.error(e.getMessage());
        }
      return jsonStr;
	}

	@Override
	public Timestamp unconvert(String object) {
	 ObjectMapper objectMapper = new ObjectMapper();
	 Timestamp timestamp = null;
        try {
        	timestamp = objectMapper.readValue(object, Timestamp.class);
        } catch (Exception e) {
        	LOGGER.error(e.getMessage());
        }
        return timestamp;
	}

}
