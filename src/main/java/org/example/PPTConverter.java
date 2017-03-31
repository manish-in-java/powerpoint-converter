package org.example;

import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.sl.usermodel.SlideShow;

import java.io.InputStream;

public class PPTConverter extends SlideshowConverter
{
  protected SlideShow<?, ?> getSlideshow(final InputStream stream) throws Throwable
  {
    return new HSLFSlideShow(stream);
  }
}
