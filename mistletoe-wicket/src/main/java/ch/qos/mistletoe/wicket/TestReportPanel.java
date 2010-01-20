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

import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;

import ch.qos.mistletoe.core.MistletoeCore;
import ch.qos.mistletoe.core.TestReport;
import ch.qos.mistletoe.core.helper.ExceptionHelper;

public class TestReportPanel extends Panel {
  private static final long serialVersionUID = 2645889186544792364L;

  transient MistletoeCore core;
  transient TestReport testReport;

  public TestReportPanel(String id, TestReport testReport) {
    super(id);
    this.testReport = testReport;

    if (testReport.isTest()) {
      handleBlankPlaceHolderImage();
    } else {
      TreeExpansionLink link = new TreeExpansionLink(Constants.TREE_CONTROL_ID);
      add(link);
    }

    handleResultImage(testReport);
    add(new Label(Constants.NAME_ID, testReport.getDisplayName()));

    if (testReport.isTest() && testReport.getThrowable() == null) {
      handleSimple_OK_Description();
    } else if (testReport.getThrowable() != null) {
      handle_NOT_OK_Description();
    } else {
      handleDescriptionWithChildren();
    }
    
    setOutputMarkupId(true);
  }

  void handleDescriptionWithChildren() {
    ListView<TestReport> listView = new ListView<TestReport>(
        Constants.PAYLOAD_ID, testReport.getChildren()) {
      private static final long serialVersionUID = 1L;

      @Override
      protected void populateItem(ListItem<TestReport> item) {
        TestReport childNode = item.getModelObject();
        item.add(new TestReportPanel(Constants.NODE_ID, childNode))
            .setOutputMarkupId(true);
      }
    };
    listView.setOutputMarkupId(true);
    add(listView);
  }

  void handleSimple_OK_Description() {
    final WebMarkupContainer parent = new WebMarkupContainer(Constants.PAYLOAD_ID);
    add(parent);
    parent.add(new EmptyPanel(Constants.NODE_ID).setVisible(false));
  }

  void handle_NOT_OK_Description() {
    Throwable t = testReport.getThrowable();

    ExceptionHelper ex = new ExceptionHelper(t);
    
    
    final WebMarkupContainer parent = new WebMarkupContainer(Constants.PAYLOAD_ID);
    add(parent);
    Label exception = new Label(Constants.NODE_ID, ex.asString());
    exception.setEscapeModelStrings(false);
    exception.add(new SimpleAttributeModifier("class", "exception"));
    parent.add(exception);
    if (ex.getLines() > 50) {
      System.out.println("ex.getLines() > 50");
      SimpleAttributeModifier sam = new SimpleAttributeModifier("style",
          "height: 40em; overflow: scroll;");
      exception.add(sam);
    }
  }

  void handleBlankPlaceHolderImage() {
    final WebMarkupContainer parent = new WebMarkupContainer(
        Constants.TREE_CONTROL_ID);
    // we don't want the "hand" cursor to appear over the blank place holder
    // image
    parent.add(new SimpleAttributeModifier("style", "cursor: default;"));

    Image image = new Image(Constants.TREE_CONTROL_SYMBOL_ID,
        new ResourceReference(TestReportPanel.class, Constants.BLANK_GIF));
    parent.add(image);
    add(parent);
  }
  
  void handleResultImage(TestReport description) {
    boolean hasFailures = description.hasFailures();
    boolean isSuite = description.isSuite();

    String testResultSrc = null;
    if (isSuite) {
      if (hasFailures) {
        testResultSrc = Constants.TSUITE_ERROR_GIF;
      } else {
        testResultSrc = Constants.TSUITE_OK_GIF;
      }
    } else {
      if (hasFailures) {
        testResultSrc = Constants.TEST_ERROR_GIF;
      } else {
        testResultSrc = Constants.TEST_OK_GIF;
      }
    }

    Image image = new Image(Constants.IMAGE_ID, new ResourceReference(
        TestReportPanel.class, testResultSrc));
    add(image);
  }
}
