package ch.qos.mistletoe.wicket;

import org.apache.wicket.markup.html.WebPage;
import org.junit.runner.Description;

import ch.qos.mistletoe.core.MistletoeCore;
import ch.qos.mistletoe.sample.MyCollection;


public class Tree extends WebPage {

  public Tree() {
    MistletoeCore mCore = new MistletoeCore(MyCollection.class);
    mCore.run();
    
    Description description = mCore.getDescription();
    
    DescriptionPanel nodePanel = new DescriptionPanel("node", description, mCore);
    add(nodePanel);
  }
}
