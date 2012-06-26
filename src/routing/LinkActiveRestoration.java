package routing;

import network.*;

public class LinkActiveRestoration {
  Link link;
  Route route;
  public LinkActiveRestoration(Link l, Route r) {
    this.link=l;
    this.route=r;
  }

  public Link getLink(){
    return link;
  }
  public Route getRestoration(){
    return route;
  }
}
