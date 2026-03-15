package it.jugtorino.chalk;

import java.util.Map;

public class Joiners {

  private Joiners() {
    // Private constructor for utility class
  }

  public static final String onSpaceJoiner = " ";

  public static final String onUnderscoreJoiner = "_";

  public static final String onAmpJoiner = "&";

  public static final String onQuestionMarkJoiner = "?";

  public static final String onSlashJoiner = "/";

  public static final String onEmptyJoiner = "";

  public static final String onMinusAndSpacesJoiner = " - ";

  private static final Map<String, String> separatorJoiners =
      Map.of(
          " ", onSpaceJoiner,
          "_", onUnderscoreJoiner,
          "&", onAmpJoiner,
          "/", onSlashJoiner,
          " - ", onMinusAndSpacesJoiner);

  public static String onSeparatorJoiner(String separator) {
    return separatorJoiners.getOrDefault(separator, separator);
  }
}
