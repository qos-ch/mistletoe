package ch.qos.mistletoe.core;

import java.util.HashMap;
import java.util.Map;

import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

public class StopWatchRunListener extends RunListener {

  Map<Description, StopWatch> map = new HashMap<Description, StopWatch>();

  long getRunTime(Description d) {
    StopWatch sw = map.get(d);
    if (sw == null) {
      return -1;
    } else {
      return sw.getDiff();
    }
  }

  /**
   * Called when an atomic test is about to be started.
   * 
   * @param description
   *          the description of the test that is about to be run (generally a
   *          class and method name)
   */
  public void testStarted(Description description) throws Exception {
    StopWatch sw = map.get(description);
    if (sw == null) {
      sw = new StopWatch();
      map.put(description, sw);
    }
    sw.start = System.currentTimeMillis();
  }

  /**
   * Called when an atomic test has finished, whether the test succeeds or
   * fails.
   * 
   * @param description
   *          the description of the test that just ran
   */
  public void testFinished(Description description) throws Exception {
    StopWatch sw = map.get(description);
    sw.end = System.currentTimeMillis();
  }
}
