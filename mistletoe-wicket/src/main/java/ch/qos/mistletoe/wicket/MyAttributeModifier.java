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

import org.apache.wicket.Component;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.ComponentTag;

public class MyAttributeModifier extends SimpleAttributeModifier {


  private static final long serialVersionUID = 1L;
  String value;
  String attribute;
  
  public MyAttributeModifier(String attribute, String value) {
    super(attribute, value);
    this.value = value;
    this.attribute = attribute;
  }

  @Override
  public void onComponentTag(final Component component, final ComponentTag tag) {
    System.out.println("ononComponentTag called. component="+component+", tag="+tag);
    if (isEnabled(component)) {
      System.out.println("changing attribute, value="+value);
      tag.getAttributes().put(attribute, value);
    }
  }

  public void setValue(String value) {
    this.value = value;
  }
}
