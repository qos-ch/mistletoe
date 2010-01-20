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
package ch.qos.mistletoe.sample;

import org.junit.Test;

public class MyIntegration1 {

  @Test
  public void smoke1() {
    System.out.println("MyIntegration1.smoke1");
  }
  
  @Test
  public void smoke2() {
    System.out.println("MyIntegration1.smoke2");
  }
}
