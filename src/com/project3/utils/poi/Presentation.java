package com.project3.utils.poi;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.apache.poi.xslf.usermodel.XSLFTheme;


public class Presentation {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    try {
      XMLSlideShow ppt = new XMLSlideShow(new FileInputStream("pptx/2.pptx"));
      
      showSlideShowProperties(ppt);
      
      for (XSLFSlide slide : ppt.getSlides()) {
        showSlideProperties(slide);
        showTextShapes(slide);
        System.out.println();
      }
      
      for (XSLFPictureData pictureData : ppt.getAllPictures()) {
        showPictureDataProperties(pictureData);
      }
      
      try {
        for (PackagePart part : ppt.getAllEmbedds()) {
          System.out.println(part + " " + part.getContentType() + " " + part.getPartName());
        }
      } catch (OpenXML4JException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  private static void showPictureDataProperties(XSLFPictureData pictureData) {
    System.out.println("File name" + ": " + pictureData.getFileName());
    System.out.println("Type" + ": " + pictureData.getPictureType());
    // System.out.println("Data" + ": " + pictureData.getData());
    
  }
  
  private static void showSlideShowProperties(XMLSlideShow ppt) {
    System.out.println("Page size" + ": " + ppt.getPageSize());
  }
  
  private static void showSlideProperties(XSLFSlide slide) {
    System.out.println("Title" + ": " + slide.getTitle());
    System.out.println("Background" + ": " + slide.getBackground());
    System.out.println("Notes"+ ": " + slide.getNotes());
    System.out.println("Layout"+ ": " + slide.getSlideLayout().getName());
    showSlideThemeProperties(slide.getTheme());
   
  }
  
  private static void showSlideThemeProperties(XSLFTheme theme) {
    System.out.println("Theme name" + ": " + theme.getName());
    System.out.println("Theme major font" + ": " + theme.getMajorFont());
    System.out.println("Theme minor font" + ": " + theme.getMinorFont());
  }

  private static void showTextShapes(XSLFSlide slide) {
    for (XSLFTextShape textShape : slide.getPlaceholders()) {
      showTextShapeProperties(textShape);
    }

  }

  private static void showTextShapeProperties(XSLFTextShape textShape) {
    System.out.println("Text" + " :" + textShape.getText());
    System.out.println("Text height" + ": " + textShape.getTextHeight());
    System.out.println("Text autofit" + ": " + textShape.getTextAutofit().name());
    System.out.println("Text direction" + ": " + textShape.getTextDirection().name());
    System.out.println("Text type" + ": " + textShape.getTextType().name());

    System.out.println("Bot inset" + ": " + textShape.getBottomInset());
    System.out.println("Left inset" + ": " + textShape.getLeftInset());
    System.out.println("Right inset" + ": " + textShape.getRightInset());
    System.out.println("Top inset" + ": " + textShape.getTopInset());

    System.out.println("Line width" + ": " + textShape.getLineWidth());
    System.out.println("Rotation" + ": " + textShape.getRotation());

    System.out.println("Shape id" + ": " + textShape.getShapeId());
    System.out.println("Shape name" + ":" + textShape.getShapeName());
    System.out.println("Shape type" + ": " + textShape.getShapeType());
    
    System.out.println("Anchor" + ": " + textShape.getAnchor().toString());
    System.out.println("Fill color" + ": " + textShape.getFillColor());
    
    System.out.println("Flip horizontal" + ": " + textShape.getFlipHorizontal());
    System.out.println("Flip vertical" + ": " + textShape.getFlipVertical());
    
    System.out.println("Line width" + ": " + textShape.getLineWidth());
    System.out.println("Line cap" + ": " + textShape.getLineCap());
    System.out.println("Line color" + ": " + textShape.getLineColor());
    System.out.println("Line dash" + ": " + textShape.getLineDash());
    System.out.println("Line head decor" + ": " + textShape.getLineHeadDecoration());
    System.out.println("Line head length" + ": " + textShape.getLineHeadLength());
    System.out.println("Line head width" + ": " + textShape.getLineHeadWidth());
    System.out.println("Line tail decor" + ": " + textShape.getLineTailDecoration());
    System.out.println("Line tail length" + ": " + textShape.getLineTailLength());
    System.out.println("Line tail width" + ": " + textShape.getLineTailWidth());
    
    System.out.println("Shadow" + ": " + textShape.getShadow());
    System.out.println("Vertical alignment" + ": " + textShape.getVerticalAlignment());
    System.out.println("Word wrap" + ": " + textShape.getWordWrap());
    

    showTextShapeParagraphs(textShape);
  }

  private static void showTextShapeParagraphs(XSLFTextShape textShape) {
    for (XSLFTextParagraph textParagraph : textShape.getTextParagraphs()) {
      showTextParagraphProperties(textParagraph);
    }
  }

  private static void showTextParagraphProperties(XSLFTextParagraph textParagraph) {
    System.out.println("Text" + ": " + textParagraph.getText());
    System.out.println("Text align" + ": " + textParagraph.getTextAlign());

    System.out.println("Bullet character" + ": " + textParagraph.getBulletCharacter());
    System.out.println("Bullet font" + ": " + textParagraph.getBulletFont());
    System.out.println("Bullet font size" + ": " + textParagraph.getBulletFontSize());
    System.out.println("Bullet font color" + ": " + textParagraph.getBulletFontColor());

    System.out.println("Tab size" + ": " + textParagraph.getDefaultTabSize());
    System.out.println("Indent" + ": " + textParagraph.getIndent());
    System.out.println("Level" + ": " + textParagraph.getLevel());

    System.out.println("Line spacing" + ": " + textParagraph.getLineSpacing());
    System.out.println("Space after" + ": " + textParagraph.getSpaceAfter());
    System.out.println("Space before" + ": " + textParagraph.getSpaceBefore());

    System.out.println("Margin left" + ": " + textParagraph.getLeftMargin());

    showParagraphTextRuns(textParagraph);
  }

  private static void showParagraphTextRuns(XSLFTextParagraph textParagraph) {
    for (XSLFTextRun textRun : textParagraph.getTextRuns()) {
      showTextRunProperties(textRun);
    }
  }

  private static void showTextRunProperties(XSLFTextRun textRun) {
    System.out.println("Text" + ": " + textRun.getText());
    System.out.println("Character spacing" + ": " + textRun.getCharacterSpacing());

    System.out.println("Font family" + ": " + textRun.getFontFamily());
    System.out.println("Font size" + ": " + textRun.getFontSize());
    System.out.println("Font pitch" + ": " + textRun.getPitchAndFamily());
    System.out.println("Font color" + ": " + textRun.getFontColor());

    //System.out.println("Hyperlink target" + ": " + (textRun.getHyperlink() != null && textRun.getHyperlink().getTargetURI() != null ? textRun
     //   .getHyperlink().getTargetURI() : "No hyperlink"));
    System.out.println("Text cap" + ": " + textRun.getTextCap().toString());
  }
}
