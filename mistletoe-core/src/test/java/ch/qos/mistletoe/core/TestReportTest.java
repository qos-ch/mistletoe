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
package ch.qos.mistletoe.core;

import org.junit.Test;

import ch.qos.mistletoe.sample.MyCollection;


public class TestReportTest {

  
  @Test
  public void smoke() {
    MistletoeCore mCore = new MistletoeCore(MyCollection.class);
    TestReport report = mCore.run();
    dump("", report);
  }
  
  void dump(String indentation, TestReport tr) {
    System.out.println(indentation + tr);
    for(TestReport child: tr.getChildren()) {
      dump(indentation + "   ", child);
    }
  }
}
