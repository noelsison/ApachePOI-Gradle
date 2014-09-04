package com.project3.test.models;

import java.util.HashMap;
import java.util.Map;

public class TestResultItem {

	private String string;
	private boolean exists;
	private Map<String, TestResultProperty> properties;

	public TestResultItem(String s) {
		this.string = s;
		this.exists = false;
		this.properties = new HashMap<String, TestResultProperty>();
	}
	
	public boolean isEmpty() {
		return !exists && properties.isEmpty();
	}

	public boolean exists() {
		return exists;
	}

	public void setExists(boolean exists) {
		this.exists = exists;
	}

	public TestResultProperty getProperty(String key) {
		if (properties.containsKey(key)) {
			return properties.get(key);
		} else {
			return new TestResultProperty("");
		}
	}

	public void setProperties(Map<String, TestResultProperty> properties) {
		this.properties = properties;
	}

	public void setProperty(String name, String value) {
		TestResultProperty property = new TestResultProperty(name, value);
		setProperty(property);
	}
	
	public void setProperty(TestResultProperty property) {
		properties.put(property.getName(), property);
	}
	
	public boolean hasProperty(String name) {
		return properties.containsKey(name);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(string).append("\t").append(exists).append("\t");
		
		for ( Map.Entry<String, TestResultProperty> entry : properties.entrySet()) {
			TestResultProperty property = entry.getValue();
			sb.append(property.getName()).append("=")
					.append(property.getValue()).append("\t")
					.append(property.getScore()).append("\t")
					.append(property.getTotal()).append("\n\t\t");
		}
		
		return sb.toString();
	}
}
