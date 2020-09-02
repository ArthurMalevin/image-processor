package com.example.imageprocessor.controller;

import com.example.imageprocessor.exception.UnsupportedPredefinedImageTypeException;
import com.example.imageprocessor.service.ImageService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@AllArgsConstructor
@RestController
@RequestMapping(value = "/image")
public class ImageController {

  private final ImageService imageService;

  @GetMapping(
      value = {"/show/{predefinedType}/", "/show/{predefinedType}/{seo}/"},
      produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
  public ResponseEntity<byte[]> getImage(
      @PathVariable String predefinedType,
      @PathVariable Optional<String> seo,
      @RequestParam("reference") String originalFileName)
      throws IOException, UnsupportedPredefinedImageTypeException, IllegalArgumentException {
    return ResponseEntity.ok(imageService.processImage(predefinedType, seo, originalFileName));
  }

  @DeleteMapping(value = {"/flush/{predefinedType}/"})
  public ResponseEntity<List<String>> flushImage(
      @PathVariable String predefinedType,
      @RequestParam("reference") String originalFileName)
      throws UnsupportedPredefinedImageTypeException, IllegalArgumentException {
    return ResponseEntity.ok(imageService.flushImage(predefinedType, originalFileName));
  }
}
