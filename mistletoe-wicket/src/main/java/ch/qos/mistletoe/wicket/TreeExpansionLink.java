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
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListView;

public class TreeExpansionLink extends AjaxFallbackLink<Object> {
  private static final long serialVersionUID = 1L;
  boolean expanded = true;

  static String EXPAND_GIF = "images/expand.gif";
  static String COLLAPSE_GIF = "images/collapse.gif";

  public TreeExpansionLink(String id) {
    super(id);
    ResourceReference ref = getControlSymbolResourceReference(expanded);
    Image image = new Image(Constants.TREE_CONTROL_SYMBOL_ID, ref);
    image.setOutputMarkupId(true);

    this.add(image);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void onClick(AjaxRequestTarget target) {
    TestReportPanel nodePanel = (TestReportPanel) getParent();
    if(nodePanel == null || nodePanel.testReport == null) {
      warn("Failed to find node panel");
      return;
    }
    
    if (nodePanel.testReport.isSuite()) {
      expanded = !expanded;
      System.out.println("expanded=" + expanded);

      TreeExpansionLink link = (TreeExpansionLink) nodePanel
          .get(Constants.TREE_CONTROL_ID);

      target.addComponent(link.getParent());

      Image image = (Image) link.get(Constants.TREE_CONTROL_SYMBOL_ID);
      ResourceReference ref = getControlSymbolResourceReference(expanded);
            image.setImageResourceReference(ref);

      ListView<Node> payloadNode = (ListView<Node>) nodePanel
          .get(Constants.PAYLOAD_ID);
      payloadNode.setVisible(expanded);

      // can't update a ListView
      target.addComponent(payloadNode.getParent());
    }
  }

  ResourceReference getControlSymbolResourceReference(boolean expanded) {
    String raw = EXPAND_GIF;
    if (expanded) {
      raw = COLLAPSE_GIF;
    }
    return new ResourceReference(TestReportPanel.class, raw);
  }
}
