package com.example.imageprocessor.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.imageprocessor.service.impl.ImageServiceImpl;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ImageControllerTest {

  @InjectMocks
  private ImageController imageController;
  @Mock
  private ImageServiceImpl imageService;

  @Test
  public void getImage() throws IOException {
    when(imageService.processImage(anyString(), any(), anyString())).thenReturn(null);
    imageController.getImage(anyString(), any(), anyString());
    verify(imageService, times(1)).processImage(anyString(), any(), anyString());
  }

  @Test
  public void flushImage() {
    when(imageService.flushImage(any(), anyString())).thenReturn(null);
    imageController.flushImage(any(), anyString());
    verify(imageService, times(1)).flushImage(any(), anyString());
  }
}
