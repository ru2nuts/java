package com.visiblemeasures.util;

import org.apache.hadoop.fs.FileContext;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

public class HdfsSymLink {

  public static void main(String[] args) throws IOException {
    if (args == null || args.length == 0) {
      System.out.println("HdfsSymLink <target_path> <symlink_path>");
      System.exit(1);
    }
    String source = args[0];
    String ln = args[1];

    System.out.printf("Target path: %s, symlink path: %s", source, ln);
    System.out.println();

    Path srcPath = new Path(source);
    Path lnPath = new Path(ln);

    FileContext fc = FileContext.getFileContext();
    fc.createSymlink(srcPath, lnPath, false);

    System.out.println("Done");
  }
}
