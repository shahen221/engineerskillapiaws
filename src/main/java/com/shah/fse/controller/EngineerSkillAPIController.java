package com.shah.fse.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shah.fse.dto.EngineerSkillProfile;
import com.shah.fse.dto.SkillProfileResponse;
import com.shah.fse.exception.EngineerSkillProfileException;
import com.shah.fse.service.EngineerSkillProfileService;
import com.shah.fse.validator.EngineerSkillProfileValidator;


@RestController
@RequestMapping("/skill-tracker/api/v1/engineer")
public class EngineerSkillAPIController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EngineerSkillAPIController.class);

	@Autowired
	EngineerSkillProfileService engineerSkillProfileService;
	
	@PostMapping("/add-profile")
	@CrossOrigin(origins = "*")
	public SkillProfileResponse addEngineerSkillProfile(@RequestBody(required = false) EngineerSkillProfile engineerSkillProfile) {
		LOGGER.info("Enter EngineerSkillAPIController::addEngineerSkillProfile");
		SkillProfileResponse skillProfileResponse = null;
		skillProfileResponse = EngineerSkillProfileValidator.validateNewEngineerProfile(engineerSkillProfile);
		if(ObjectUtils.isEmpty(skillProfileResponse)) {
			// validate skill expertise level
			EngineerSkillProfileValidator.validateEngineerSkillScore(engineerSkillProfile);
			skillProfileResponse = this.engineerSkillProfileService.saveEngineerSkillProfile(engineerSkillProfile);
		}
		LOGGER.info("Exit EngineerSkillAPIController::addEngineerSkillProfile");
		return skillProfileResponse;
	}
	
	@PutMapping("/update-profile/{userId}")
	public SkillProfileResponse updateEngineerSkillProfile(@RequestBody(required = false) EngineerSkillProfile engineerSkillProfile, @PathVariable("userId") String userId) {
		LOGGER.info("Enter EngineerSkillAPIController::updateEngineerSkillProfile");
		SkillProfileResponse skillProfileResponse = null;
		skillProfileResponse = EngineerSkillProfileValidator.validateUpdatedEngineerProfile(engineerSkillProfile);
		if(ObjectUtils.isEmpty(skillProfileResponse)) {
			// validate skill expertise level
			EngineerSkillProfileValidator.validateEngineerSkillScore(engineerSkillProfile);
			skillProfileResponse = this.engineerSkillProfileService.updateEngineerSkillProfile(engineerSkillProfile, userId);
		}
		LOGGER.info("Exit EngineerSkillAPIController::updateEngineerSkillProfile");
		return skillProfileResponse;
	}

	@ExceptionHandler(value = EngineerSkillProfileException.class)
	public SkillProfileResponse handleError(EngineerSkillProfileException engineerSkillProfileException) {
		LOGGER.info("Enter EngineerSkillAPIController::handleError");
		SkillProfileResponse skillProfileResponse = new SkillProfileResponse();
		skillProfileResponse.setStatus("Failure");
		skillProfileResponse.setStatusText("Failure");
		skillProfileResponse.getErrorMessages().add(engineerSkillProfileException.getErrorMessage());
		skillProfileResponse.setUserId(engineerSkillProfileException.getUserId());
		LOGGER.info("Exit EngineerSkillAPIController::handleError");
		return skillProfileResponse;
	}
}
