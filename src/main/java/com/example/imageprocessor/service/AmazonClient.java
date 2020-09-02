package com.example.imageprocessor.service;

import com.example.imageprocessor.model.ImageType;
import java.awt.image.BufferedImage;
import java.io.IOException;

public interface AmazonClient {

  void uploadFileToS3(ImageType imageType, String originalName, BufferedImage bufferedImage) throws IOException;

  String deleteFileFromS3(String fileUrl);

  BufferedImage getFileFromS3(String objectKey);
}
