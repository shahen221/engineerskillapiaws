package com.shah.fse.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.shah.fse.converter.SkillSetListConverter;
import com.shah.fse.converter.TimestampConverter;

@SuppressWarnings("serial")
@DynamoDBTable(tableName = "engineer_details")
public class EngineerDetails implements Serializable {
	
	@Id
	private EngineerId engineerId;
	
	//private String userId;
	
	private String associateId;
	
	//private String userName;
	
	private String emailAddress;
	
	private String mobileNo;
	
	private Timestamp addedDate;
	
	private Timestamp updatedDate;
	
	private List<EngineerSkillset> skillSets = new ArrayList<>();
	
	public EngineerDetails() {
		super();
	}

	public EngineerDetails(EngineerId engineerId) {
		super();
		this.engineerId = engineerId;
	}

	@DynamoDBHashKey
	@DynamoDBAutoGeneratedKey
	public String getUserId() {
		return engineerId != null ? engineerId.getUserId() : null;
	}

	public void setUserId(String userId) {
		if(engineerId == null) {
			engineerId = new EngineerId();
		}
		engineerId.setUserId(userId);
	}
	
	@DynamoDBAttribute
	public String getAssociateId() {
		return associateId;
	}

	public void setAssociateId(String associateId) {
		this.associateId = associateId;
	}
	
	@DynamoDBRangeKey
	@DynamoDBAttribute
	public String getUserName() {
		return engineerId != null ? engineerId.getUserName() : null;
	}

	public void setUserName(String userName) {
		if(engineerId == null) {
			engineerId = new EngineerId();
		}
		engineerId.setUserName(userName);
	}
	
	@DynamoDBAttribute
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	@DynamoDBAttribute
	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	
	@DynamoDBTypeConverted(converter = TimestampConverter.class)
	public Timestamp getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Timestamp addedDate) {
		this.addedDate = addedDate;
	}
	
	@DynamoDBTypeConverted(converter = TimestampConverter.class)
	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	@DynamoDBTypeConverted(converter = SkillSetListConverter.class)
	public List<EngineerSkillset> getSkillSets() {
		return skillSets;
	}

	public void setSkillSets(List<EngineerSkillset> skillSets) {
		this.skillSets = skillSets;
	}
	
}
