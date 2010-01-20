/**
 * Mistletoe: a junit extension for integration testing
 * Copyright (C) 2009, QOS.ch. All rights reserved.
 *
 * This program and the accompanying materials are dual-licensed under
 * either the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation
 *
 *   or (per the licensee's choosing)
 *
 * under the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation.
 */
package ch.qos.mistletoe.core;

import java.util.List;

import org.junit.runner.Computer;
import org.junit.runner.Description;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

public class MistletoeCore {

  private final Class<?> targetClass;
  private final RunNotifier notifier = new RunNotifier();
  
  final StopWatchRunListener swRunListener = new StopWatchRunListener();

  // description of the suite run
  Description description;
  // result of the run
  Result result;

  public MistletoeCore(Class<?> targetClass) {
    this.targetClass = targetClass;
  }

  public MistletoeCore(String targetClassStr) throws ClassNotFoundException {
    if(targetClassStr == null) {
      throw new NullPointerException("[targetClassStr] cannot be null");
    }
    this.targetClass = Class.forName(targetClassStr);
  }

  public Description getDescription() {
    return description;
  }

  public Result getResult() {
    return result;
  }

  public StopWatchRunListener getStopWatchRunListener() {
    return swRunListener;
  }

  public TestReport run() {
    Computer defaultComputer = new Computer();
    Request request = Request.classes(defaultComputer, targetClass);
    Runner runner = request.getRunner();
    result = run(runner);
    description = runner.getDescription();
    
    TestReport testReport = new TestReport(description, this);
    return testReport;
  }


  private Result run(Runner runner) {
    Result result = new Result();
    RunListener listener = result.createListener();
    notifier.addFirstListener(listener);
    notifier.addListener(swRunListener);

    try {
      notifier.fireTestRunStarted(runner.getDescription());
      runner.run(notifier);
      notifier.fireTestRunFinished(result);
    } finally {
      notifier.removeListener(listener);
    }
    return result;
  }

  public boolean hasAssociatedFailures(Description d) {
    List<Failure> failureList = result.getFailures();

    for (Failure f : failureList) {
      if (f.getDescription().equals(d)) {
        return true;
      }
      if (description.isTest()) {
        return false;
      }
      List<Description> descriptionList = d.getChildren();
      for (Description child : descriptionList) {
        if (hasAssociatedFailures(child)) {
          return true;
        }
      }
    }
    return false;
  }

  static void dumpDescription(StopWatchRunListener myListener,
      Description description) {
    if (description.isSuite()) {
      dumpSuite(myListener, description, "");
    } else {
      dumpTest(myListener, description, "");
    }
  }

  static void dumpTest(StopWatchRunListener myListener,
      Description description, String offset) {
    System.out.println(offset + "T -----");
    System.out.println(offset + "T display name="
        + description.getDisplayName());
    System.out.println(offset + "T getClassName=" + description.getClassName());
    System.out.println(offset + "T getMethodName="
        + description.getMethodName());
    System.out.println(offset + "T test count=" + description.testCount());
    System.out.println(offset + "T getAnnotations="
        + description.getAnnotations());
    System.out.println(offset + "T runtime ="
        + myListener.getRunTime(description));

  }

  static void dumpSuite(StopWatchRunListener myListener,
      Description description, String offset) {
    System.out.println(offset + "display name=" + description.getDisplayName());
    System.out.println(offset + "getClassName=" + description.getClassName());
    System.out.println(offset + "test count=" + description.testCount());

    List<Description> children = description.getChildren();
    for (Description child : children) {
      if (child.isSuite()) {
        dumpSuite(myListener, child, offset + "  ");
      } else {
        dumpTest(myListener, child, offset + "  ");
      }
    }
  }

  static void dumpResult(Result result) {
    System.out.println("Failure count=" + result.getFailureCount());
    System.out.println("Run count=" + result.getRunCount());

    List<Failure> failureList = result.getFailures();
    for (Failure f : failureList) {
      System.out.println("header=" + f.getTestHeader());
      System.out.println("msg=" + f.getMessage());
      System.out.println(f.getTrace());
    }
  }

}
