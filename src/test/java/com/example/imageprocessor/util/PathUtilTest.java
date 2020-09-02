package com.example.imageprocessor.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.imageprocessor.model.Extension;
import com.example.imageprocessor.model.PredefinedImageTypes;
import org.junit.Test;

public class PathUtilTest {

  private Extension extension = Extension.JPG;
  private PredefinedImageTypes type = PredefinedImageTypes.SMALL;

  @Test
  public void getPathFromRealFile() {
    String validPath = "SMALL/066_/670_/066_670_13_0666702579000000_pro_flt_frt_01_1108_1528_4747501.jpg";
    String input = "066/670/13_0666702579000000_pro_flt_frt_01_1108_1528_4747501.jpg";
    String result = PathUtil.getPath(type, extension, input);
    assertThat(result).isEqualTo(validPath);
  }

  @Test
  public void getPathWhenFileNameLengthGreaterThanFourSymbols() {
    String validPath = "SMALL/1234/5678/123456789.jpg";
    String input = "123456789.jpg";
    String result = PathUtil.getPath(type, extension, input);
    assertThat(result).isEqualTo(validPath);
  }

  @Test
  public void getPathWhenFileNameLengthLessThanFourSymbols() {
    String validPath = "SMALL/123.jpg";
    String input = "123.jpg";
    String result = PathUtil.getPath(type, extension, input);
    assertThat(result).isEqualTo(validPath);
  }

  @Test
  public void getPathWhenFileNameLengthFourSymbols() {
    String validPath = "SMALL/1234.jpg";
    String input = "1234.jpg";
    String result = PathUtil.getPath(type, extension, input);
    assertThat(result).isEqualTo(validPath);
  }

  @Test
  public void getPathWhenFileNameLengthLessThanFourSymbolsAndLessThanEight() {
    String validPath = "SMALL/1234/12345.jpg";
    String input = "12345.jpg";
    String result = PathUtil.getPath(type, extension, input);
    assertThat(result).isEqualTo(validPath);
  }

  @Test
  public void getPathWhenThereIsSlashes() {
    String validPath = "SMALL/_som/edir/_somedir_anotherdir_abcdef.jpg";
    String input ="/somedir/anotherdir/abcdef.jpg";
    String result = PathUtil.getPath(type, extension, input);
    assertThat(result).isEqualTo(validPath);
  }
}
