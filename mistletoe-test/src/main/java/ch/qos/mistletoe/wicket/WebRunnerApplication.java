package ch.qos.mistletoe.wicket;

import org.apache.wicket.protocol.http.WebApplication;

public class WebRunnerApplication  extends WebApplication {

  @Override
  public Class<Tree> getHomePage() {
    return Tree.class;
  }

}
