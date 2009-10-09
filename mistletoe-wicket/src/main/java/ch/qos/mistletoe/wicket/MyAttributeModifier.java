package ch.qos.mistletoe.wicket;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.ComponentTag;

public class MyAttributeModifier extends SimpleAttributeModifier {


  private static final long serialVersionUID = 1L;
  String value;
  String attribute;
  
  public MyAttributeModifier(String attribute, String value) {
    super(attribute, value);
    this.value = value;
    this.attribute = attribute;
  }

  @Override
  public void onComponentTag(final Component component, final ComponentTag tag) {
    System.out.println("ononComponentTag called. component="+component+", tag="+tag);
    if (isEnabled(component)) {
      System.out.println("changing attribute, value="+value);
      tag.getAttributes().put(attribute, value);
    }
  }

  public void setValue(String value) {
    this.value = value;
  }
}
