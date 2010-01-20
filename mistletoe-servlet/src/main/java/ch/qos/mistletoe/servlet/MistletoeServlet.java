package ch.qos.mistletoe.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import ch.qos.mistletoe.core.MistletoeCore;
import ch.qos.mistletoe.core.TestReport;

public class MistletoeServlet extends HttpServlet {
  private static final long serialVersionUID = 8651798172023005153L;

  static String DEFAULT_TEST_SUITE_KEY = "defaultTestSuite";
  static String TEST_CLASS_NAME_KEY = "testClassName";
  String defaultTestClassName;

  public void init(ServletConfig config) {
    defaultTestClassName = trim(config.getInitParameter(DEFAULT_TEST_SUITE_KEY));
  }

  boolean isEmpty(String s) {
    return s == null || s.length() == 0;
  }
  
  String trim(String s) {
    if(s == null) {
      return null;
    } else {
      return s.trim();
    }
  }
  
  public void service(ServletRequest req, ServletResponse response)
      throws IOException {
    response.setContentType("text/html");

    HttpServletRequest request = (HttpServletRequest) req;
    
    PrintWriter out = response.getWriter();
    TestReportPrinter trp = new TestReportPrinter(request, out);
    
    trp.printHeader();
    String testClassName = trim(request.getParameter(TEST_CLASS_NAME_KEY));
    
    if (isEmpty(testClassName)) {
      testClassName = defaultTestClassName;
    }
    
    out.print("<form>");
    out.println();
    out.print("Name of suite or test to run:");
    out.println();
    out.print("<input name=\"" + TEST_CLASS_NAME_KEY
        + "\" type=\"text\" size=\"70\" value=\"" + testClassName + "\"/>");
    out.println();
    out.print("<input type=\"submit\" value=\"Run\"/>");
    out.println();

    out.print("</form>");
    out.println();

    if (testClassName == null || testClassName.length() == 0) {
      out.println("Enter target class name to run.");
    } else {
      testClassName = testClassName.trim();
      TestReport testReport = run(out, testClassName);
      if (testReport != null) {
        trp.printSummary(testReport);
        trp.printAsTable("  ", testReport);
      }
    }
    trp.printFooter();
    out.close();
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