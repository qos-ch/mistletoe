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
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;

public class TreePage extends WebPage {


  public TreePage() {
    WebApplication application = (WebApplication) getApplication();
    String originalTargetClassStr = application.getInitParameter("testTarget");

    add(new FeedbackPanel("feedback"));
    
    Form<String> form = new RunTestForm("form", this);
    add(form);
    form.add(new TextField<String>("testClassName", new Model<String>(originalTargetClassStr)));

    EmptyPanel emptyPanel = new EmptyPanel(Constants.NODE);
    add(emptyPanel);
  }

  public void init() {

  }

  

}
