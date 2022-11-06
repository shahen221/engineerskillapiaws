package com.shah.fse.validator;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import com.shah.fse.dto.EngineerSkillProfile;
import com.shah.fse.dto.SkillProfileResponse;
import com.shah.fse.exception.EngineerSkillProfileException;

public class EngineerSkillProfileValidator {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EngineerSkillProfileValidator.class);

	/**
	 * This method validate Engineer Skill Score
	 * 
	 * @param engineerSkillProfile
	 */
	public static void validateEngineerSkillScore(EngineerSkillProfile engineerSkillProfile) {
		LOGGER.info("Enter EngineerSkillProfileValidator::validateEngineerSkillScore");
		if(engineerSkillProfile.getSkills() != null && !engineerSkillProfile.getSkills().isEmpty()) {
			engineerSkillProfile.getSkills().forEach(engineerSkill -> {
				if(ObjectUtils.isEmpty(engineerSkill.getSkillScore())) {
					throw new EngineerSkillProfileException("Skill expertise level cannot be empty");
				}
				else {
					try {
						int skillScore = Integer.parseInt(engineerSkill.getSkillScore());
						if(skillScore < 0 || skillScore > 20) {
							throw new EngineerSkillProfileException("Skill expertise level should be between (0-20)");
						}
					}
					catch(NumberFormatException nfException) {
						throw new EngineerSkillProfileException("Skill expertise level should be numeric and in between (0-20)");
					}
				}
			});
		}
		LOGGER.info("Exit EngineerSkillProfileValidator::validateEngineerSkillScore");
	}

	/**
	 * This method validate user provide Engineer Profile data
	 * 
	 * @param engineerSkillProfile
	 * @return SkillProfileErrorResponse
	 */
	public static SkillProfileResponse validateNewEngineerProfile(EngineerSkillProfile engineerSkillProfile) {
		LOGGER.info("Enter EngineerSkillProfileValidator::validateNewEngineerProfile");
		SkillProfileResponse skillProfileResponse = new SkillProfileResponse();
		//validate username
		if(ObjectUtils.isEmpty(engineerSkillProfile.getUserName())) {
			skillProfileResponse.getErrorMessages().add("UserName cannot be empty, please provide a valid user name with minimum 5 characters and upto maximum 30 characters");
		}
		else if(!ObjectUtils.isEmpty(engineerSkillProfile.getUserName()) && (engineerSkillProfile.getUserName().length() <5 || engineerSkillProfile.getUserName().length() >30)) {
			skillProfileResponse.getErrorMessages().add("Provide UserName with minimum 5 characters and upto maximum 30 characters");
		}
		// validate associate id
		if(ObjectUtils.isEmpty(engineerSkillProfile.getAssociateId())) {
			skillProfileResponse.getErrorMessages().add("AssociateId cannot be empty, please provide a valid AssociateId with minimum 5 characters and upto maximum 30 characters and must start with 'CTS'");
		}
		else if(!ObjectUtils.isEmpty(engineerSkillProfile.getAssociateId()) && (engineerSkillProfile.getAssociateId().length() <5 || engineerSkillProfile.getAssociateId().length() >30 || !engineerSkillProfile.getAssociateId().startsWith("CTS"))) {
			skillProfileResponse.getErrorMessages().add("Please provide a valid AssociateId with minimum 5 characters and upto maximum 30 characters and must start with 'CTS'");
		}
		//validate email address
		if(ObjectUtils.isEmpty(engineerSkillProfile.getEmailAddress())) {
			skillProfileResponse.getErrorMessages().add("EmailAddress cannot be empty, please provide a valid email address");
		}
		else if(!ObjectUtils.isEmpty(engineerSkillProfile.getEmailAddress())) {
			String emailPattern = "^(.+)@(\\S+)$";
			boolean validEmail = Pattern.compile(emailPattern)
		      .matcher(engineerSkillProfile.getEmailAddress())
		      .matches();
			if(!validEmail) {
				skillProfileResponse.getErrorMessages().add("Please provide a valid email address");
			}
		}
		//validate mobile no
		if(ObjectUtils.isEmpty(engineerSkillProfile.getMobileNo())) {
			skillProfileResponse.getErrorMessages().add("Mobile No cannot be empty, please provide a valid 10 digit mobile number");
		}
		else if(!ObjectUtils.isEmpty(engineerSkillProfile.getMobileNo())) {
			String mobileNoPattern = "^\\d{10}$";
			boolean validMobileNo = Pattern.compile(mobileNoPattern)
		      .matcher(engineerSkillProfile.getMobileNo())
		      .matches();
			if(!validMobileNo) {
				skillProfileResponse.getErrorMessages().add("Please provide a valid 10 digit mobile number");
			}
		}
		if(!skillProfileResponse.getErrorMessages().isEmpty()) {
			skillProfileResponse.setStatus("Failure");
			skillProfileResponse.setStatusText("Client Validations Failed");
		}
		else {
			skillProfileResponse = null;
		}
		LOGGER.info("Exit EngineerSkillProfileValidator::validateNewEngineerProfile");
		return skillProfileResponse;
	}
	
	/**
	 * This method validate user provide Engineer Profile data
	 * 
	 * @param engineerSkillProfile
	 * @return SkillProfileErrorResponse
	 */
	public static SkillProfileResponse validateUpdatedEngineerProfile(EngineerSkillProfile engineerSkillProfile) {
		LOGGER.info("Enter EngineerSkillProfileValidator::validateUpdatedEngineerProfile");
		SkillProfileResponse skillProfileResponse = new SkillProfileResponse();
		//validate username
		if(!ObjectUtils.isEmpty(engineerSkillProfile.getUserName()) && (engineerSkillProfile.getUserName().length() <5 || engineerSkillProfile.getUserName().length() >30)) {
			skillProfileResponse.getErrorMessages().add("Provide UserName with minimum 5 characters and upto maximum 30 characters");
		}
		// validate associate id
		if(!ObjectUtils.isEmpty(engineerSkillProfile.getAssociateId()) && (engineerSkillProfile.getAssociateId().length() <5 || engineerSkillProfile.getAssociateId().length() >30 || !engineerSkillProfile.getAssociateId().startsWith("CTS"))) {
			skillProfileResponse.getErrorMessages().add("Please provide a valid AssociateId with minimum 5 characters and upto maximum 30 characters and must start with 'CTS'");
		}
		//validate email address
		if(!ObjectUtils.isEmpty(engineerSkillProfile.getEmailAddress())) {
			String emailPattern = "^(.+)@(\\S+)$";
			boolean validEmail = Pattern.compile(emailPattern)
		      .matcher(engineerSkillProfile.getEmailAddress())
		      .matches();
			if(!validEmail) {
				skillProfileResponse.getErrorMessages().add("Please provide a valid email address");
			}
		}
		//validate mobile no
		if(!ObjectUtils.isEmpty(engineerSkillProfile.getMobileNo())) {
			String mobileNoPattern = "^\\d{10}$";
			boolean validMobileNo = Pattern.compile(mobileNoPattern)
		      .matcher(engineerSkillProfile.getMobileNo())
		      .matches();
			if(!validMobileNo) {
				skillProfileResponse.getErrorMessages().add("Please provide a valid 10 digit mobile number");
			}
		}
		if(!skillProfileResponse.getErrorMessages().isEmpty()) {
			skillProfileResponse.setStatus("Failure");
			skillProfileResponse.setStatusText("Client Validations Failed");
		}
		else {
			skillProfileResponse = null;
		}
		LOGGER.info("Exit EngineerSkillProfileValidator::validateUpdatedEngineerProfile");
		return skillProfileResponse;
	}
}
