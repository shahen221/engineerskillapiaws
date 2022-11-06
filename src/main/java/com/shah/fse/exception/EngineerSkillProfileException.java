package com.shah.fse.exception;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
public class EngineerSkillProfileException extends RuntimeException {
	
	private String errorMessage;
	
	private String userId;
	
	private Throwable error;

	public EngineerSkillProfileException(String errorMessage, Throwable error) {
		super();
		this.errorMessage = errorMessage;
		this.error = error;
	}

	public EngineerSkillProfileException(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}
	
	public EngineerSkillProfileException(String errorMessage, String userId) {
		super();
		this.errorMessage = errorMessage;
		this.userId = userId;
	}
	
	
}
