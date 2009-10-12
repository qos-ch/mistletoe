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

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;

import ch.qos.mistletoe.core.MistletoeCore;
import ch.qos.mistletoe.core.TestReport;

public class TreePage extends WebPage {

  public TreePage() {

    WebApplication application = (WebApplication) getApplication();
    String originalTargetClassStr = application.getInitParameter("testTarget");

    Form<String> form = new Form<String>("form") {
      private static final long serialVersionUID = 1L;

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
          e.printStackTrace();
        }

        TestReport rootReport = mCore.run();

        rootReport = getFistChildIfNecessary(rootReport);
        TestReportPanel nodePanel = new TestReportPanel("node", rootReport);
        TreePage.this.remove("node");
        TreePage.this.add(nodePanel);
      }
    };
    add(form);
    form.add(new TextField<String>("testClassName", new Model<String>(originalTargetClassStr)));

    EmptyPanel emptyPanel = new EmptyPanel(Constants.NODE);
    
    
    //TestReportPanel nodePanel = new TestReportPanel("node", rootReport);
    add(emptyPanel);
  }

  public void init() {

  }

  TestReport getFistChildIfNecessary(TestReport description) {
    if (description.getDisplayName().equals("null")) {
      return description.getChildren().get(0);
    } else {
      return description;
    }
  }
}
