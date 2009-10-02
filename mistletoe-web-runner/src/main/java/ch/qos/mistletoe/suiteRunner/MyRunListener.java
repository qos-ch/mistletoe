package ch.qos.mistletoe.suiteRunner;

import java.util.HashMap;
import java.util.Map;

import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

public class MyRunListener extends RunListener {

  
  
  Map<Description, Timer> map = new HashMap<Description, Timer>();
  
  
  long getRunTime(Description d) {
    Timer t = map.get(d);
    return t.getDiff();
  }
  /**
   * Called when an atomic test is about to be started.
   * @param description the description of the test that is about to be run 
   * (generally a class and method name)
   */
  public void testStarted(Description description) throws Exception {
    Timer t = map.get(description);
    if(t == null) {
      t = new Timer();
      map.put(description, t);
    }
    t.start = System.currentTimeMillis();
  }

  /**
   * Called when an atomic test has finished, whether the test succeeds or fails.
   * @param description the description of the test that just ran
   */
  public void testFinished(Description description) throws Exception {
    Timer t = map.get(description);
    t.end = System.currentTimeMillis();
  }
}
