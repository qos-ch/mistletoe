package ch.qos.mistletoe.core;

public class StopWatch {
  long start;
  long end;
  
  public long getDiff() {
    return end-start;
  }
}
