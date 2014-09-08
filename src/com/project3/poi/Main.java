package com.project3.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xwpf.usermodel.TestXWPFDocument;
import org.json.simple.parser.ParseException;

import com.project3.poi.test.controllers.TestChecker;
import com.project3.test.models.TestQuestion;
import com.project3.test.utils.TestReader;

public class Main {

	public static void main(String[] args) {
		File jsonFile = new File("test_questions/2.json");
		File docxFile = new File("docx/test_2.docx");
		TestXWPFDocument docx;
		
		try {
			List<TestQuestion> testQuestions = TestReader.parseJSONQuestions(jsonFile.getAbsolutePath());
			
			System.out.println("Name\tType\tMustPass\tStrings\tProperties");
			
			for (TestQuestion question : testQuestions) {
				System.out.println(question);
			}

			docx = new TestXWPFDocument(new FileInputStream(docxFile));
			TestChecker.checkAllQuestions(docx, testQuestions);
		} catch (IOException| ParseException e) {
			e.printStackTrace();
		}
	}

}
