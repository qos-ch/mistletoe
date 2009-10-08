package ch.qos.mistletoe.wicket;

import org.apache.wicket.markup.html.WebPage;

@SuppressWarnings("serial")
public class Tree extends WebPage {

  public Tree() {
    Node node = Node.getSampleNode();
    NodePanel nodePanel = new NodePanel("node", node);
    add(nodePanel);
  }
}
