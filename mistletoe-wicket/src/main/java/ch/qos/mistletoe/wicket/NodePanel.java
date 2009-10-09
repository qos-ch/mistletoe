package ch.qos.mistletoe.wicket;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;

@SuppressWarnings("serial")
public class NodePanel extends Panel {

  Node node;

  public NodePanel(String id, Node node) {
    super(id);
    this.node = node;

    boolean inError = node.payloadList.size() != 0;

    if (node.isSimple()) {
      final WebMarkupContainer parent = new WebMarkupContainer(
          Constants.TREE_CONTROL);
      parent.add(new SimpleAttributeModifier("style", "cursor: default;"));

      Label label = new Label(Constants.TREE_CONTROL_SYMBOL);
      label.add(new SimpleAttributeModifier("src", Constants.BLANK_GIF));
      label.setEnabled(false);
      parent.add(label);
      add(parent);
    } else {
      TreeExpansionLink link = new TreeExpansionLink(Constants.TREE_CONTROL);
      add(link);
    }

    String testResultSrc = Constants.TESTOK_GIF;
    if (inError) {
      testResultSrc = Constants.TESTFAIL_GIF;
    }
    add(new Label(Constants.IMAGE, "").add(new SimpleAttributeModifier("src",
        testResultSrc)));
    add(new Label(Constants.NAME, node.getName()));

    if (node.isSimple() && node.payloadList.size() == 0) {
      final WebMarkupContainer parent = new WebMarkupContainer(
          Constants.PAYLOAD);
      add(parent);
      parent.add(new EmptyPanel(Constants.NODE).setVisible(false));
    } else if (node.payloadList.size() != 0) {
      System.out.println("***********");
      StringBuffer buf = new StringBuffer();
      for (String s : node.payloadList) {
        buf.append(s);
        buf.append("<br/>");
      }
      final WebMarkupContainer parent = new WebMarkupContainer(
          Constants.PAYLOAD);
      add(parent);
      parent.add(new Label(Constants.NODE, buf.toString())
          .setEscapeModelStrings(false).add(
              new SimpleAttributeModifier("style",
                  "text: white-space: pre; color: grey;")));
    } else {
      ListView<Node> listView = new ListView<Node>(Constants.PAYLOAD, node
          .getChildrenList()) {
        @Override
        protected void populateItem(ListItem<Node> item) {
          Node childNode = item.getModelObject();
          item.add(new NodePanel(Constants.NODE, childNode)).setOutputMarkupId(
              true);
        }
      };
      listView.setOutputMarkupId(true);
      add(listView);
    }
    setOutputMarkupId(true);

  }
}
