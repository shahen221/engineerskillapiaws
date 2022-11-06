package com.shah.fse.config;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

@Configuration
@EnableDynamoDBRepositories(basePackages = "com.shah.fse.repository")
public class DynamoDBConfig {

	@Value("${amazon.dynamodb.endpoint.url}")
	private String amazonDynamoDBEndpoint;
	
	@Value("${amazon.access.key}")
	private String amazonDynamoDBAccessKey;
	
	@Value("${amazon.access.secretkey}")
	private String amazonDynamoDBSecretKey;
	
	@SuppressWarnings("deprecation")
	@Bean
	public AmazonDynamoDB amazonDynamoDB() {
		AmazonDynamoDB amazonDynamoDB = new AmazonDynamoDBClient(amazonAWSCredentials());
		if(!ObjectUtils.isEmpty(amazonDynamoDBEndpoint)) {
			amazonDynamoDB.setEndpoint(amazonDynamoDBEndpoint);
		}
		return amazonDynamoDB;
	}
	
	@Bean
	public AWSCredentials amazonAWSCredentials() {
		return new BasicAWSCredentials(amazonDynamoDBAccessKey, amazonDynamoDBSecretKey);
	}
}
