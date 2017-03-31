package org.example;

import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

import java.io.InputStream;

public class PPTXConverter extends SlideshowConverter
{
  protected SlideShow<?, ?> getSlideshow(final InputStream stream) throws Throwable
  {
    return new XMLSlideShow(stream);
  }
}
