/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.project3.utils.poi;

import java.util.List;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xwpf.usermodel.LineSpacingRule;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;

/**
 * Class that displays the properties of each element in a given XWPFDocument
 * @author Noel
 */
public class DocumentPropertyEnumerator {
    public static void showParagraphElementProperties(List<XWPFRun> rl)
    {
        System.out.println("\nELEMENTS: ");
        int counter = 1;
        for (XWPFRun r : rl) {
            if(r.toString().trim().length() > 0) {
                System.out.println("#" + counter++ + ": " + r.toString());
            }
            else {
                //Ignore spaces, uncomment to display spaces and comment out "continue"
                //System.out.println("#" + counter++ + ": <SPACE>");
                continue;
            }
            if (r.getColor() != null) {
                System.out.println("COLOR: " + r.getColor());
            }
            if (r.getFontFamily() != null) {
                System.out.println("FONT: " + r.getFontFamily());
            }
            if (r.getFontSize() > 0) {
                System.out.println("FONT SIZE: " + r.getFontSize());
            }
            if (r.getPictureText().length() > 0) {
                System.out.println("PIC TEXT: " + r.getPictureText());
            }
            if (r.getTextPosition() > 0) {
                System.out.println("TEXT POS: " + r.getTextPosition());
            }
            if (r.isBold()) {
                System.out.println("BOLD: " + r.isBold());
            }
            if (r.isItalic()) {
                System.out.println("ITALIC: " + r.isItalic());
            }
            if (r.isStrike()) {
                System.out.println("STRIKETHROUGH: " + r.isStrike());
            }
            if (! r.getUnderline().toString().equals("NONE")) {
                System.out.println("UNDERLINE: " + r.getUnderline().toString());
            }
            if (! r.getSubscript().toString().equals("BASELINE")) {
                System.out.println("Subscript: " + r.getSubscript().toString());
            }
            System.out.println("");
        }
    }
    public static void showParagraphProperties(List<XWPFParagraph> lp)
    {
        int i1 = 1;
        for (XWPFParagraph p : lp) {
            //System.out.println(p.getStyleID() + " " + sl1.getStyle(p.getStyleID()).getCTStyle().xmlText());
            System.out.println("____________________________________");
            if(p.getParagraphText().trim().length() > 0) {
                System.out.println("\n#" + i1++ + " LINE: " + p.getParagraphText());
                System.out.println("ALIGNMENT: " + p.getAlignment().toString());
                //Uncomment to display other properties
                /*
                System.out.println("BORDER BETWEEN: " + p.getBorderBetween().toString());
                System.out.println("BORDER BOTTOM: " + p.getBorderBottom().toString());
                System.out.println("BORDER LEFT: " + p.getBorderLeft().toString());
                System.out.println("BORDER RIGHT: " + p.getBorderRight().toString());
                System.out.println("BORDER TOP: " + p.getBorderTop().toString());
                System.out.println("BODY ELEMENT TYPE: " + p.getElementType().toString());
                System.out.println("FOOTNOTE: " + p.getFootnoteText());
                System.out.println("INDENTATION 1ST LINE: " + p.getIndentationFirstLine());
                System.out.println("INDENTATION HANGING: " + p.getIndentationHanging());
                System.out.println("INDENTATION LEFT: " + p.getIndentationLeft());
                System.out.println("INDENTATION RIGHT: " + p.getIndentationRight());
                System.out.println("NUMBERING FORMAT: " + p.getNumFmt());
                System.out.println("NUMERIC STYLE ILVL: " + p.getNumIlvl());
                System.out.println("SPACING AFTER: " + p.getSpacingAfter());
                System.out.println("SPACING AFTER LINES: " + p.getSpacingAfterLines());
                System.out.println("SPACING BEFORE: " + p.getSpacingBefore());
                System.out.println("SPACING BEFORE LINES: " + p.getSpacingBeforeLines());
                System.out.println("SPACING LINE RULE: " + p.getSpacingLineRule());
                System.out.println("VERTICAL ALIGNMENT: " + p.getVerticalAlignment());
                */
            }   // can also use .searchText to look for a string
            else {
                // Uncomment to display lines
                //System.out.println("\n#" + i1++ + " LINE: <SPACE>");
            }
                
            showParagraphElementProperties(p.getRuns());
        }
    }
    public static void showParagraphPropertiesOnly(List<XWPFParagraph> lp)
    {
        int i1 = 1;
        for (XWPFParagraph p : lp) {
            //System.out.println(p.getStyleID() + " " + sl1.getStyle(p.getStyleID()).getCTStyle().xmlText());
            System.out.println("____________________________________");
            if(p.getParagraphText().trim().length() > 0) {
                System.out.println("\n#" + i1++ + " LINE: " + p.getParagraphText());
                System.out.println("ALIGNMENT: " + p.getAlignment().toString());
                //Uncomment to display other properties
                
                System.out.println("BORDER BETWEEN: " + p.getBorderBetween().toString());
                System.out.println("BORDER BOTTOM: " + p.getBorderBottom().toString());
                System.out.println("BORDER LEFT: " + p.getBorderLeft().toString());
                System.out.println("BORDER RIGHT: " + p.getBorderRight().toString());
                System.out.println("BORDER TOP: " + p.getBorderTop().toString());
                System.out.println("BODY ELEMENT TYPE: " + p.getElementType().toString());
                System.out.println("FOOTNOTE: " + p.getFootnoteText());
                System.out.println("INDENTATION 1ST LINE: " + p.getIndentationFirstLine());
                System.out.println("INDENTATION HANGING: " + p.getIndentationHanging());
                System.out.println("INDENTATION LEFT: " + p.getIndentationLeft());
                System.out.println("INDENTATION RIGHT: " + p.getIndentationRight());
                System.out.println("NUMBERING FORMAT: " + p.getNumFmt());
                System.out.println("NUMERIC STYLE ILVL: " + p.getNumIlvl());
                System.out.println("STYLE: " + p.getBody().getXWPFDocument().getStyles().getStyle(p.getStyleID()));
                
                XWPFParagraphClone pc;
                pc = new XWPFParagraphClone(p.getCTP(), p.getBody());
                
                
                System.out.println("SPACING VALUE: " + pc.getCTSpacing(false).getLine().floatValue()/240);
                System.out.println("SPACING AFTER: " + p.getSpacingAfter());
                System.out.println("SPACING AFTER LINES: " + p.getSpacingAfterLines());
                System.out.println("SPACING BEFORE: " + p.getSpacingBefore());
                System.out.println("SPACING BEFORE LINES: " + p.getSpacingBeforeLines());
                System.out.println("SPACING LINE RULE: " + p.getSpacingLineRule());
                System.out.println("VERTICAL ALIGNMENT: " + p.getVerticalAlignment());
                
            }   // can also use .searchText to look for a string
            else {
                // Uncomment to display lines
                //System.out.println("\n#" + i1++ + " LINE: <SPACE>");
            }
        }
    }
    public static void showTableProperties(List<XWPFTable> lt)
    {
        for (XWPFTable t: lt) {
            System.out.println("TABLE: ");
            //Not yet needed
            //System.out.println("COL BAND SIZE: " + t.getColBandSize());
            //System.out.println("ROW BAND SIZE: " + t.getRowBandSize());
            System.out.println("NO. OF ROWS: " + t.getNumberOfRows());
            System.out.println("WIDTH: " + t.getWidth());
        }
    }
    public static void showProperties(XWPFDocument docx) {
        List<XWPFParagraph> lp = docx.getParagraphs();
        List<XWPFTable> lt = docx.getTables();
        showParagraphProperties(lp);
        showTableProperties(lt);
    }
}
