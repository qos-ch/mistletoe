package ch.qos.mistletoe.wicket;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListView;

public class TreeExpansionLink extends AjaxFallbackLink<Object> {
  private static final long serialVersionUID = 1L;
  boolean expanded = true;

  static String EXPAND_GIF = "../images/expand.gif";
  static String COLLAPSE_GIF = "../images/collapse.gif";

  public TreeExpansionLink(String id) {
    super(id);
    String controlSymbolValue = EXPAND_GIF;
    if (expanded) {
      controlSymbolValue = COLLAPSE_GIF;
    }
    Label label = new Label(Constants.TREE_CONTROL_SYMBOL, "");
    label.add(new MyAttributeModifier("src", controlSymbolValue));
    
    label.setOutputMarkupId(true);
    this.add(label);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void onClick(AjaxRequestTarget target) {
    System.out.println("*****clicked on ajax link");
    NodePanel nodePanel = (NodePanel) getParent();

    if (!nodePanel.node.isSimple()) {
      expanded = !expanded;
      System.out.println("expanded=" + expanded);

      TreeExpansionLink link = (TreeExpansionLink) nodePanel
          .get(Constants.TREE_CONTROL);
      Label label = (Label) link.get(Constants.TREE_CONTROL_SYMBOL);
      
      
      target.addComponent(link.getParent());

      
      
      List<IBehavior> bList = label.getBehaviors();
      for (IBehavior b : bList) {
        if (b instanceof MyAttributeModifier) {
          MyAttributeModifier attrModifier = (MyAttributeModifier) b;
          if (expanded) {
            attrModifier.setValue(COLLAPSE_GIF);
          } else {
            attrModifier.setValue(EXPAND_GIF);
          }
        }
      }

      ListView<Node> payloadNode = (ListView<Node>) nodePanel
          .get(Constants.PAYLOAD);
      payloadNode.setVisible(expanded);

      // can't update a ListView
      target.addComponent(payloadNode.getParent());

      // Iterator<? extends Component> it = payloadNode.iterator();
      // while (it.hasNext()) {
      // Component c = it.next();
      // c.setVisible(expanded);
      // target.addComponent(c);
      // }
    }
  }

}
