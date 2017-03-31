package org.example;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.UUID;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

abstract class SlideshowConverterTest
{
  @Test
  public void testConvert() throws IOException
  {
    final Collection<BufferedImage> images = getConverter().convert(getSlideshowStream());

    assertNotNull(images);

    assertNotEquals(0, images.size());

    for (final BufferedImage image : images)
    {
      assertNotNull(image);

      assertNotEquals(0, image.getHeight());
      assertNotEquals(0, image.getWidth());
    }
  }

  protected abstract SlideshowConverter getConverter();

  protected abstract InputStream getSlideshowStream();

  private void save(final BufferedImage image) throws IOException
  {
    ImageIO.write(image, "png", new File("C:/Temp/New/" + UUID.randomUUID().toString() + ".png"));
  }
}
