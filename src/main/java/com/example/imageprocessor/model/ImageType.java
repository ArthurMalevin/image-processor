package com.example.imageprocessor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class ImageType {

  private int width;
  private int height;
  private int quality;
  private ScaleTypes scaleType;
  private String fillColor;
  private PredefinedImageTypes predefinedType;
  private Extension extension;
}
