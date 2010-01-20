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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Result;

import ch.qos.mistletoe.sample.MyCollection;

public class MistletoeCoreTest {

//  public static void main(String[] args) {
//    MistletoeCore mCore = new MistletoeCore(MyCollection.class);
//    mCore.run();
//    dumpDescription(mCore.swRunListener, mCore.description);
//    dumpResult(mCore.result);
//  }
  
  @Test
  public void smoke() {
    MistletoeCore mCore = new MistletoeCore(MyCollection.class);
    mCore.run();
    
    Result result = mCore.getResult();
    Description description = mCore.getDescription();
    
    assertEquals(4, description.testCount());
    assertEquals(1, result.getFailureCount());
    
    System.out.println();
    System.out.println("=============== DUMP ============");
    System.out.println();
    MistletoeCore.dumpDescription(mCore.getStopWatchRunListener(), mCore.getDescription());
    //MistletoeCore.dumpResult(mCore.getResult());
  }
  
  @Test
  public void hasAssociatedFailures() {
    MistletoeCore mCore = new MistletoeCore(MyCollection.class);
    mCore.run();
    Description description = mCore.getDescription();
    
    assertTrue(mCore.hasAssociatedFailures(description));
    
    Map<Description, Boolean> map = new HashMap<Description, Boolean>();
    doCheck(map, mCore, description);
    for(Description d: map.keySet()) {
      System.out.println(d +", "+map.get(d));
    }
  }
  
  void doCheck(Map<Description, Boolean> map, MistletoeCore mCore, Description d) {
    boolean withFailures = mCore.hasAssociatedFailures(d);
    map.put(d, withFailures);
    for(Description c: d.getChildren()) {
      doCheck(map, mCore, c);
    }
    
  }
}
