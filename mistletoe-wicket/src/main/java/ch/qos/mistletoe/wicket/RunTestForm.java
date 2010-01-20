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
package ch.qos.mistletoe.wicket;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;

import ch.qos.mistletoe.core.MistletoeCore;
import ch.qos.mistletoe.core.TestReport;

class RunTestForm extends Form<String> {
  private static final long serialVersionUID = 1L;

  TestReportPage parent;

  RunTestForm(String id, TestReportPage parent) {
    super(id);
    this.parent = parent;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void onSubmit() {
    TextField<String> tf = (TextField<String>) get(Constants.TEST_CLASS_NAME_ID);
    String targetClassStr = (String) tf.getDefaultModelObject();
    MistletoeCore mCore = null;
    try {
      mCore = new MistletoeCore(targetClassStr);
    } catch (ClassNotFoundException e) {
      error("Failed to find class " + targetClassStr);
      return;
    }

    TestReport rootReport = mCore.run();
    rootReport = TestReport.getFistChildIfNecessary(rootReport);
    TestReportPanel nodePanel = new TestReportPanel(Constants.NODE_ID,
        rootReport);
    parent.remove(Constants.NODE_ID);
    parent.add(nodePanel);

    parent.remove(Constants.SUMMARY_ID);
    SummaryMarkupContainer summaryContainer = new SummaryMarkupContainer(
        Constants.SUMMARY_ID, rootReport);
    parent.add(summaryContainer);
  }


}
