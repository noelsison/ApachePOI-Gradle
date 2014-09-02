package com.project3.utils.test;

public class TestQuestionProperty {
	private String name, value;
	private int correct = 0, 
			    total = 0;
	public TestQuestionProperty (String name) {
		this.name = name;
	}
	public TestQuestionProperty (String name, String value) {
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
	public int getCorrect() {
		return correct;
	}
	public void setCorrect(int correct) {
		this.correct = correct;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
}
