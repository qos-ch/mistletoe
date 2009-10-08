package ch.qos.mistletoe.wicket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Node implements Serializable {


  String name;
  List<Node> childrenList = new ArrayList<Node>();
  List<String> payloadList =  new ArrayList<String>();
  
  Node(String name) {
    this.name = name;
  }
  
  public void add(Node child) {
    childrenList.add(child);
  }

  public void addPayload(String s) {
    payloadList.add(s);
  }

  public List<String> getPayloadList() {
    return payloadList;
  }

  public String getName() {
    return name;
  }

  public boolean isSimple() {
    return childrenList.size() == 0;
  }
  
  public List<Node> getChildrenList() {
    return childrenList;
  }
  
  static Node getSampleNode() {
    Node nodeA = new Node("A");
    Node nodeA0 = new Node("A0");
    Node nodeA00 = new Node("A00");
    Node nodeA01 = new Node("A01");
    nodeA01.addPayload("java.lang.AssertionError: fail");
    nodeA01.addPayload("&nbsp;&nbsp;at org.junit.Assert.fail(Assert.java:91)");
    nodeA01.addPayload("&nbsp;&nbsp;at ch.qos.mistletoe.sample.MyIntegration2.smoke2(MyIntegration2.java:18)");
    
    Node nodeA1 = new Node("A1");
    
    nodeA.add(nodeA0);
    nodeA.add(nodeA1);
    
    nodeA0.add(nodeA00);
    nodeA0.add(nodeA01);
    return nodeA;
  }
  
  @Override
  public String toString() {
    return "Node [name=" + name + "]";
  }

}
