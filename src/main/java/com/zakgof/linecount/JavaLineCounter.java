package com.zakgof.linecount;

/**
 * Counts java code lines.
 */
public class JavaLineCounter {

  private int javaLineCount;
  private ParserState state = this::codeParser;

  /**
   * Supplies a source line for parsing.
   *
   * @param sourceLine source code line
   * @return true if the line has java code, false if it has only comments and/or whitespace
   */
  public boolean feedLine(String sourceLine) {
    LineReader reader = new LineReader(sourceLine);
    while (!reader.isEol()) {
      state = state.parse(reader);
    }
    if (reader.hasJavaCode()) {
      javaLineCount++;
      return true;
    }
    return false;
  }

  /**
   * Returns java line count.
   *
   * @return number or lines with java code among parsed sources lines
   */
  public int javaLineCount() {
    return javaLineCount;
  }

  /*
   * State design pattern: a parser state scans a part of the source line and returns the new parser
   * state
   */
  private interface ParserState {
    ParserState parse(LineReader reader);
  }

  private ParserState codeParser(LineReader reader) {
    String delim = reader.readUntil(true, "\"", "/*", "//");
    switch (delim) {
      case "/*":
        return this::slashAsteriskCommentParser;
      case "//":
        return this::doubleSlashCommentParser;
      case "\"":
        reader.appendJava(delim);
        return this::stringParser;
      default:
        return this::codeParser;
    }
  }

  private ParserState stringParser(LineReader reader) {
    String delim = reader.readUntil(true, "\"");
    switch (delim) {
      case "\"":
        reader.appendJava(delim);
        return this::codeParser;
      default:
        return this::stringParser;
    }
  }

  private ParserState slashAsteriskCommentParser(LineReader reader) {
    String delim = reader.readUntil(false, "*/");
    switch (delim) {
      case "*/":
        return this::codeParser;
      default:
        return this::slashAsteriskCommentParser;
    }
  }

  private ParserState doubleSlashCommentParser(LineReader reader) {
    reader.readUntil(false);
    return this::codeParser;
  }

}
