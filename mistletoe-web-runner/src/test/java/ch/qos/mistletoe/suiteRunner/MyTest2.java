package ch.qos.mistletoe.suiteRunner;

import static org.junit.Assert.fail;

import org.junit.Test;

public class MyTest2 {

  @Test
  public void smoke1() throws InterruptedException {
    Thread.sleep(10);
    System.out.println("MyTest2.smoke1");
  }
  
  @Test
  public void smoke2() {
    System.out.println("MyTest2.smoke2");
    fail("fail");
  }
}
