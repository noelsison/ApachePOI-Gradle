package com.project3.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xwpf.usermodel.TestXWPFDocument;
import org.json.simple.parser.ParseException;

import com.project3.poi.test.controllers.TestChecker;
import com.project3.poi.test.controllers.TestReader;
import com.project3.test.models.TestQuestion;

public class Main {

	public static void main(String[] args) {
		File jsonFile = new File("test_questions/2.json");
		File docxFile = new File("docx/test_2.docx");
		TestXWPFDocument docx;
		TestChecker testChecker;
		try {
			List<TestQuestion> testQuestions = TestReader.parseJSONQuestions(jsonFile.getAbsolutePath());
			System.out.println("----------------- QUESTIONS -----------------");
			System.out.println("Name\tType\tStrings\tProperties");
			
			for (TestQuestion question : testQuestions) {
				System.out.println(question);
			}

			docx = new TestXWPFDocument(new FileInputStream(docxFile));
			testChecker = new TestChecker(docx);
			testChecker.checkAllQuestions(testQuestions);
			
		} catch (IOException| ParseException e) {
			e.printStackTrace();
		}
	}

}
