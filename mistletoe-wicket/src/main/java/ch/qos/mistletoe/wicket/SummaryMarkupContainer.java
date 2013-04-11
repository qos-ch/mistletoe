package ch.qos.mistletoe.wicket;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;

import ch.qos.mistletoe.core.TestReport;

public class SummaryMarkupContainer extends WebMarkupContainer
{

	private static final long serialVersionUID = -7378945281812473957L;

	SummaryMarkupContainer(final String id, final TestReport testReport)
	{
		super(id);

		if (testReport == null)
		{
			this.handleNullTestReport();
		}
		else
		{
			this.handleTestReport(testReport);
		}
	}


	void handleNullTestReport()
	{
		final Label runsSummary = new Label(Constants.RUNS_SUMMARY_ID, "");
		this.add(runsSummary);

		final Label errorsSummary = new Label(Constants.ERRORS_SUMMARY_ID, "");
		this.add(errorsSummary);

		final Label summaryInColor = new Label(Constants.SUMMARY_IN_COLOR_ID, "");
		this.add(summaryInColor);
		this.setVisible(false);
	}

	void handleTestReport(final TestReport testReport)
	{
		System.out.println("handleTestReport**");
		final Label runsSummary = new Label(Constants.RUNS_SUMMARY_ID, "Total tests: "
				+ testReport.getTestCount());
		this.add(runsSummary);

		final Label errorsSummary = new Label(Constants.ERRORS_SUMMARY_ID, "Errors/Failures: "
				+ testReport.getTotalFailures());
		this.add(errorsSummary);

		final Label summaryInColor = new Label(Constants.SUMMARY_IN_COLOR_ID, "&nbsp;");
		summaryInColor.setEscapeModelStrings(false);
		String summaryColor = TestReport.SUCCESS_COLOR;
		if (testReport.hasFailures())
		{
			summaryColor = TestReport.FAILURE_COLOR;
		}
		summaryInColor.add(new AttributeModifier("style", "background: " + summaryColor + ";"));
		this.add(summaryInColor);
	}

}
