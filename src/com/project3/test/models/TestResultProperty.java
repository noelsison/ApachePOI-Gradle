package com.project3.test.models;

public class TestResultProperty {

	private String name, value;
	private int score, total;

	public TestResultProperty(String name) {
		this.name = name;
	}
	
	public TestResultProperty(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int numCorrect) {
		this.score = numCorrect;
	}
	
	public void addScore(int increment) {
		this.score += increment;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
	public void addTotal(int increment) {
		this.total += increment;
	}
}
