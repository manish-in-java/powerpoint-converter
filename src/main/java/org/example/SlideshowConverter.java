package org.example;

import org.apache.poi.sl.usermodel.Slide;
import org.apache.poi.sl.usermodel.SlideShow;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

abstract class SlideshowConverter
{
  public Collection<BufferedImage> convert(final InputStream stream)
  {
    try
    {
      return convert(getSlideshow(stream));
    }
    catch (final Throwable t)
    {
      throw new RuntimeException(t);
    }
  }

  protected abstract SlideShow<?, ?> getSlideshow(final InputStream stream) throws Throwable;

  private Collection<BufferedImage> convert(final SlideShow<?, ?> slideshow)
  {
    if (slideshow == null)
    {
      throw new NullPointerException("Argument [slideshow] must not be null.");
    }

    final List<? extends Slide<?, ?>> slides = slideshow.getSlides();

    if (slides == null || slides.size() == 0)
    {
      throw new IllegalArgumentException("The slideshow does not contain any slides to convert.");
    }

    final Dimension dimensions = slideshow.getPageSize();

    if (dimensions.height == 0)
    {
      throw new IllegalArgumentException("The slideshow contains slides of zero height and therefore cannot be converted.");
    }
    else if (dimensions.width == 0)
    {
      throw new IllegalArgumentException("The slideshow contains slides of zero width and therefore cannot be converted.");
    }

    final List<BufferedImage> images = new ArrayList<BufferedImage>();

    for (final Slide<?, ?> slide : slides)
    {
      try
      {
        final BufferedImage image = new BufferedImage(dimensions.width, dimensions.height, BufferedImage.TYPE_INT_RGB);

        final Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.setPaint(Color.white);
        graphics.fill(new Rectangle2D.Float(0, 0, dimensions.width, dimensions.height));

        slide.draw(graphics);
        image.flush();

        images.add(image);
      }
      catch (final Throwable t)
      {
        throw new RuntimeException(String.format("An error occurred while trying to convert slide %d.", slides.indexOf(slide)), t);
      }
    }

    return images;
  }

  private int[][] getPixels(final BufferedImage image)
  {
    final int[][] pixels = new int[image.getHeight()][image.getWidth()];

    for (int y = 0; y < image.getHeight(); y++)
    {
      for (int x = 0; x < image.getWidth(); x++)
      {
        final Color c = new Color(image.getRGB(x, y));
        int red = (c.getRed() & 0xC0) >> 2;
        int green = (c.getGreen() & 0xC0) >> 4;
        int blue = (c.getBlue() & 0xC0) >> 6;

        pixels[y][x] = red | green | blue;
      }
    }

    return pixels;
  }

  private BufferedImage quantizeImage(final BufferedImage image)
  {
    final int[][] pixels = getPixels(image);

    final BufferedImage quantizedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
    for (int i = 0; i < pixels.length; i++)
    {
      for (int j = 0; j < pixels[0].length; j++)
      {
        int color = pixels[i][j];
        int red = (color & 0x30) << 2;
        int green = (color & 0xC) << 4;
        int blue = (color & 0x3) << 6;

        quantizedImage.setRGB(j, i, new Color(red, green, blue).getRGB());
      }
    }

    quantizedImage.flush();

    return quantizedImage;
  }
}
