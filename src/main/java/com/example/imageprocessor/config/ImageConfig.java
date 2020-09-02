package com.example.imageprocessor.config;

import com.example.imageprocessor.model.Extension;
import com.example.imageprocessor.model.ImageType;
import com.example.imageprocessor.model.PredefinedImageTypes;
import com.example.imageprocessor.model.ScaleTypes;
import java.util.HashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageConfig {

  @Bean
  public HashMap<PredefinedImageTypes, ImageType> predefinedTypes() {
    HashMap<PredefinedImageTypes, ImageType> predefinedTypes = new HashMap<>();
    predefinedTypes
        .put(
            PredefinedImageTypes.THUMBNAIL, ImageType.builder()
                .width(75)
                .height(75)
                .quality(50)
                .scaleType(ScaleTypes.SKEW)
                .fillColor(null)
                .predefinedType(PredefinedImageTypes.THUMBNAIL)
                .extension(Extension.JPG)
                .build());

    predefinedTypes
        .put(
            PredefinedImageTypes.SMALL, ImageType.builder()
                .width(300)
                .height(300)
                .quality(85)
                .scaleType(ScaleTypes.FILL)
                .fillColor(null)
                .predefinedType(PredefinedImageTypes.SMALL)
                .extension(Extension.JPG)
                .build());

    predefinedTypes
        .put(
            PredefinedImageTypes.LARGE, ImageType.builder()
                .width(600)
                .height(600)
                .quality(90)
                .scaleType(ScaleTypes.FILL)
                .fillColor(null)
                .predefinedType(PredefinedImageTypes.LARGE)
                .extension(Extension.JPG)
                .build());

    predefinedTypes
        .put(
            PredefinedImageTypes.ORIGINAL, ImageType.builder()
                .width(0)
                .height(0)
                .quality(100)
                .scaleType(ScaleTypes.NONE)
                .fillColor(null)
                .predefinedType(PredefinedImageTypes.ORIGINAL)
                .extension(Extension.JPG)
                .build());

    return predefinedTypes;
    
  }
}
