package ch.qos.mistletoe.servlet;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import ch.qos.mistletoe.core.TestReport;
import ch.qos.mistletoe.core.helper.ExceptionHelper;

public class TestReportPrinter {
  static final String TEST_OK_GIF = "/images/testok.gif";
  static final String TEST_ERROR_GIF = "/images/testerr.gif";

  static final String TSUITE_OK_GIF = "/images/tsuiteok.gif";
  static final String TSUITE_ERROR_GIF = "/images/tsuiteerror.gif";

  HttpServletRequest request;
  PrintWriter out;

  TestReportPrinter(HttpServletRequest request, PrintWriter out) {
    this.request = request;
    this.out = out;
  }

  private void print(String msg) {
    out.print(msg);
    out.println();
  }

  private  void printResultImage(String indent, TestReport testReport) {
    boolean hasFailures = testReport.hasFailures();
    boolean isSuite = testReport.isSuite();

    String testResultSrc = null;
    if (isSuite) {
      if (hasFailures) {
        testResultSrc = adjusted(TSUITE_ERROR_GIF);
      } else {
        testResultSrc = adjusted(TSUITE_OK_GIF);
      }
    } else {
      if (hasFailures) {
        testResultSrc = adjusted(TEST_ERROR_GIF);
      } else {
        testResultSrc = adjusted(TEST_OK_GIF);
      }
    }
    print(indent + "<img width=\"16px\" height=\"16px\" border=\"0\" src=\""
        + testResultSrc + "\"/>");
  }

  void printHeader() {
    print("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Strict//EN\">");
    print("<html>");
    print("<header>");
    print("  <style type=\"text/css\">");
    print("body {");
    print("  text-align: left;");
    print("  margin-left: 2ex;");
    print("  margin-right: 2ex;");
    print("  padding-left: 0px;");
    print("  padding-right: 0px;  ");
    print("}");
    print("td.icon {");
    print("  width: 16px;");
    print("}");
    print("");
    print(".description {");
    print("  padding-left: 1ex;");
    print("  text-align: left;  ");
    print("  width: 50em;");
    print("}");
    print("");
    print(".summaryBox {");
    print("  border: 1px inset #ccc;");
    print("  width: 40em;");
    print("}");
    print("");
    print(".exception {");
    print("  white-space: pre;");
    print("  margin-left: 1ex;  ");
    print("  background: #EEE; ");
    print("  font-family: courier, monospace;");
    print("  font-size: x-small;");
    print("  max-width: 180ex;");
    print("}");
    print("  </style>");

    print("");
    print("</header>");
  }

  void printFooter() {
    print("</body>");
    print("</html>");
  }

  void printAsTable(String indent, TestReport testReport) {

    print(indent + "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
    print(indent + "  <tr>");

    print(indent + "    <td>");
    printResultImage(indent + "  ", testReport);
    print(indent + "    </td>");

    print(indent + "    <td class=\"description\">" + testReport.getDisplayName() + "</td>");
    print(indent + "  </tr>");

    print(indent + "  <tr>");
    print(indent + "    <td></td>");
    print(indent + "    <td>");
    printPayload(indent + "  ", testReport);
    print(indent + "    </td>");

    print(indent + "  </tr>");

    print(indent + "</table>");
  }
  
  private void printPayload(String indent, TestReport testReport) {
    if (testReport.isTest() && testReport.getThrowable() == null) {
      handleSimple_OK_Description(indent);
    } else if (testReport.getThrowable() != null) {
      handle_NOT_OK_Description(indent, testReport);
    } else {
      handleDescriptionWithChildren(indent, testReport);
    }
  }

  private void handleSimple_OK_Description(String indent) {
    print("");
  }

  private void handle_NOT_OK_Description(String indent, TestReport testReport) {
    Throwable t = testReport.getThrowable();
    ExceptionHelper ex = new ExceptionHelper(t);
    out.print("<pre class=\"exception\">");
    out.println(ex.asString());
    out.println("</pre>");
  }

  private void handleDescriptionWithChildren(String indent, TestReport testReport) {
    for (TestReport child : testReport.getChildren()) {
      printAsTable(indent + "  ", child);
    }
  }

  void printSummary(TestReport testReport) {
    print("<p/>");
    print("<table>");
    print("  <tr>");
    print("    <td>Tests: " + testReport.getTestCount() + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Errors: " + testReport.getTotalFailures()+"</td>");
    print("    <td width=\"50%\"/>");
    print("   </tr>");
    print("   <tr>");
    print("     <td colspan=\"2\" class=\"summaryBox\" "
        + getSummaryBoxStyle(testReport) + ">&nbsp;</td>");
    print("   </tr>");
    print("   <tr><td style=\"line-height: 5px;\">&nbsp;</td></tr>");
    print("</table>");
  }

  private String getSummaryBoxStyle(TestReport testReport) {
    String backgroundColor = TestReport.SUCCESS_COLOR;
    if (testReport.hasFailures()) {
      backgroundColor = TestReport.FAILURE_COLOR;
    }
    return "style=\"background: " + backgroundColor + ";\"";
  }



  private String adjusted(String resource) {
    return request.getContextPath() + resource;
  }
}
