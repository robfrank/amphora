package it.jugtorino.chalk;

import java.util.Map;

public class Splitters {

  private Splitters() {
    // Private constructor for utility class
  }

  public static final String onSpaceSplitter = " ";

  public static final String onCommaSplitter = ",";

  public static final String onSemicolonSplitter = ";";

  public static final String onUnderscoreSplitter = "_";

  public static final String onAmpSplitter = "&";

  public static final String onQuestionMarkSplitter = "\\?";

  public static final String onSlashSplitter = "/";

  public static final String onMinusAndSpacesSplitter = " - ";

  private static final Map<String, String> separatorSplitters =
      Map.of(
          " ", onSpaceSplitter,
          ",", onCommaSplitter,
          ";", onSemicolonSplitter,
          "_", onUnderscoreSplitter,
          "/", onSlashSplitter,
          " - ", onMinusAndSpacesSplitter);

  public static String onSeparatorSplitter(String separator) {
    return separatorSplitters.getOrDefault(separator, separator);
  }
}
