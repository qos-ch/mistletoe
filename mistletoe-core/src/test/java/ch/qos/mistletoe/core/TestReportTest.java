package ch.qos.mistletoe.core;

import org.junit.Test;
import org.junit.runner.Description;

import ch.qos.mistletoe.sample.MyCollection;


public class TestReportTest {

  
  @Test
  public void smoke() {
    MistletoeCore mCore = new MistletoeCore(MyCollection.class);
    mCore.run();
    
    Description description = mCore.getDescription();
    
  }
  
  void dump(String indentation, TestReport tr) {
    System.out.println(indentation + tr);
    for(TestReport child: tr.getChildren()) {
      dump(indentation + "   ", child);
    }
  }
}
