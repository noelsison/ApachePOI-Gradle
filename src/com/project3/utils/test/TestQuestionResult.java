package com.project3.utils.test;

import java.util.ArrayList;
import java.util.List;

public class TestQuestionResult {
	private String string;
	private boolean exists = false;
	private List<TestQuestionProperty> properties = new ArrayList();
	
	public TestQuestionResult (String s) {
		this.string = s;
	}
	
	public boolean isExists() {
		return exists;
	}
	public void setExists(boolean exists) {
		this.exists = exists;
	}
	public List<TestQuestionProperty> getProperties() {
		return properties;
	}
	public void setProperties(List<TestQuestionProperty> properties) {
		this.properties = properties;
	}
	public TestQuestionProperty getProperty(String s) {
		for (TestQuestionProperty tqp: properties) {
			if (tqp.getName().equalsIgnoreCase(s)) {
				return tqp;
			}
		}
		return null;
	}
	public String toString() {
	    return "String: " + this.string + ", Exists: " + this.exists + ", Properties: " + this.properties;
	}
}
