package com.example.imageprocessor.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ImageService {

  byte[] processImage(String predefinedType, Optional<String> seo, String originalFileName) throws IOException;

  List<String> flushImage(String predefinedType, String originalFileName);
}
