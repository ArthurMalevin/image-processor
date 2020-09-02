package com.example.imageprocessor.service.impl;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.example.imageprocessor.model.ImageType;
import com.example.imageprocessor.service.AmazonClient;
import com.example.imageprocessor.util.PathUtil;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j
@Service
public class AmazonClientImpl implements AmazonClient {

  private AmazonS3 s3client;
  @Value("${amazonProperties.endpointUrl}")
  private String endpointUrl;
  @Value("${amazonProperties.bucketName}")
  private String bucketName;
  @Value("${amazonProperties.accessKey}")
  private String accessKey;
  @Value("${amazonProperties.secretKey}")
  private String secretKey;
  @Value("${local.directory}")
  private String localDirectory;

  @PostConstruct
  private void initializeAmazon() {
    BasicAWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
    this.s3client = AmazonS3ClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(credentials))
        .withRegion(Regions.US_EAST_2)
        .build();
  }

  @Override
  public void uploadFileToS3(ImageType imageType, String originalName, BufferedImage bufferedImage) throws IOException {
    String path = PathUtil.getPath(imageType.getPredefinedType(), imageType.getExtension(), originalName);
    File file = new File(localDirectory + path);
    file.mkdirs();

    try {
      ImageIO.write(bufferedImage, imageType.getExtension().toLowerCase(), file);
    } catch (IOException e) {
      log.error("Exception while saving file: " + originalName + "to local directory: " + localDirectory, e);
      throw e;
    }
    s3client.putObject(bucketName, path, file);
    file.delete();
    log.info("file uploaded to cloud successfully. Path: " + path);
  }

  @Override
  public String deleteFileFromS3(String fileUrl)  {
    if (Objects.nonNull(getFileFromS3(fileUrl))) {
      s3client.deleteObject(bucketName, fileUrl);
      log.info("Successfully deleted image with fileURL: " + fileUrl);
      return "Successfully deleted image with fileURL: " + fileUrl;
    }
    log.info("Nothing to delete by fileURL: " + fileUrl);
    return "Nothing to delete by fileURL: " + fileUrl;
  }

  @Override
  public BufferedImage getFileFromS3(String objectKey) {
    S3Object s3Object;

    try {
      log.info("Attempt to get file from aws s3 with object key: " + objectKey);
      s3Object = s3client.getObject(bucketName, objectKey);
    } catch (AmazonS3Exception e) {
      log.error("File not found by fileURL: " + objectKey);
      return null;
    }

    BufferedImage imageFromAWS;
    try {
      imageFromAWS = ImageIO.read(s3Object.getObjectContent());
    } catch (IOException e) {
      log.error("Exception while reading file from AWS s3 with object key: " + objectKey, e);
      return null;
    }
    log.info("File with filename: " + objectKey + " was found.");
    return imageFromAWS;
  }
}
