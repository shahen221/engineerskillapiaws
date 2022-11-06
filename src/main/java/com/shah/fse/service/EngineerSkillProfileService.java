package com.shah.fse.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.shah.fse.dto.EngineerSkill;
import com.shah.fse.dto.EngineerSkillProfile;
import com.shah.fse.dto.SkillProfileResponse;
import com.shah.fse.dto.SkillSet;
import com.shah.fse.entity.EngineerDetails;
import com.shah.fse.entity.EngineerSkillset;
import com.shah.fse.exception.EngineerSkillProfileException;
import com.shah.fse.queue.ActiveMQMessageProducer;
import com.shah.fse.repository.EngineerSkillProfileRepository;
import com.shah.fse.util.TimeStampUtil;

@Service
public class EngineerSkillProfileService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EngineerSkillProfileService.class);
	
	@Autowired
	EngineerSkillProfileRepository engineerSkillProfileRepository;
	
	@Autowired
	ActiveMQMessageProducer activeMQMessageProducer;
	
	/**
	 * This method save Engineer Skill Profile details
	 * 
	 * @param engineerSkillProfile
	 * @return SkillProfileResponse
	 */
	public SkillProfileResponse saveEngineerSkillProfile(EngineerSkillProfile engineerSkillProfile) {
		LOGGER.info("Enter EngineerSkillProfileService::saveEngineerSkillProfile");
		SkillProfileResponse skillProfileResponse = null;
		EngineerDetails engineerDetails = prepareSaveProfileRequest(engineerSkillProfile);
		try {
			engineerDetails = this.engineerSkillProfileRepository.save(engineerDetails);
		}
		catch(Exception exception) {
			LOGGER.error("Error occured while saving engineer skill profile ", exception);
			throw new EngineerSkillProfileException(exception.getMessage());
		}
		skillProfileResponse = processAddProfileResponse(engineerDetails, engineerSkillProfile);
		// send AWS MQ message
		activeMQMessageProducer.sendProducerMessage(engineerDetails);
		LOGGER.info("Exit EngineerSkillProfileService::saveEngineerSkillProfile");
		return skillProfileResponse;
	}
	
	/**
	 * This method save Engineer Skill Profile details
	 * 
	 * @param engineerSkillProfile
	 * @return SkillProfileResponse
	 */
	public SkillProfileResponse updateEngineerSkillProfile(EngineerSkillProfile engineerSkillProfile, String userId) {
		LOGGER.info("Enter EngineerSkillProfileService::updateEngineerSkillProfile");
		SkillProfileResponse skillProfileResponse = null;
		EngineerDetails currentEngineerDetails = prepareUpdateProfileRequest(engineerSkillProfile, userId);
		try {
			currentEngineerDetails = this.engineerSkillProfileRepository.save(currentEngineerDetails);
		}
		catch(Exception exception) {
			LOGGER.error("Error occured while updating engineer skill profile ", exception);
			throw new EngineerSkillProfileException(exception.getMessage());
		}
		skillProfileResponse = processUpdateProfileResponse(currentEngineerDetails, engineerSkillProfile);
		// send AWS MQ message
		activeMQMessageProducer.sendProducerMessage(currentEngineerDetails);
		LOGGER.info("Exit EngineerSkillProfileService::updateEngineerSkillProfile");
		return skillProfileResponse;
	}
	
	/**
	 * This method prepare Engineer Profile Request for save this data.
	 * 
	 * @param engineerSkillProfile
	 * @return EngineerDetails
	 */
	private EngineerDetails prepareSaveProfileRequest(EngineerSkillProfile engineerSkillProfile) {
		LOGGER.info("Enter EngineerSkillProfileService::prepareSaveProfileRequest");
		EngineerDetails engineerDetails = new EngineerDetails();
		engineerDetails.setAssociateId(!ObjectUtils.isEmpty(engineerSkillProfile.getAssociateId())? engineerSkillProfile.getAssociateId() : "");
		engineerDetails.setUserName(!ObjectUtils.isEmpty(engineerSkillProfile.getUserName())? engineerSkillProfile.getUserName() : "");
		engineerDetails.setEmailAddress(!ObjectUtils.isEmpty(engineerSkillProfile.getEmailAddress())? engineerSkillProfile.getEmailAddress() : "");
		engineerDetails.setMobileNo(!ObjectUtils.isEmpty(engineerSkillProfile.getMobileNo())? engineerSkillProfile.getMobileNo() : "");
		if(engineerSkillProfile.getSkills() != null && !engineerSkillProfile.getSkills().isEmpty()) {
			engineerDetails.setSkillSets(new ArrayList<EngineerSkillset>());
			List<EngineerSkill> skills = engineerSkillProfile.getSkills();
			skills.stream().forEach( skill -> {
				EngineerSkillset engineerSkillset = new EngineerSkillset();
				engineerSkillset.setSkillScore(Integer.valueOf(skill.getSkillScore()));
				SkillSet skillSet = SkillSet.valueOf(skill.getSkillName());
				if(skillSet != null) {
					engineerSkillset.setSkillId(skillSet.getSkillId());
					engineerSkillset.setSkillType(skillSet.getSkillType());
					engineerSkillset.setSkillName(skillSet.getSkillName());
				}
				engineerSkillset.setAddedDate(new Timestamp(new Date().getTime()));
				engineerDetails.getSkillSets().add(engineerSkillset);
			});
		}
		engineerDetails.setAddedDate(new Timestamp(new Date().getTime()));
		LOGGER.info("Exit EngineerSkillProfileService::prepareSaveProfileRequest");
		return engineerDetails;
	}
	
	/**
	 * This method prepare response after save of Engineer Profile.
	 * 
	 * @param engineerDetails
	 * @param engineerSkillProfile
	 * @return SkillProfileResponse
	 */
	private SkillProfileResponse processAddProfileResponse(EngineerDetails engineerDetails, EngineerSkillProfile engineerSkillProfile) {
		LOGGER.info("Enter EngineerSkillProfileService::processAddProfileResponse");
		SkillProfileResponse response = new SkillProfileResponse();
		if(engineerDetails.getUserId() != null) {
			response.setStatus("success");
			response.setStatusText("User Skill Profile Added Successfully");
			response.setUserId(engineerDetails.getUserId());
			engineerSkillProfile.setProfileStatus("Added");
			response.setEngineerSkillProfile(engineerSkillProfile);
		}
		LOGGER.info("Exit EngineerSkillProfileService::processAddProfileResponse");
		return response;
	}
	
	
	/**
	 * This method prepare Engineer Profile Request to update engineer profile.
	 * 
	 * @param engineerSkillProfile
	 * @return EngineerDetails
	 */
	private EngineerDetails prepareUpdateProfileRequest(EngineerSkillProfile engineerSkillProfile, String userId) {
		LOGGER.info("Enter EngineerSkillProfileService::prepareUpdateProfileRequest");
		EngineerDetails currentEngineerDetails = null;
		try {
			currentEngineerDetails = this.engineerSkillProfileRepository.findByUserName(userId).get(0);
		}
		catch(NoSuchElementException nseException) {
			throw new EngineerSkillProfileException("Invalid userid, please provide valid user id.");
		}
		if(!ObjectUtils.isEmpty(currentEngineerDetails)) {// if valid user id
			//currentEngineerDetails.setUserId(Integer.parseInt(userId));
			if(!ObjectUtils.isEmpty(engineerSkillProfile.getAssociateId()) || !ObjectUtils.isEmpty(engineerSkillProfile.getUserName()) 
					|| !ObjectUtils.isEmpty(engineerSkillProfile.getEmailAddress()) || !ObjectUtils.isEmpty(engineerSkillProfile.getMobileNo())) {
				// check if profile added date is greater than 10 days or updated date is greater than 10 days in engineer_details table
				checkProfileUpdateAllowed(engineerSkillProfile, currentEngineerDetails);
			}
			// check skill set updates
			if(engineerSkillProfile.getSkills() != null && !engineerSkillProfile.getSkills().isEmpty()) {
				//engineerDetails.setSkillSets(new HashSet<EngineerSkillset>());
				List<EngineerSkill> skills = engineerSkillProfile.getSkills();
				List<EngineerSkillset> currentSkillSets = currentEngineerDetails.getSkillSets();
				skills.stream().forEach( skill -> {
					SkillSet skillSet = SkillSet.valueOf(skill.getSkillName());
					 Optional<EngineerSkillset> matchedEngineerSkillset = currentSkillSets.stream().filter(engineerSkillset -> (engineerSkillset.getSkillName().equalsIgnoreCase(skillSet.getSkillName()))).findFirst();
					 EngineerSkillset updatedEngineerSkillset = null;
					 if(matchedEngineerSkillset.isPresent() && matchedEngineerSkillset.get() != null) {
						 updatedEngineerSkillset = matchedEngineerSkillset.get();
						 updatedEngineerSkillset.setSkillScore(Integer.valueOf(skill.getSkillScore()));
						updatedEngineerSkillset.setUpdatedDate(new Timestamp(new Date().getTime()));
					}
					 //currentEngineerDetails.getSkillSets().add(engineerSkillset);
				});
			}
			currentEngineerDetails.setUpdatedDate(new Timestamp(new Date().getTime()));
		}
		LOGGER.info("Exit EngineerSkillProfileService::prepareUpdateProfileRequest");
		return currentEngineerDetails;
	}

	/**
	 * This method checks if user profile details last updated or added 10 days from current date.
	 * 
	 * @param engineerSkillProfile
	 * @param currentEngineerDetails
	 */
	private void checkProfileUpdateAllowed(EngineerSkillProfile engineerSkillProfile,
			EngineerDetails currentEngineerDetails) {
		LOGGER.info("Enter EngineerSkillProfileService::checkProfileUpdateAllowed");
		Calendar addedUpdatedCalendar = null;
		if(!ObjectUtils.isEmpty(currentEngineerDetails.getUpdatedDate())) {
			addedUpdatedCalendar = TimeStampUtil.getCalendarFromTimeStamp(currentEngineerDetails.getUpdatedDate());
		}
		else if(!ObjectUtils.isEmpty(currentEngineerDetails.getAddedDate())) {
			addedUpdatedCalendar = TimeStampUtil.getCalendarFromTimeStamp(currentEngineerDetails.getAddedDate());
		}
		Calendar allowedCalendar = TimeStampUtil.getCalendarBeforeTenDays();
		if(allowedCalendar.after(addedUpdatedCalendar)) {
			currentEngineerDetails.setAssociateId(!ObjectUtils.isEmpty(engineerSkillProfile.getAssociateId())? engineerSkillProfile.getAssociateId() : currentEngineerDetails.getAssociateId());
			currentEngineerDetails.setUserName(!ObjectUtils.isEmpty(engineerSkillProfile.getUserName())? engineerSkillProfile.getUserName() : currentEngineerDetails.getUserName());
			currentEngineerDetails.setEmailAddress(!ObjectUtils.isEmpty(engineerSkillProfile.getEmailAddress())? engineerSkillProfile.getEmailAddress() : currentEngineerDetails.getEmailAddress());
			currentEngineerDetails.setMobileNo(!ObjectUtils.isEmpty(engineerSkillProfile.getMobileNo())? engineerSkillProfile.getMobileNo() : currentEngineerDetails.getMobileNo());
			//currentEngineerDetails.setUpdatedDate(new Timestamp(new Date().getTime()));
		}
		else {
			throw new EngineerSkillProfileException("Update of profile must be allowed only after 10 days of adding profile or last change", currentEngineerDetails.getUserId());
		}
		LOGGER.info("Exit EngineerSkillProfileService::checkProfileUpdateAllowed");
	}
	
	/**
	 * This method prepare response after update of Engineer Profile.
	 * 
	 * @param engineerDetails
	 * @param engineerSkillProfile
	 * @return SkillProfileResponse
	 */
	private SkillProfileResponse processUpdateProfileResponse(EngineerDetails engineerDetails, EngineerSkillProfile engineerSkillProfile) {
		LOGGER.info("Enter EngineerSkillProfileService::processUpdateProfileResponse");
		SkillProfileResponse response = new SkillProfileResponse();
		if(engineerDetails.getUserId() != null) {
			engineerSkillProfile.setProfileStatus("Updated");
			engineerSkillProfile.setAssociateId(engineerDetails.getAssociateId());
			response.setStatus("success");
			response.setStatusText("User Skill Profile Updated Successfully");
			response.setUserId(engineerDetails.getUserId());
			response.setEngineerSkillProfile(engineerSkillProfile);
		}
		LOGGER.info("Exit EngineerSkillProfileService::processUpdateProfileResponse");
		return response;
	}
}
