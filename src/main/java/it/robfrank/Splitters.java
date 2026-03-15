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
package it.robfrank;

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
