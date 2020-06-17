package com.zakgof.linecount.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.zakgof.linecount.JavaLineCounter;

public class MultiLineTest {

  @Test
  public void comments() {
    test(0, "/* open ", " still comment ", " and comment ", "closing */");
    test(1, "pre /* open ", " still comment ", " and comment ", "closing */");
    test(1, "/* open ", " still comment ", " and comment ", "closing */ post");
    test(1, "/* open ", " still comment ", " and comment ", "closing */", "next line");

    test(0, "/* open ", " close */      /* and open ", " and comment ", "closing */");
    test(1, "/* open ", " close */ java /* and open ", " and comment ", "closing */");

    test(0, "/*one*/  /*two*//*three", "still comment*/ /*open again", "and finally close*/   ");
    test(2, "/*one*/  /*two*//*three", "still comment*/ java here /*open again",
        "and finally close*/ and more java");

    test(0, "/* open // tricky here", " still comment ", " and comment ", "closing */");
    test(1, "/* open // tricky here", " still comment ", " and comment ", "closing */ code");
    test(1, "/* open // tricky here", " still comment ", " and comment ", "closing // */ code");
    test(0, "/* open // tricky here", " still comment ", " and comment ", "closing */  // code");

  }

  public void stringsVsComments() {
    test(2, "\"string/* \"", " not a comment !");
    test(1, "/* comment", "\" not a string */", "so this is code");
    test(1, "/* comment //\" not a string ! */", "so this is code");
    test(1, "/* \"string*/\"", "not a comment !");
  }

  private void test(int expected, String... lines) {
    JavaLineCounter lineCounter = new JavaLineCounter();
    for (String line : lines) {
      lineCounter.feedLine(line);
    }
    Assertions.assertEquals(expected, lineCounter.javaLineCount());
  }

}
