package ch.qos.mistletoe.wicket;

import java.util.List;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import ch.qos.mistletoe.core.MistletoeCore;

@SuppressWarnings("serial")
public class DescriptionPanel extends Panel {

  MistletoeCore core;
  Description description;
  Failure f;
  public DescriptionPanel(String id, Description node, MistletoeCore core) {
    super(id);
    this.core = core;
    this.description = node;
     f = findFailure();
    
    if (node.isTest()) {
      handleBlankPlaceHolderImage();
    } else {
      TreeExpansionLink link = new TreeExpansionLink(Constants.TREE_CONTROL);
      add(link);
    }

    handleResultImage(node);
    add(new Label(Constants.NAME, node.getDisplayName()));
    
   
    
    
    if (node.isTest() && f == null) {
      handleSimple_OK_Node();
    } else if (f != null) {
      handle_NOT_OK_Node();
    } else {
      handleNodeWithChildren();
    }
    setOutputMarkupId(true);
  }

  Failure findFailure() {
    
    Result r = core.getResult();
    
    List<Failure> failureList = r.getFailures();
    System.out.println("in findFailure===================");
    for(Failure f: failureList) {
      
      System.out.println(f);
      System.out.println("this.description="+description);
      System.out.println("f.description="+f.getDescription() );
      if(f.getDescription().equals(description)) {
        System.out.println("*****match");
        return f;
      }
    }
    return null;
  }
   
  void handleNodeWithChildren() {
    ListView<Description> listView = new ListView<Description>(Constants.PAYLOAD, description
        .getChildren()) {
      @Override
      protected void populateItem(ListItem<Description> item) {
        Description childNode = item.getModelObject();
        item.add(new DescriptionPanel(Constants.NODE, childNode, core)).setOutputMarkupId(
            true);
      }
    };
    listView.setOutputMarkupId(true);
    add(listView);
  }

  void handleSimple_OK_Node() {
    final WebMarkupContainer parent = new WebMarkupContainer(Constants.PAYLOAD);
    add(parent);
    parent.add(new EmptyPanel(Constants.NODE).setVisible(false));
  }

  void handle_NOT_OK_Node() {
    StringBuffer buf = new StringBuffer();
     Throwable t = f.getException();
    
     StackTraceElement[] steArray = t.getStackTrace();
     
    for (StackTraceElement se : steArray) {
      buf.append(se);
      buf.append("<br/>");
    }
    final WebMarkupContainer parent = new WebMarkupContainer(Constants.PAYLOAD);
    add(parent);
    Label exception = new Label(Constants.NODE, buf.toString());
    exception.setEscapeModelStrings(false);
    exception.add(new SimpleAttributeModifier("class", "exception"));
    parent.add(exception);

    if (steArray.length > 20) {
      SimpleAttributeModifier sam = new SimpleAttributeModifier("style",
          "height: 40em; overflow: scroll;");
      exception.add(sam);
    }

  }

  void handleBlankPlaceHolderImage() {
    final WebMarkupContainer parent = new WebMarkupContainer(
        Constants.TREE_CONTROL);
    // we don't want the "hand" cursor to appear over the blank place holder
    // image
    parent.add(new SimpleAttributeModifier("style", "cursor: default;"));

    Image image = new Image(Constants.TREE_CONTROL_SYMBOL,
        new ResourceReference(DescriptionPanel.class, Constants.BLANK_GIF));
    parent.add(image);
    add(parent);
  }

  void handleResultImage(Description description) {
    boolean inError = (f != null);
    boolean isSuite = description.isSuite();

    String testResultSrc = null;
    if (isSuite) {
      
      if (core.hasAssociatedFailures(description)) {
        testResultSrc = Constants.TSUITE_ERROR_GIF;
      } else {
        testResultSrc = Constants.TSUITE_OK_GIF;
      }
    } else {
      if (inError) {
        testResultSrc = Constants.TEST_ERROR_GIF;
      } else {
        testResultSrc = Constants.TEST_OK_GIF;
      }
    }

    Image image = new Image(Constants.IMAGE, new ResourceReference(
        DescriptionPanel.class, testResultSrc));
    add(image);
  }
}
