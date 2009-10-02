package ch.qos.mistletoe.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Result;

import ch.qos.mistletoe.sample.MyCollection;

public class MistletoeCoreTest {

  
  @Test
  public void smoke() {
    MistletoeCore mCore = new MistletoeCore(MyCollection.class);
    mCore.run();
    
    Result result = mCore.getResult();
    Description description = mCore.getDescription();
    
    assertEquals(4, description.testCount());
    assertEquals(1, result.getFailureCount());
    
    MistletoeCore.dumpDescription(mCore.getStopWatchRunListener(), mCore.getDescription());
    MistletoeCore.dumpResult(mCore.getResult());
  }
}
