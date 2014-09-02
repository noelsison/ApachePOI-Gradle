package com.project3.utils.test;

import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.project3.utils.poi.DocumentPropertyChecker;
import com.project3.utils.poiold.DocumentPropertyCheckerOld;

public class TestChecker {
	public static void checkAllQuestions(XWPFDocument docx, List<TestQuestion> testQuestionList) {
		for (TestQuestion o: testQuestionList) {
			System.out.println(checkQuestion(docx, o));
		}
	}
	public static String checkQuestion(XWPFDocument docx, TestQuestion o) {
		String result;
		switch (o.getType()) {
		case TestConstants.RUN:
			result = DocumentPropertyChecker.checkRunPropertiesOfParagraphs(docx.getParagraphs(), o.getStrings(), o.getProperties()).toString();
			break;
		case TestConstants.PARAGRAPH:
			result = DocumentPropertyChecker.checkPropertiesOfParagraphs(docx.getParagraphs(), o.getStrings(),  o.getProperties()).toString();
			break;
		case TestConstants.ALL_PARAGRAPHS:
			result = DocumentPropertyChecker.checkPropertiesOfAllParagraphs(docx.getParagraphs(),  o.getProperties()).toString();
			break;
		case TestConstants.MATCH:
			result = DocumentPropertyChecker.checkIfStringExistsInParagraphs(docx.getParagraphs(), o.getStrings()).toString();
			break;
		case TestConstants.DOCUMENT:
			result = DocumentPropertyChecker.checkPropertiesOfDocument(docx, o.getProperties()).toString();
			break;
		default:
			result = "Question type unsupported: " + o.getType();
			break;
		}
		return result;
	}
}
