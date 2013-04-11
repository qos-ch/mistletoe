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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

public class TreeExpansionLink extends AjaxFallbackLink<Object>
{
	private static final long serialVersionUID = 1L;
	boolean expanded = true;

	static String EXPAND_GIF = "images/expand.gif";
	static String COLLAPSE_GIF = "images/collapse.gif";

	public TreeExpansionLink(final String id)
	{
		super(id);
		final ResourceReference ref = this.getControlSymbolResourceReference(this.expanded);
		final Image image = new Image(Constants.TREE_CONTROL_SYMBOL_ID, ref);
		image.setOutputMarkupId(true);

		this.add(image);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onClick(final AjaxRequestTarget target)
	{
		final TestReportPanel nodePanel = (TestReportPanel)this.getParent();
		if ((nodePanel == null) || (nodePanel.testReport == null))
		{
			this.warn("Failed to find node panel");
			return;
		}

		if (nodePanel.testReport.isSuite())
		{
			this.expanded = !this.expanded;
			System.out.println("expanded=" + this.expanded);

			final TreeExpansionLink link = (TreeExpansionLink)nodePanel
					.get(Constants.TREE_CONTROL_ID);

			target.add(link.getParent());

			final Image image = (Image)link.get(Constants.TREE_CONTROL_SYMBOL_ID);
			final ResourceReference ref = this.getControlSymbolResourceReference(this.expanded);
			image.setImageResourceReference(ref);

			final ListView<Node> payloadNode = (ListView<Node>)nodePanel.get(Constants.PAYLOAD_ID);
			payloadNode.setVisible(this.expanded);

			// can't update a ListView
			target.add(payloadNode.getParent());
		}
	}

	ResourceReference getControlSymbolResourceReference(final boolean expanded)
	{
		String raw = TreeExpansionLink.EXPAND_GIF;
		if (expanded)
		{
			raw = TreeExpansionLink.COLLAPSE_GIF;
		}
		return new PackageResourceReference(TestReportPanel.class, raw);
	}
}
