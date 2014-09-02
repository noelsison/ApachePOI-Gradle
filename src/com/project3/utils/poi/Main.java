package com.project3.utils.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.project3.utils.test.TestChecker;
import com.project3.utils.test.TestReader;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestReader tr = new TestReader("C:\\Users\\Noel\\git\\ApachePOI-Gradle\\test_questions\\1.json");
		System.out.println(tr.getTestQuestionList().toString());
		XWPFDocument docx;
		try {
			docx = new XWPFDocument(new FileInputStream(new File("C:\\Users\\Noel\\git\\ApachePOI-Gradle\\docx\\resume_only.docx")));
			TestChecker.checkAllQuestions(docx, tr.getTestQuestionList());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
