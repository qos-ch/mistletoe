package ch.qos.mistletoe.wicket;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;

import ch.qos.mistletoe.core.TestReport;

public class SummaryMarkupContainer extends WebMarkupContainer {

  private static final long serialVersionUID = -7378945281812473957L;

  SummaryMarkupContainer(String id, TestReport testReport) {
    super(id);

    if (testReport == null) {
      handleNullTestReport();
    } else {
      handleTestReport(testReport);
    }
  }


  void handleNullTestReport() {
    Label runsSummary = new Label(Constants.RUNS_SUMMARY_ID, "");
    add(runsSummary);
    
    Label errorsSummary = new Label(Constants.ERRORS_SUMMARY_ID, "");
    add(errorsSummary);

    Label summaryInColor = new Label(Constants.SUMMARY_IN_COLOR_ID, "");
    add(summaryInColor);
    setVisible(false);
  }
  
  void handleTestReport(TestReport testReport) {
    System.out.println("handleTestReport**");
    Label runsSummary = new Label(Constants.RUNS_SUMMARY_ID, "Total tests: "
        + testReport.getTestCount());
    add(runsSummary);

    Label errorsSummary = new Label(Constants.ERRORS_SUMMARY_ID,
        "Errors/Failures: " + testReport.getTotalFailures());
    add(errorsSummary);

    Label summaryInColor = new Label(Constants.SUMMARY_IN_COLOR_ID, "&nbsp;");
    summaryInColor.setEscapeModelStrings(false);
    String summaryColor = TestReport.SUCCESS_COLOR;
    if (testReport.hasFailures()) {
      summaryColor = TestReport.FAILURE_COLOR;
    }
    summaryInColor.add(new SimpleAttributeModifier("style", "background: "
        + summaryColor + ";"));
    add(summaryInColor);
  }

}
