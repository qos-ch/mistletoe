package ch.qos.mistletoe.wicket;

import org.apache.wicket.markup.html.WebPage;
import org.junit.runner.Description;

import ch.qos.mistletoe.core.MistletoeCore;
import ch.qos.mistletoe.sample.MyCollection;


public class Tree extends WebPage {

  public Tree() {
    MistletoeCore mCore = new MistletoeCore(MyCollection.class);
    mCore.run();
    
    Description rootDescription = mCore.getDescription();
    rootDescription = getFistChildIfNecessary(rootDescription);
    DescriptionPanel nodePanel = new DescriptionPanel("node", rootDescription, mCore);
    add(nodePanel);
  }
  
  Description getFistChildIfNecessary(Description description) {
    if(description.getDisplayName().equals("null")) {
      return description.getChildren().get(0);
    } else {
      return description;
    }
  }
}
