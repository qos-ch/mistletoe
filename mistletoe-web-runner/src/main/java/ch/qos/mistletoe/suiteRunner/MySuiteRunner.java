package ch.qos.mistletoe.suiteRunner;

import java.util.List;

import org.junit.runner.Computer;
import org.junit.runner.Description;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

public class MySuiteRunner {

  static RunNotifier notifier = new RunNotifier();
  static MyRunListener myListener = new MyRunListener();
  
  public static void main(String[] args) {
    Computer defaultComputer = new Computer();
    Request request = Request.classes(defaultComputer, MyCollection.class);
    Runner runner = request.getRunner();
   
    Result result = run(runner);
    
    Description description = runner.getDescription();
    
    dumpDescription(description);
    dumpResult(result);
  }
  
  public static Result run(Runner runner) {
    Result result = new Result();
    RunListener listener = result.createListener();
    notifier.addFirstListener(listener);
    notifier.addListener(myListener);
    
    try {
      notifier.fireTestRunStarted(runner.getDescription());
      runner.run(notifier);
      notifier.fireTestRunFinished(result);
    } finally {
      notifier.removeListener(listener);
    }
    return result;
  }

  static void dumpDescription(Description description) {
    if(description.isSuite()) {
      dumpSuite(description, "");
    } else {
      dumpTest(description,  "");
    }
  }
  
  static void dumpTest(Description description, String offset) {
    System.out.println(offset+"T -----");
    System.out.println(offset+"T display name="+description.getDisplayName());
    System.out.println(offset+"T getClassName="+description.getClassName());
    System.out.println(offset+"T getMethodName="+description.getMethodName());
    System.out.println(offset+"T test count="+description.testCount());
    System.out.println(offset+"T getAnnotations="+description.getAnnotations());
    System.out.println(offset+"T runtime ="+myListener.getRunTime(description));
    
  }

  static void dumpSuite(Description description, String offset) {
    System.out.println(offset+"display name="+description.getDisplayName());
    System.out.println(offset+"getClassName="+description.getClassName());
    System.out.println(offset+"test count="+description.testCount());
   
    List<Description> children = description.getChildren();
    for(Description child: children) {
      if(child.isSuite()) {
        dumpSuite(child, offset + "  ");
      } else {
        dumpTest(child, offset + "  ");
      }
    }
  }

  static void dumpResult(Result result) {
    System.out.println("Failure count="+result.getFailureCount());
    System.out.println("Run count="+result.getRunCount());
    
    List<Failure> failureList = result.getFailures();
    for(Failure f: failureList) {
      System.out.println("header="+f.getTestHeader());
      System.out.println("msg="+f.getMessage());
      System.out.println(f.getTrace());
    }
  }

}
