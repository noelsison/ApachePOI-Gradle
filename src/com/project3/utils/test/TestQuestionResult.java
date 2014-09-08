package com.project3.utils.test;

import java.util.ArrayList;
import java.util.List;

public class TestQuestionResult {
	private String string;
	private boolean exists = false;
	private List<TestQuestionProperty> properties = new ArrayList<TestQuestionProperty>();
	
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

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		//		sb.append("Text\tExists\tProperties\n");
		sb.append(string).append("\t").append(exists).append("\t");
		for (TestQuestionProperty property : properties) {
			sb.append(property.getName()).append("=").append(property.getValue()).append("\t").append(property.getCorrect()).append("\t").append(property.getTotal()).append("\n\t\t");
		}
		//		sb.append("\n");
		return sb.toString();
	}
}
