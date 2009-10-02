package ch.qos.mistletoe.sample;

import static org.junit.Assert.fail;

import org.junit.Test;

public class MyIntegration2 {

  @Test
  public void smoke1() throws InterruptedException {
    Thread.sleep(10);
    System.out.println("MyIntegration2.smoke1");
  }
  
  @Test
  public void smoke2() {
    System.out.println("MyIntegration2.smoke2");
    fail("fail");
  }
}
