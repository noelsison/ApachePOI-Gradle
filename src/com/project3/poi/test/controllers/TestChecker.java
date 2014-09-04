package com.project3.poi.test.controllers;

import java.util.ArrayList;
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
//			Map<String, TestResultItem> = checkQuestion(docx, o); 
			List<TestResultItem> results = checkQuestion(docx, o);
			System.out.println(resultsToString(results));
//			System.out.println(resultMapToString(resultMap));			
		}
	}
	
	public static List<TestResultItem> checkQuestion(XWPFDocument docx, TestQuestion question) {
		List<TestResultItem> resultMap = new ArrayList<TestResultItem>();
		
		switch (question.getType()) {
		case RUN:
			resultMap = DocumentPropertyChecker.checkRunQuestion(docx.getParagraphs(), question);
			break;
//		case PARAGRAPH:
//			resultMap = DocumentPropertyChecker.checkPropertiesOfParagraphs(docx.getParagraphs(), o.getStrings(),  o.getProperties());
//			break;
//		case ALL_PARAGRAPHS:
//			resultMap = DocumentPropertyChecker.checkPropertiesOfAllParagraphs(docx.getParagraphs(),  o.getProperties());
//			break;
		case MATCH:
			resultMap = DocumentPropertyChecker.checkStringsInParagraphs(docx.getParagraphs(), question);
//			break;
//		case DOCUMENT:
//			resultMap = DocumentPropertyChecker.checkPropertiesOfDocument(docx, o.getProperties());
//			break;
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
	
	private static String resultsToString(List<TestResultItem> results) {
		StringBuffer resultString = new StringBuffer();
		
		for (TestResultItem resultItem : results) {
			resultString.append(resultItem.toString()).append("\n");
		}
		
		return resultString.toString();
	}
}
