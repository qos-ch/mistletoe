package ch.qos.mistletoe.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

public class TestReport implements Serializable {

  private static final long serialVersionUID = -1196521748389497981L;

  String displayName;
  String className;
  String methodName;

  Throwable throwable;
  double runtime;
  
  // ArrayList instead of List to enforce Serializablity
  ArrayList<TestReport> children = new ArrayList<TestReport>();

  public TestReport(Description description, List<Failure> failureList) {
    this.displayName = description.getDisplayName();
    this.className = description.getClassName();
    this.methodName = description.getMethodName();
    
    Failure associatedFailure = findAssociatedFailure(description, failureList);
    this.throwable = associatedFailure.getException();
    
    for (Description childDescription : description.getChildren()) {
      TestReport childTR = new TestReport(childDescription, failureList);
      children.add(childTR);
    }
  }

  private Failure findAssociatedFailure(Description d, List<Failure> failureList) {
    for(Failure f: failureList) {
      if(f.getDescription().equals(d)) {
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

  public void setRuntime(long runtime) {
    this.runtime = runtime;
  }

  public List<TestReport> getChildren() {
    return children;
  }

  public void setChildren(ArrayList<TestReport> children) {
    this.children = children;
  }

  public boolean isSuite() {
    return !isTest();
  }

  public boolean isTest() {
    return getChildren().isEmpty();
  }

  boolean hasFailures() {
    if (throwable != null) {
      return true;
    }
    for (TestReport child : children) {
      if (child.hasFailures())
        return true;
    }
    return false;
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
