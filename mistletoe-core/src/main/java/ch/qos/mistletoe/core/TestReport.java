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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

public class TestReport implements Serializable {

  private static final long serialVersionUID = -1196521748389497981L;

  public static final String FAILURE_COLOR = "#BB4444";
  public static final String SUCCESS_COLOR = "#66bb66";
  
  final String displayName;
  final String className;
  final String methodName;

  final Throwable throwable;
  final double runtime;

  // ArrayList instead of List to enforce Serializablity
  final ArrayList<TestReport> children = new ArrayList<TestReport>();

  public TestReport(Description description, MistletoeCore mCore) {
    this.displayName = description.getDisplayName();
    this.className = description.getClassName();
    this.methodName = description.getMethodName();
    this.runtime = findRuntime(description, mCore);

    Failure associatedFailure = findAssociatedFailure(description, mCore.result
        .getFailures());
    if (associatedFailure != null) {
      this.throwable = associatedFailure.getException();
    } else {
      this.throwable = null;
    }

    for (Description childDescription : description.getChildren()) {
      TestReport childTR = new TestReport(childDescription, mCore);
      children.add(childTR);
    }
  }

  double findRuntime(Description d, MistletoeCore mCore) {
    StopWatchRunListener swRunListener = mCore.getStopWatchRunListener();
    return swRunListener.getRunTime(d);
  }

  private Failure findAssociatedFailure(Description d, List<Failure> failureList) {
    for (Failure f : failureList) {
      if (f.getDescription().equals(d)) {
        return f;
      }
    }
    return null;
  }

  public String getDisplayName() {
    return displayName;
  }

  public double getRuntime() {
    return runtime;
  }

  public int getTotalFailures() {
    int total = 0;
    if(throwable != null) {
      total++;
    } 
    for (TestReport child : children) {
      total += child.getTotalFailures();
    }
    return total;
  }
  

  static public TestReport getFistChildIfNecessary(TestReport description) {
    if (description.getDisplayName().equals("null")) {
      return description.getChildren().get(0);
    } else {
      return description;
    }
  }
  public int getTestCount() {
    int total = 0;
    if (isTest()) {
      return 1;
    } else {
      for (TestReport child : children) {
        total += child.getTestCount();
      }
    }
    return total;
  }

  public List<TestReport> getChildren() {
    return children;
  }

  public boolean isSuite() {
    return !isTest();
  }

  public boolean isTest() {
    return getChildren().isEmpty();
  }

  public boolean hasFailures() {
    if (throwable != null) {
      return true;
    }
    for (TestReport child : children) {
      if (child.hasFailures())
        return true;
    }
    return false;
  }

  public Throwable getThrowable() {
    return throwable;
  }

  public double cumulativedRuntime() {
    double cumulative = runtime;
    for (TestReport child : children) {
      cumulative += child.cumulativedRuntime();
    }
    return cumulative;
  }

  @Override
  public String toString() {
    String type = isTest() ? "isTest " : "isSuite";
    return "TestReport [displayName=" + displayName + ", methodName="
        + methodName + ", " + type + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((displayName == null) ? 0 : displayName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TestReport other = (TestReport) obj;
    if (className == null) {
      if (other.className != null)
        return false;
    } else if (!className.equals(other.className))
      return false;
    if (displayName == null) {
      if (other.displayName != null)
        return false;
    } else if (!displayName.equals(other.displayName))
      return false;
    if (methodName == null) {
      if (other.methodName != null)
        return false;
    } else if (!methodName.equals(other.methodName))
      return false;
    return true;
  }

}
