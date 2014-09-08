package com.project3.test.models;

public class TestResultProperty {
	private String name, value;
	private int correct = 0, 
			    total = 0;

	public TestResultProperty (String name) {
		this.name = name;
	}

	public TestResultProperty (String name, String value) {
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
	@Override
  public String toString() {
    return "[name=" + name + ", value=" + value + ", correct=" + correct
        + ", total=" + total + "]";
  }
}
