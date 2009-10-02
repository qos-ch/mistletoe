package ch.qos.mistletoe.suiteRunner;

public class Timer {
  long start;
  long end;
  
  public long getDiff() {
    return end-start;
  }
}
