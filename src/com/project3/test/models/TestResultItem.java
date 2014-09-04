package com.project3.test.models;

import java.util.HashMap;
import java.util.Map;

public class TestResultItem {

	private String string;
//	private boolean exists;
//	private List<TestResultProperty> properties;
	private Map<String, TestResultProperty> properties;

	public TestResultItem(String s) {
		this.string = s;
//		this.exists = false;
		this.properties = new HashMap<String, TestResultProperty>();
	}
	
	public boolean isEmpty() {
		return properties.isEmpty();
	}
//
//	public boolean isExists() {
//		return exists;
//	}
//
//	public void setExists(boolean exists) {
//		this.exists = exists;
//	}

//	public List<TestResultProperty> getProperties() {
//		return properties;
//	}
//
//	public void setProperties(List<TestResultProperty> properties) {
//		this.properties = properties;
//	}

	public TestResultProperty getProperty(String key) {
		if (properties.containsKey(key)) {
			return properties.get(key);
		} else {
			return new TestResultProperty("");
		}
	}
	
	public void setProperty(String name, String value) {
		TestResultProperty property = new TestResultProperty(name, value);
		setProperty(property);
	}
	
	public void setProperty(TestResultProperty property) {
		properties.put(property.getName(), property);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		// sb.append("Text\tExists\tProperties\n");
		sb.append(string).append("\t").append("").append("\t");
//		for (TestResultProperty property : properties) {
		for ( Map.Entry<String, TestResultProperty> entry : properties.entrySet()) {
			TestResultProperty property = entry.getValue();
			sb.append(property.getName()).append("=")
					.append(property.getValue()).append("\t")
					.append(property.getNumCorrect()).append("\t")
					.append(property.getTotal()).append("\n\t\t");
		}
		// sb.append("\n");
		return sb.toString();
	}
}
