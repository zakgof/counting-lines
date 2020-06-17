package com.zakgof.linecount.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.zakgof.linecount.JavaLineCounter;

public class SingleLineTest {

  @Test
  public void simple() {
    test("", false);
    test("  ", false);
    test("  hello  ", true);
  }

  @Test
  public void doubleSlashComments() {
    test("// comment", false);
    test("   // comment", false);
    test("   ///// //// // comment", false);
    test("some java  // comment", true);
  }

  @Test
  public void slashAsteriskComments() {
    test("/* comment */", false);
    test("/* comment */   ", false);
    test("   /* comment */", false);
    test("   /* comment */  /* comment2 */  ", false);

    test("java before /* comment */", true);
    test("/* comment */ java after", true);
    test("/* comment */ java between /* comment2 */", true);
  }

  @Test
  public void mixedComments() {
    test("/* comment */  // more comment", false);
    test("/* comment */  /* comment2*/  // trailing", false);
    test("  /* comment */  // more comment", false);
    test("  /* comment */  /* comment2*/  // trailing", false);
    test("// something   /* comment */", false);

    test("java before /* comment */ more code // and trailing", true);
    test("/* comment */ then java // and comment", true);
    test("/* comment // more comment */ and java", true);

    test("// /* tricky */ still comment */ and still comment", false);
  }

  private void test(String line, boolean expected) {
    JavaLineCounter lineCounter = new JavaLineCounter();
    boolean isJavaLine = lineCounter.feedLine(line);
    Assertions.assertEquals(expected, isJavaLine);
  }

}
