/**
 * Mistletoe: a junit extension for integration testing
 * Copyright (C) 2009, QOS.ch. All rights reserved.
 *
 * This program and the accompanying materials are dual-licensed under
 * either the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation
 *
 *   or (per the licensee's choosing)
 *
 * under the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation.
 */
package ch.qos.mistletoe.core.helper;

public class ExceptionHelper {

  static String xLINE_SEPARATORX = "<br/>\r\n";
  static String LINE_SEPARATOR = "\r\n";
  public final static String CAUSED_BY = "Caused by: ";
  static final String TRACE_PREFIX = "&nbsp;&nbsp;";

  
  final Throwable throwable;
  int lines = 0;
  
  public ExceptionHelper(Throwable throwable) {
    this.throwable = throwable;
  }
  
  
  public int getLines() {
    return lines;
  }

  public String asString() {
    StringBuilder sbuf = new StringBuilder();
    
    Throwable t = this.throwable;
    // there is no parent initially
    Throwable parent = null;
    while (t != null) {
      innetRender(sbuf, parent, t);
      t = t.getCause();
      parent = t;
    }
    return sbuf.toString();
  }

  private void innetRender(StringBuilder sbuf, Throwable parent,
      Throwable t) {
    printFirstLine(sbuf, parent, t);

    int commonFrames = findNumberOfCommonFrames(parent, t);
    StackTraceElement[] stepArray = t.getStackTrace();
    for (int i = 0; i < stepArray.length - commonFrames; i++) {
      StackTraceElement step = stepArray[i];
      sbuf.append(TRACE_PREFIX);
      sbuf.append(escapeTags(step.toString()));
      sbuf.append(LINE_SEPARATOR);
      lines++;
    }

    if (commonFrames > 0) {
      sbuf.append(TRACE_PREFIX);
      sbuf.append("... " + commonFrames).append(" common frames omitted");
      sbuf.append(LINE_SEPARATOR);
      lines++;
    }
  }

  private void printFirstLine(StringBuilder sb, Throwable parent,
      Throwable t) {
    int commonFrames = findNumberOfCommonFrames(parent, t);
    if (commonFrames > 0) {
      sb.append(CAUSED_BY);
    }
    sb.append(t.getClass().getName()).append(": ").append(
        escapeTags(t.getMessage()));
    sb.append(LINE_SEPARATOR);
    lines++;
  }

  static int findNumberOfCommonFrames(Throwable parent, Throwable t) {

    if (parent == null) {
      return 0;
    }

    StackTraceElement[] parentSTEPArray = parent.getStackTrace();
    StackTraceElement[] steArray = t.getStackTrace();

    if (parentSTEPArray == null) {
      return 0;
    }

    int steIndex = steArray.length - 1;
    int parentIndex = parentSTEPArray.length - 1;
    int count = 0;
    while (steIndex >= 0 && parentIndex >= 0) {
      StackTraceElement ste = steArray[steIndex];
      StackTraceElement otherSte = parentSTEPArray[parentIndex];
      if (ste.equals(otherSte)) {
        count++;
      } else {
        break;
      }
      steIndex--;
      parentIndex--;
    }
    return count;
  }

  private static String escapeTags(final String input) {
    // Check if the string is null or zero length -- if so, return
    // what was sent in.
    if ((input == null) || (input.length() == 0)
        || (input.indexOf("<") == -1 && input.indexOf(">") == -1)) {
      return input;
    }

    StringBuffer buf = new StringBuffer(input);
    return escapeTags(buf);
  }

/**
     * This method takes a StringBuilder which may contain HTML tags (ie, &lt;b&gt;,
     * &lt;table&gt;, etc) and replaces any '<' and '>' characters with
     * respective predefined entity references.
     * @param buf
     * @return
     */
  private static String escapeTags(final StringBuffer buf) {
    for (int i = 0; i < buf.length(); i++) {
      char ch = buf.charAt(i);
      if (ch == '<') {
        buf.replace(i, i + 1, "&lt;");
      } else if (ch == '>') {
        buf.replace(i, i + 1, "&gt;");
      }
    }
    return buf.toString();
  }
}
