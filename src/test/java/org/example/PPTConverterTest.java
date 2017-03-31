package org.example;

import java.io.InputStream;

public class PPTConverterTest extends SlideshowConverterTest
{
  protected SlideshowConverter getConverter()
  {
    return new PPTConverter();
  }

  protected InputStream getSlideshowStream()
  {
    return getClass().getClassLoader().getResourceAsStream("sample.ppt");
  }
}
