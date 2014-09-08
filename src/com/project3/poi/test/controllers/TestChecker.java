package com.project3.poi.test.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.project3.poi.DocumentPropertyChecker;
import com.project3.test.models.TestQuestion;
import com.project3.test.models.TestResultItem;

public class TestChecker {
	
	public static void checkAllQuestions(XWPFDocument docx, List<TestQuestion> testQuestionList) {
		System.out.println("Text\tExists\tProperties\tCorrect\tTotal");
		for (TestQuestion o: testQuestionList) {
			Map<String, TestResultItem> resultMap = checkQuestion(docx, o); 
			System.out.println(resultMapToString(resultMap));			
		}
	}
	
	public static Map<String, TestResultItem> checkQuestion(XWPFDocument docx, TestQuestion o) {
		Map<String, TestResultItem> resultMap = new HashMap<String, TestResultItem>();
		
		switch (o.getType()) {
		case RUN:
			resultMap = DocumentPropertyChecker.checkRunPropertiesOfParagraphs(docx.getParagraphs(), o.getStrings(), o.getProperties());
			break;
		case PARAGRAPH:
			resultMap = DocumentPropertyChecker.checkPropertiesOfParagraphs(docx.getParagraphs(), o.getStrings(),  o.getProperties());
			break;
		case ALL_PARAGRAPHS:
			resultMap = DocumentPropertyChecker.checkPropertiesOfAllParagraphs(docx.getParagraphs(),  o.getProperties());
			break;
		case MATCH:
			resultMap = DocumentPropertyChecker.checkIfStringExistsInParagraphs(docx.getParagraphs(), o.getStrings());
			break;
		case DOCUMENT:
			resultMap = DocumentPropertyChecker.checkPropertiesOfDocument(docx, o.getProperties());
			break;
		case PICTURE:
		  resultMap = DocumentPropertyChecker.checkPropertiesOfPictures(docx.getAllPictures(), o.getStrings(), o.getProperties());
            break;
		case TABLE_CONTENT:
		    resultMap = DocumentPropertyChecker.checkContentsOfTable(docx.getTables().get(0), o.getStrings());
		    break;
		default:
			break;
		}
		return resultMap;
	}
	
	private static String resultMapToString(Map<String, TestResultItem> resultMap) {
		StringBuffer result = new StringBuffer();
		
		for (Map.Entry<String, TestResultItem> entry : resultMap.entrySet()) {
			result.append(entry.getValue().toString()).append("\n");
		}
		
		return result.toString();
	}
}
