package com.project3.test.models;

import java.util.ArrayList;
import java.util.List;

public class TestResultItem {
	private String string;
	private boolean exists = false;
	private List<TestResultProperty> properties = new ArrayList<TestResultProperty>();
	
	public TestResultItem (String s) {
		this.string = s;
	}
	
	public boolean isExists() {
		return exists;
	}
	public void setExists(boolean exists) {
		this.exists = exists;
	}
	public List<TestResultProperty> getProperties() {
		return properties;
	}
	public void setProperties(List<TestResultProperty> properties) {
		this.properties = properties;
	}
	public TestResultProperty getProperty(String s) {
		for (TestResultProperty tqp: properties) {
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
		for (TestResultProperty property : properties) {
			sb.append(property.getName()).append("=").append(property.getValue()).append("\t").append(property.getCorrect()).append("\t").append(property.getTotal()).append("\n\t\t");
		}
		//		sb.append("\n");
		return sb.toString();
	}
}
