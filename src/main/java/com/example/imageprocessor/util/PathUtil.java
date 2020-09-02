package com.example.imageprocessor.util;

import com.example.imageprocessor.model.Extension;
import com.example.imageprocessor.model.PredefinedImageTypes;
import java.util.StringJoiner;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PathUtil {

  /**
   * if the image file-name (without the extension) has a length greater than 4, the first 4
   * characters will define the initial sub-directory. if the image file-name (without the extension) has a length greater than 8, the second set of 4
   * characters will define the second sub-directory.
   * @param predefinedType
   * @param extension
   * @param filename
   * @return
   */
  public static String getPath(PredefinedImageTypes predefinedType, Extension extension, String filename) {
    filename = filename.replaceAll("/", "_");

    filename = filename.substring(0, filename.lastIndexOf("."));

    String suffix = "." + extension.toLowerCase();

    StringJoiner stringJoiner = new StringJoiner("/", "", suffix);
    stringJoiner.add(predefinedType.toString());
    if (filename.length() > 4) {
      stringJoiner.add(filename.substring(0, 4));
    }
    if (filename.length() > 8) {
      stringJoiner.add(filename.substring(4, 8));
    }
    stringJoiner.add(filename);

    return stringJoiner.toString();
  }
}
