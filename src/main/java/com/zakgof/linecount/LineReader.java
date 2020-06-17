package com.zakgof.linecount;

/**
 * Scans a source string by seeking to the next known delimiter. It may skip the characters or keep
 * them as "java code".
 */
public class LineReader {

  private final String srcCode;
  private String javaCode = "";
  private int pos;

  /**
   * Creates a line reader with a specified source line.
   *
   * @param srcCode source code line
   */
  public LineReader(String srcCode) {
    this.srcCode = srcCode;
  }

  /**
   * Appends a string as java code.
   *
   * @param javaCode string to append
   */
  public void appendJava(String javaCode) {
    this.javaCode += javaCode;
  }

  /**
   * Reads a string until the nearest occurrence of a known delimiter and returns the delimiters.
   *
   * @param asJava if true - store the scanned characters as java code; otherwise skip them.
   *        Delimiter is never included, call {@link #appendJava(String)} to append manually if
   *        needed.
   * @param delimiters known delimiters
   * @return the first found delimited or empty string if the end of the source line reached.
   */
  public String readUntil(boolean asJava, String... delimiters) {
    String nearestDelimiter = "";
    int nearestDelimiterPos = Integer.MAX_VALUE;
    for (String delimiter : delimiters) {
      int delimiterPos = srcCode.indexOf(delimiter, pos);
      if (delimiterPos >= 0 && delimiterPos < nearestDelimiterPos) {
        nearestDelimiterPos = delimiterPos;
        nearestDelimiter = delimiter;
      }
    }
    int moveto = nearestDelimiter.isEmpty() ? srcCode.length() : nearestDelimiterPos;
    if (asJava) {
      javaCode += srcCode.substring(pos, moveto);
    }
    this.pos = moveto + nearestDelimiter.length();
    return nearestDelimiter;
  }

  /**
   * Checks is EOL reached in the line.
   *
   * @return whether the end of source code string is reached
   */
  public boolean isEol() {
    return pos == srcCode.length();
  }

  /**
   * Checks is the line has Java code.
   *
   * @return whether the source string has any java code
   */
  public boolean hasJavaCode() {
    return !javaCode.strip().isEmpty();
  }

  /**
   * Returns Java code for the source line with (comments removed).
   *
   * @return java code for the source line
   */
  public String javaCode() {
    return javaCode;
  }

}
