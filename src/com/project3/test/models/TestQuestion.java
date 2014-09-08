package com.project3.test.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestQuestion {
	
	private String questionId;
	private TestQuestionType type;
	private ArrayList<String> strings;
	private Map<String, String> properties;
	
	public TestQuestion (String name, TestQuestionType type) {
		this.questionId = name;
		this.type = type;
		this.strings = new ArrayList<String>();
		this.properties = new HashMap<String, String>();
	}
	
	public ArrayList<String> getStrings() {
		return strings;
	}

	public void setStrings(ArrayList<String> strings) {
		this.strings = strings;
	}

	public Map<String, String> getProperties() {
		return properties;
	}
	
	public void addString(String string) {
		this.strings.add(string);
	}
	
	public boolean hasProperty(String key) {
		return this.properties.containsKey(key);
	}
	
	public String getProperty(String key) {
		return this.properties.get(key);
	}
	
	public void setProperty(String key, String value) {
		this.properties.put(key, value);
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(questionId).append("\t").append(type).append("\t").append("\t").append(strings).append("\t");
		for (Map.Entry<String, String> entry : properties.entrySet()) {
			sb.append(entry.getKey()).append("=").append(entry.getValue()).append("\t");
		}
		
		return sb.toString();
	}

	public String getQuestionId() {
		return questionId;
	}

	public TestQuestionType getType() {
		return type;
	}
}
