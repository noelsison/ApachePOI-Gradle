package com.project3.utils.poi;

public class ResultModel {
	private String text;
	private String property;
	private String value;
	private int rawScore;
	private int totalScore;
	private boolean exists;
	
	public ResultModel(String text, String property, String value, int rawScore, int totalScore) {
		this.setText(text);
		this.setProperty(property);
		this.setValue(value);
		this.setRawScore(rawScore);
		this.setTotalScore(totalScore);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getRawScore() {
		return rawScore;
	}

	public void setRawScore(int rawScore) {
		this.rawScore = rawScore;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public boolean correct() {
		return rawScore == totalScore;
	}

	public boolean isExists() {
		return exists;
	}

	public void setExists(boolean exists) {
		this.exists = exists;
	}
	
	
}
