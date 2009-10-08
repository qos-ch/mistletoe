package ch.qos.mistletoe.wicket;

import java.util.Iterator;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;

@SuppressWarnings("serial")
public class NodePanel extends Panel {

  boolean expanded = true;
  Node node;

  public NodePanel(String id, Node node) {
    super(id);
    this.node = node;

    boolean inError = node.payloadList.size() != 0;

    AjaxFallbackLink<Object> link = new AjaxFallbackLink<Object>("control") {
      @Override
      public void onClick(AjaxRequestTarget target) {
        System.out.println("*****click and stuff");
        NodePanel nodePanel = (NodePanel) getParent();

    
        if (!nodePanel.node.isSimple()) {
          
          ListView payloadNode = (ListView) nodePanel.get("payload");
        
          Iterator<? extends Component> it = payloadNode.iterator();
          while(it.hasNext()) {
            Component c = it.next();
            c.setVisible(false);
            target.addComponent(c);
            System.out.println(c);
          }
          
          
          for(Object item: payloadNode.getList()) {
            //System.out.println(item.getClass());
          }
          
//          if(innerNode == null) {
//            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxx");
//            return;
//          }
//          nodePanel.expanded = !nodePanel.expanded;
//          innerNode.setVisible(nodePanel.expanded);
//          System.out.println("********* expanded=" + nodePanel.expanded);
//          if (target != null) {
//            target.addComponent(innerNode);
//          }
        }
      }
    };
    add(link);

    String controlSymbol = "";
    if (!node.isSimple()) {
      controlSymbol = expanded ? "-" : "+";

    }
    link.add(new Label("controlSymbol", controlSymbol));

    String src = "../images/tick.png";
    if (inError) {
      src = "../images/cross.png";
    }

    add(new Label("image", "").add(new SimpleAttributeModifier("src", src)));
    add(new Label("name", node.getName()));

    if (node.isSimple() && node.payloadList.size() == 0) {
      final WebMarkupContainer parent = new WebMarkupContainer("payload");
      add(parent);
      parent.add(new EmptyPanel("node").setVisible(false));
    } else if (node.payloadList.size() != 0) {
      System.out.println("***********");
      StringBuffer buf = new StringBuffer();
      for (String s : node.payloadList) {
        buf.append(s);
        buf.append("<br/>");
      }
      final WebMarkupContainer parent = new WebMarkupContainer("payload");
      add(parent);
      parent.add(new Label("node", buf.toString()).setEscapeModelStrings(false)
          .add(
              new SimpleAttributeModifier("style",
                  "text: white-space: pre; color: grey;")));
    } else {
      add(new ListView<Node>("payload", node.getChildrenList()) {
        @Override
        protected void populateItem(ListItem<Node> item) {
          Node childNode = item.getModelObject();
          item.add(new NodePanel("node", childNode)).setOutputMarkupId(true);
        }
      });
    }
    setOutputMarkupId(true);

  }
}
