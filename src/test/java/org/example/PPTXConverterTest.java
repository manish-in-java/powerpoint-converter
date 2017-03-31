package org.example;

import java.io.InputStream;

public class PPTXConverterTest extends SlideshowConverterTest
{
  protected SlideshowConverter getConverter()
  {
    return new PPTXConverter();
  }

  protected InputStream getSlideshowStream()
  {
    return getClass().getClassLoader().getResourceAsStream("sample.pptx");
  }
}
