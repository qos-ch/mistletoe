package ch.qos.mistletoe.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import ch.qos.mistletoe.core.MistletoeCore;
import ch.qos.mistletoe.core.TestReport;
import ch.qos.mistletoe.core.helper.ExceptionHelper;

public class MistletoeServlet extends HttpServlet {

  static String TEST_CLASS_NAME_KEY = "testClassName";
  private static final long serialVersionUID = 8651798172023005153L;
  String defaultTestClassName;

  static final String TEST_OK_GIF = "images/testok.gif";
  static final String TEST_ERROR_GIF = "images/testerr.gif";

  static final String TSUITE_OK_GIF = "images/tsuiteok.gif";
  static final String TSUITE_ERROR_GIF = "images/tsuiteerror.gif";

  public void init(ServletConfig config) {
    defaultTestClassName = config.getInitParameter(TEST_CLASS_NAME_KEY);

  }

  public void service(ServletRequest request, ServletResponse response)
      throws IOException {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

    String testClassName = request.getParameter(TEST_CLASS_NAME_KEY);
    if (testClassName == null) {
      testClassName = defaultTestClassName;
    }

    out.print("<form>");
    out.println();
    out.print("Name of suite or test to run:");
    out.println();
    out.print("<input name=\"" + TEST_CLASS_NAME_KEY
        + "\" type=\"text\" size=\"70\" vale=\"" + testClassName + "\"/>");
    out.println();
    out.print("<input type=\"submit\" value=\"Run\"/>");
    out.println();

    out.print("</form>");
    out.println();

    if (testClassName == null || testClassName.length() == 0) {
      out.println("Enter target class name to run.");
    } else {
      TestReport testReport = run(out, testClassName);
      if (testReport != null) {
        printAsTable("  ", out, testReport);
      }
    }
    out.close();
  }

  void print(PrintWriter out, String msg) {
    out.print(msg);
    out.println();
  }

  void printResultImage(String indent, PrintWriter out, TestReport testReport) {
    boolean hasFailures = testReport.hasFailures();
    boolean isSuite = testReport.isSuite();

    String testResultSrc = null;
    if (isSuite) {
      if (hasFailures) {
        testResultSrc = TSUITE_ERROR_GIF;
      } else {
        testResultSrc = TSUITE_OK_GIF;
      }
    } else {
      if (hasFailures) {
        testResultSrc = TEST_ERROR_GIF;
      } else {
        testResultSrc = TEST_OK_GIF;
      }
    }
    print(out, indent + "<img border=\"0\" src=\"" + testResultSrc + "\"/>");
  }

  void printPayload(String indent, PrintWriter out, TestReport testReport) {
    if (testReport.isTest() && testReport.getThrowable() == null) {
      handleSimple_OK_Description(indent, out);
    } else if (testReport.getThrowable() != null) {
      handle_NOT_OK_Description(indent, out, testReport);
    } else {
      handleDescriptionWithChildren(indent, out, testReport);
    }
  }

  void handleSimple_OK_Description(String indent, PrintWriter out) {
    print(out, "");
  }

  void handle_NOT_OK_Description(String indent, PrintWriter out,
      TestReport testReport) {
    Throwable t = testReport.getThrowable();
    ExceptionHelper ex = new ExceptionHelper(t);
    out.print("<pre>");
    out.println(ex.asString());
    out.println("</pre>");
  }

  void handleDescriptionWithChildren(String indent, PrintWriter out,
      TestReport testReport) {
    for (TestReport child : testReport.getChildren()) {
      printAsTable(indent + "  ", out, child);
    }
  }

  void printAsTable(String indent, PrintWriter out, TestReport testReport) {

    print(out, indent + "<table>");
    print(out, indent + "  <tr>");

    print(out, indent + "    <td>");
    printResultImage(indent + "  ", out, testReport);
    print(out, indent + "    </td>");

    print(out, indent + "    <td>" + testReport.getDisplayName() + "</td>");
    print(out, indent + "  </tr>");

    print(out, indent + "  <tr>");
    print(out, indent + "    <td></td>");
    print(out, indent + "    <td>");
    printPayload(indent + "  ", out, testReport);
    print(out, indent + "    </td>");

    print(out, indent + "  </tr>");

    print(out, indent + "</table>");
  }

  TestReport run(PrintWriter out, String targetClassStr) {
    MistletoeCore mCore = null;
    try {
      mCore = new MistletoeCore(targetClassStr);
    } catch (ClassNotFoundException e) {
      out.print("Failed to load class [" + targetClassStr+"]");
      return null;
    }

    TestReport rootReport = mCore.run();
    rootReport = TestReport.getFistChildIfNecessary(rootReport);
    return rootReport;
  }

}