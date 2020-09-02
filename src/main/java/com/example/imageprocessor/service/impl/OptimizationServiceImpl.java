package com.example.imageprocessor.service.impl;

import com.example.imageprocessor.exception.UnsupportedScaleTypeException;
import com.example.imageprocessor.model.ImageType;
import com.example.imageprocessor.service.OptimizationService;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

@Log4j
@Service
public class OptimizationServiceImpl implements OptimizationService {

  @Override
  public BufferedImage optimizeImage(ImageType imageType, BufferedImage original)
      throws UnsupportedScaleTypeException {
    switch (imageType.getScaleType()) {
      case SKEW:
        return skew(original, imageType);
      case FILL:
        return scale(original, imageType);
      case CROP:
        return crop(original, imageType);
      default:
        throw new UnsupportedScaleTypeException(
            "Unsupported scale type of " + imageType.getScaleType() + " was given to the optimisation service");
    }
  }


  /**
   * @return a scaled image respecting the aspect ratio
   */
  private BufferedImage scale(BufferedImage original, ImageType imageType) {

    int newWidth = original.getWidth();
    int newHeight = original.getHeight();
    int predefinedWidth = imageType.getWidth();
    int predefinedHeight = imageType.getHeight();

    if (original.getWidth() > predefinedWidth) {
      newWidth = predefinedWidth;
      newHeight = (newWidth * original.getHeight()) / original.getWidth();
    }
    if (original.getHeight() > predefinedHeight) {
      newHeight = predefinedHeight;
      newWidth = (newHeight * original.getWidth()) / original.getHeight();
    }

    int x = predefinedWidth / 2 - newWidth / 2;
    int y = predefinedHeight / 2 - newHeight / 2;

    BufferedImage scaledImage = new BufferedImage(predefinedWidth, predefinedHeight, original.getType());
    Graphics2D g = scaledImage.createGraphics();
    g.drawImage(original, x, y, newWidth, newHeight, null);
    g.dispose();

    log.info("Image was scaled");
    return scaledImage;
  }


  /**
   * @return a center cropped image that no longer fit the new aspect ratio
   */
  private BufferedImage crop(BufferedImage original, ImageType imageType) {
    int x = (original.getWidth() / 2) - (imageType.getWidth() / 2);
    int y = (original.getHeight() / 2) - (imageType.getHeight() / 2);
    log.info("Image was crop[ed");
    return original.getSubimage(x, y, imageType.getWidth(), imageType.getHeight());
  }


  /**
   * @return simply squeezed image to fit the new height and width, this will make the image look bad in most cases
   */
  private BufferedImage skew(BufferedImage original, ImageType imageType) {
    BufferedImage skewedImage = new BufferedImage(imageType.getWidth(), imageType.getHeight(), original.getType());
    Graphics2D g = skewedImage.createGraphics();
    g.drawImage(original, 0, 0, imageType.getWidth(), imageType.getHeight(), null);
    g.dispose();
    log.info("Image was skewed");
    return skewedImage;
  }
}
