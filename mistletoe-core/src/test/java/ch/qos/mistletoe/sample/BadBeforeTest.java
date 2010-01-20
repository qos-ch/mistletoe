package ch.qos.mistletoe.sample;

import org.junit.BeforeClass;
import org.junit.Test;


public class BadBeforeTest {

  @BeforeClass
  static public void badBefore() {
    throw new IllegalStateException("testing");
  }
  
  @Test
  public void smoke() {
    
  }
  
}
