package com.moviebooking.auth.dto;

public class ResetDto {

	private String username;
	private String newPassword;
	private String secQuestion;
	private String secAnswer;

	public ResetDto(String username, String newPassword, String secQuestion, String secAnswer) {
		this.username = username;
		this.newPassword = newPassword;
		this.secQuestion = secQuestion;
		this.secAnswer = secAnswer;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getSecQuestion() {
		return secQuestion;
	}

	public void setSecQuestion(String secQuestion) {
		this.secQuestion = secQuestion;
	}

	public String getSecAnswer() {
		return secAnswer;
	}

	public void setSecAnswer(String secAnswer) {
		this.secAnswer = secAnswer;
	}

}
