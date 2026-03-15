/*
 * MIT License
 *
 * Copyright (c) 2026 JUG Torino
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
