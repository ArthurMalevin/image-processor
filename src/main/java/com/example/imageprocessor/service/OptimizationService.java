package com.example.imageprocessor.service;

import com.example.imageprocessor.exception.UnsupportedScaleTypeException;
import com.example.imageprocessor.model.ImageType;
import java.awt.image.BufferedImage;


public interface OptimizationService {

  BufferedImage optimizeImage(ImageType imageType, BufferedImage original) throws UnsupportedScaleTypeException;
}
