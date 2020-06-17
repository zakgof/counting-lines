package com.zakgof.linecount;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

  /**
   * CLI entry point.
   *
   * @param args Exactly one argument required: folder or file to scan
   */
  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("Usage: linecount <folder_or_file>");
      return;
    }
    File file = new File(args[0]);
    StringBuffer sb = new StringBuffer();
    scan(file, sb, 0);
    System.out.println(sb.toString());
  }

  private static int scan(File file, StringBuffer report, int level) {
    try {
      if (file.isFile()) {
        int lines = scanSingleFile(file);
        appendReport(report, level, file, lines);
        return lines;
      } else if (file.isDirectory()) {
        return scanDirectory(file, report, level);
      } else {
        report.append("Not a file or directory: ").append(file).append("\n");
      }
    } catch (IOException e) {
      report.append("Error in ").append(file).append(" : ").append(e.getMessage()).append("\n");
    }
    return 0;
  }

  private static int scanDirectory(File file, StringBuffer report, int level) {
    File[] files = file.listFiles();
    int totalLines = 0;
    StringBuffer childrenReport = new StringBuffer();
    for (File child : files) {
      totalLines += scan(child, childrenReport, level + 1);
    }
    appendReport(report, level, file, totalLines);
    report.append(childrenReport.toString());
    return totalLines;
  }

  private static int scanSingleFile(File file) throws IOException {
    JavaLineCounter lineCounter = new JavaLineCounter();
    try (Scanner scanner = new Scanner(file)) {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        lineCounter.feedLine(line);
      }
    }
    return lineCounter.javaLineCount();
  }

  private static void appendReport(StringBuffer report, int level, File file, int lines) {
    // generate n*2 spaces
    String indent = new String(new char[level * 2]).replace('\0', ' ');
    report.append(indent).append(file.getName()).append(" : ").append(lines).append("\n");
  }

}
