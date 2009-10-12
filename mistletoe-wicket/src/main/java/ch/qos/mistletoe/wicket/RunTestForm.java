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

  TreePage parent;
  
  RunTestForm(String id, TreePage parent) {
    super(id);
    this.parent = parent;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void onSubmit() {
    System.out.println("form was submitted!");
    TextField<String> tf = (TextField<String>) get("testClassName");
    String targetClassStr = (String) tf.getDefaultModelObject();
    MistletoeCore mCore = null;
    try {
      mCore = new MistletoeCore(targetClassStr);
    } catch (ClassNotFoundException e) {
      error("Failed to find class "+targetClassStr);
      return;
    }

    TestReport rootReport = mCore.run();
    rootReport = getFistChildIfNecessary(rootReport);
    TestReportPanel nodePanel = new TestReportPanel("node", rootReport);
    parent.remove("node");
    parent.add(nodePanel);
  }
  
  TestReport getFistChildIfNecessary(TestReport description) {
    if (description.getDisplayName().equals("null")) {
      return description.getChildren().get(0);
    } else {
      return description;
    }
  }
}
