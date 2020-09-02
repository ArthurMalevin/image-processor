package com.example.imageprocessor.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.imageprocessor.config.ImageConfig;
import com.example.imageprocessor.exception.UnsupportedScaleTypeException;
import com.example.imageprocessor.model.ImageType;
import com.example.imageprocessor.model.PredefinedImageTypes;
import com.example.imageprocessor.service.impl.OptimizationServiceImpl;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ImageConfig.class, loader = AnnotationConfigContextLoader.class)
public class OptimizationServiceTest {

  @InjectMocks
  private OptimizationServiceImpl optimizationService;
  private File image = new File("src/test/resources/small.jpg");


  @Autowired
  private HashMap<PredefinedImageTypes, ImageType> predefinedTypes;


  @Test
  public void optimizeWithThumbnailType() throws IOException {
    BufferedImage result = optimizationService
        .optimizeImage(predefinedTypes.get(PredefinedImageTypes.THUMBNAIL), ImageIO.read(image));
    assertThat(result.getWidth()).isEqualTo(75);
    assertThat(result.getHeight()).isEqualTo(75);

  }

  @Test
  public void optimizeWithSmallType() throws IOException {
    BufferedImage result = optimizationService
        .optimizeImage(predefinedTypes.get(PredefinedImageTypes.SMALL), ImageIO.read(image));
    assertThat(result.getWidth()).isEqualTo(300);
    assertThat(result.getHeight()).isEqualTo(300);

  }

  @Test
  public void optimizeWithLargeType() throws IOException {
    BufferedImage result = optimizationService
        .optimizeImage(predefinedTypes.get(PredefinedImageTypes.LARGE), ImageIO.read(image));
    assertThat(result.getWidth()).isEqualTo(600);
    assertThat(result.getHeight()).isEqualTo(600);

  }

  @Test(expected = UnsupportedScaleTypeException.class)
  public void optimizeWithOriginalType() throws IOException {
    optimizationService.optimizeImage(predefinedTypes.get(PredefinedImageTypes.ORIGINAL), ImageIO.read(image));
  }

}
