package com.example.imageprocessor.service.impl;

import com.example.imageprocessor.exception.UnsupportedPredefinedImageTypeException;
import com.example.imageprocessor.model.Extension;
import com.example.imageprocessor.model.ImageType;
import com.example.imageprocessor.model.PredefinedImageTypes;
import com.example.imageprocessor.service.AmazonClient;
import com.example.imageprocessor.service.ImageService;
import com.example.imageprocessor.service.OptimizationService;
import com.example.imageprocessor.util.PathUtil;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

  private final HashMap<PredefinedImageTypes, ImageType> predefinedTypes;
  private final AmazonClient amazonClient;
  private final OptimizationService optimizationService;
  @Value("${server.default-source}")
  private String defaultSource;

  @Override
  public byte[] processImage(String predefinedType, Optional<String> seo, String originalFileName)
      throws UnsupportedPredefinedImageTypeException, IOException {

    log.info("Came request for image processing, filename: " + originalFileName);

    ByteArrayOutputStream bao = new ByteArrayOutputStream();
    BufferedImage returnableImage;
    ImageType imageType = validateAndGetImageType(predefinedType, originalFileName);

    String imagePath = PathUtil.getPath(imageType.getPredefinedType(), imageType.getExtension(), originalFileName);
    String originalImagePath = PathUtil
        .getPath(PredefinedImageTypes.ORIGINAL, imageType.getExtension(), originalFileName);

    BufferedImage image = amazonClient.getFileFromS3(imagePath);
    BufferedImage originalImage = amazonClient.getFileFromS3(originalImagePath);

    if (Objects.nonNull(image)) {
      returnableImage = image;

    } else if (Objects.nonNull(originalImage)) {

      returnableImage = optimizationService.optimizeImage(imageType, originalImage);
      amazonClient.uploadFileToS3(imageType, originalFileName, returnableImage);
    } else {

      try {
        URL url = new URL(defaultSource + "?reference=" + originalFileName);
        originalImage = ImageIO.read(url);
      } catch (IOException e) {
        log.error("Can not read file: " + originalFileName, e);
        throw e;
      }

      amazonClient.uploadFileToS3(predefinedTypes.get(PredefinedImageTypes.ORIGINAL), originalFileName, originalImage);
      returnableImage = optimizationService.optimizeImage(imageType, originalImage);
      amazonClient.uploadFileToS3(imageType, originalFileName, returnableImage);
    }

    try {
      ImageIO.write(returnableImage, imageType.getExtension().toLowerCase(), bao);
    } catch (IOException e) {
      log.error("Exception while writing file:  " + returnableImage, e);
      throw e;
    }

    log.info("request processed successfully");
    return bao.toByteArray();
  }

  @Override
  public List<String> flushImage(String predefinedType, String originalFileName) {
    log.info("Came request for flushing image with name: " + originalFileName);
    ImageType imageType = validateAndGetImageType(predefinedType, originalFileName);
    final List<String> status = new ArrayList<>();
    if (imageType.getPredefinedType() == PredefinedImageTypes.ORIGINAL) {
      Arrays.stream(PredefinedImageTypes.values()).forEach(type -> {
        String result = amazonClient.deleteFileFromS3(PathUtil.getPath(type, imageType.getExtension(), originalFileName));
        status.add(result);

      });
      return status;
    }
    status.add(amazonClient.deleteFileFromS3(
        PathUtil.getPath(imageType.getPredefinedType(), imageType.getExtension(), originalFileName)));
    return status;

  }

  /**
   * Will check if predefined type from controller is valid
   */
  private ImageType validateAndGetImageType(String predefinedType, String originalFileName) {
    ImageType imageType = predefinedTypes.get(PredefinedImageTypes.valueOf(predefinedType.toUpperCase()));
    if (Objects.isNull(imageType)) {
      log.info("Unsupported predefined type: " + predefinedType);
      throw new UnsupportedPredefinedImageTypeException("Unsupported type was given to service: " + predefinedType);
    }
    imageType
        .setExtension(
            Extension.valueOf(originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toUpperCase()));
    log.info("Successful validation");
    return imageType;
  }
}
