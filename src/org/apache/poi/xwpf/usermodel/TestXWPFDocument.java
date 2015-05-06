package org.apache.poi.xwpf.usermodel;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.util.IdentifierManager;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 * Placed in org.apache.poi.xwpf.usermodel package to override getDrawingIdManager method
 */
public class TestXWPFDocument extends XWPFDocument{
  
  public TestXWPFDocument (InputStream is) throws IOException {
    super(is);
  }
  /**
   * Returning drawingIdManager becomes null because of internal changes?
   * Forcing a new IdentifierManager every time works but might be expensive.
   * Modify POI source code then build custom jar library instead.
   */
  @Override
  IdentifierManager getDrawingIdManager() {
    //Accommodate 0 starting index
    return new IdentifierManager(0L,4294967295L);
  }
}
