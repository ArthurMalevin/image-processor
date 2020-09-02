package com.example.imageprocessor.model;

public enum Extension {
  JPG,
  PNG;

  public String toLowerCase() {
    return super.toString().toLowerCase();
  }
}
