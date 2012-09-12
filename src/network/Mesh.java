package network;

import java.io.*;
import java.util.*;

import measurement.*;
import routing.*;
import simulator.*;
import wavelengthAssignment.*;
import traffic.*;

public class Mesh {

  private Vector<Node> nodeList;
  private Vector<Pair> pairs;
  private RoutingControl routingControl;
  private WaveAssignControl waveAssignControl;
  private ConnectionControl connectionControl;
  private TrafficControl trafficControl;
  private Measurements measurements;
  private RandGenerator randomVar;
  private Vector<Link> linkList;
  public Vector<Link> linkList_actual_route;
  private int conversionType;
  // teste ................
  public int falha = 0;
  public int countMoreOneFailure = 0;
  // teste fim .............
  public Vector<Link> linkListFaildAtMoment;

  public Mesh(Vector<Node> nodeList, String algorithm, String pairFile,int trafficType) {
    this.nodeList = nodeList;
    this.waveAssignControl = new WaveAssignControl(this, algorithm);
    this.randomVar = new RandGenerator();
    this.linkListFaildAtMoment = new Vector<Link> ();
    this.startTrafficControl(trafficType,pairFile);
    this.coumputeAllLinks();
    this.measurements = new Measurements(this.linkList, this.nodeList);
    this.connectionControl = new ConnectionControl(this.measurements);
  }

  /**
   * setConversion
   * @param conversionType int
   */
  public void setConversionType(int conversionType) {
    this.conversionType = conversionType;
    if (conversionType == 1) { //conversao total      
      for (int i = 0; i < this.nodeList.size(); i++) {
        this.nodeList.get(i).setWcrTotal(true);
      }      
    }
  }

  /**
   * retorna o tipo de conversão
   * @return int
   */
  public int getConversionType() {
    return this.conversionType;
  }

  /**
   * calcula a utilização da rede. Faz o somatorio do número de comprimentos
   * de onda ocupados em todos os enlaces e divide pelo número total de
   * comprimentos de onda da rede.
   * @return double
   */
  public double calculateUtilization() {
    double waveBusy = 0, totalWaveNumber = 0;

    for (int i = 0; i < this.linkList.size(); i++) {
      waveBusy = waveBusy + linkList.get(i).numWaveBusy();
      totalWaveNumber = totalWaveNumber + linkList.get(i).getNumWave();
    }
    return (waveBusy / totalWaveNumber);
  }

  /**
   * calcula a utilização de cada comprimento de onda.
   * Faz o somatorio do número enlaces que o comprimentos de onda ocupada
   * e divide pela quantidade de enlaces da da rede.
   * @return double[]
   */
  public double[] calculateWavelengthUtilization() {
    int linksNumber = this.linkList.size();
    int wavesNumber = this.linkList.get(0).getNumWave();
    //guarda a utilizacao da cada comprimento de onda (waveBasy[w]/linksNumber)
    double[] waveUtilization = new double[wavesNumber];

    for (int i = 0; i < linksNumber; i++) {
      for (int w = 0; w < wavesNumber; w++) {
        if ( (!this.linkList.get(i).waveEmpty(w, false))) {
          waveUtilization[w]++;
        }
      }
    }

    for (int w = 0; w < wavesNumber; w++) {
      waveUtilization[w] = waveUtilization[w] / linksNumber;
    }

    return waveUtilization;
  }

  /**
   * retorna true se o algoritmo de alocação alocou um comprimento de onda para
   * a rota r com sucesso.
   * @param waveList int[]
   * @param freeWaves int[]
   * @param category int
   * @return boolean
   */
  public boolean callWaveAssign(int[] waveList, int[] freeWaves, int category) {
    return this.waveAssignControl.run(waveList, freeWaves, category);
  }

  /**
   * adiciona os link na lista de links atualmente falhados
   * @param x Vector
   */

  public void addLinkFaild(Vector<Link> x) {
    for (int i = 0; i < x.size(); i++) {
      this.linkListFaildAtMoment.add(x.get(i));
    }
  }

  /**
   * remove os link na lista de links atualmente falhados
   * @param x Vector
   */

  public void removeLinkFaild(Vector<Link> x) {
    for (int i = 0; i < x.size(); i++) {
      this.linkListFaildAtMoment.remove(x.get(i));
    }
  }

  public Link getLink(String source, String destination) {
    for (int i = 0; i < linkList.size(); i++) {
      if ( (linkList.get(i).getSource().getName() == source) &&
          (linkList.get(i).getDestination().getName() == destination)) {
        return linkList.get(i);
      }

    }
    return null;
  }

  /**
   * retorna um Vector com todos os enlaces.
   * @return Vector
   */
  public Vector<Link> getLinkList() {
    return linkList;
  }

  /**
   * computa todos os enlaces e armazena os enlaces em linkList.
   */
  private void coumputeAllLinks() {
    linkList = new Vector<Link> ();
    for (int i = 0; i < nodeList.size(); i++) {
      linkList.addAll(nodeList.get(i).getOxc().getLinksList());
    }
  }

  /**
   * retorna um enlace sorteado aleatoriamente dentre todos os enlaces possíveis.
   * Assume que o enlace é bidirecional. Isto é, o método retorna o enlace de
   * ida e o enlace de volta.
   * @return Link
   */
  public Vector<Link> getRandomOneLink() {
    Vector<Link> listLinks = new Vector<Link> ();
    int x = this.getRandomVar().randInt(0, this.getLinkList().size() - 1);
    Link auxLink = this.linkList.get(x);
    //ida
    listLinks.add(auxLink);
    //volta
    listLinks.add(this.getLink(auxLink.getDestination().getName(),
                               auxLink.getSource().getName()));
    return listLinks;

  }

  /**
   * gera um enlace que será falhado, mas que atualmente está funcionando
   * normalmente no instante atual.
   * @return Vector
   */
  public Vector<Link> generateLinkFailure() {
    Vector<Link> linksFaild = getRandomOneLink();
    while (this.isThereFailure(linksFaild)) {
      linksFaild = getRandomOneLink();
    }
    if (linksFaild == null) {
      System.out.println(
          "linkFaild =null class Mesh, metodo generateLinkFailure()");
    }
    return linksFaild;
  }

  /**
   * retorna true se pelo menos um dos enlaces passados no Vector links já
   * estiver definido como falho. Está assumindo que o Vector links possui
   * dois links <a,b> e <b,a>
   * @param links Vector
   * @return boolean
   */
  public boolean isThereFailure(Vector<Link> links) {
    for (int i = 0; i < links.size(); i++) {
      if (this.linkListFaildAtMoment.contains(links.get(i))) {
        return true;
      }
    }
    return false;
  }

//------------------------------------------------------------------------------
  public Measurements getMeasurements() {
    return this.measurements;
  }

//------------------------------------------------------------------------------
  public RandGenerator getRandomVar() {
    return this.randomVar;
  }

//------------------------------------------------------------------------------
  public RoutingControl getRoutingControl() {
    return this.routingControl;
  }

//------------------------------------------------------------------------------
  /**
   * Add Node in property nodeList.
   * @param x Node
   * @return true if Node x added successfully; false otherwise.
   */
  public boolean addNode(Node x) {
    return nodeList.add(x);
  }

//------------------------------------------------------------------------------
  /**
   * Getter for property nodeList.
   * @return Vector with nodes.
   */
  public Vector<Node> getNodeList() {
    return nodeList;
  }

//------------------------------------------------------------------------------
  /**
   * get Node index
   * @param index int
   * @return Node Node i.
   */
  public Node getNode(int index) {
    return nodeList.get(index);
  }

  //------------------------------------------------------------------------------
  public ConnectionControl getConnectionControl() {
    return connectionControl;
  }

//------------------------------------------------------------------------------
  /**
   * Localiza um Node em função do nome.
   * @param name String
   * @return Node
   */
  public Node searchNode(String name) {
    for (int i = 0; i < this.nodeList.size(); i++) {
      Node tmp = nodeList.get(i);
      if (tmp.getName().equals(name)) {
        return tmp;
      }
    }
    return null;
  }

//------------------------------------------------------------------------------
  public void startRouteControl(int type) {
    this.routingControl = new RoutingControl(this.nodeList, this.linkList,
                                             this.trafficControl.getPairList(),
                                             type);
  }

//------------------------------------------------------------------------------
  public void setNodeList(Vector nodeList) {
    this.nodeList = nodeList;
  }

  //------------------------------------------------------------------------------
  /**
   * Inicializa o tráfego com os pares
   * @param trafficType int
   * @param pairFile String
   */
  private void startTrafficControl(int trafficType,String pairFile) {
    this.trafficControl = new TrafficControl(trafficType, this.computeAllPairs(pairFile),this);
  }

  //------------------------------------------------------------------------------
  /**
   * carrega do arquivo os pares e respectivas classes e armazena em pairList
   * @param pairFile String
   * @return Vector
   */
  private Vector<Pair> computeAllPairs(String pairFile) {
    Vector<Pair> pairList = new Vector<Pair> ();
    BufferedReader in;
    FileReader file;
    try {
      file = new FileReader(pairFile);
      in = new BufferedReader(file);
      //LINE 1-lendo os comentarios
      String comment = in.readLine();
      // System.out.println(comment);
      if (in.ready()) {
        while (in.ready()) {
          StringTokenizer line = new StringTokenizer(in.readLine(), ";\t");
          String nameS = line.nextToken(); //lendo a Origem
          String nameD = line.nextToken(); //lendo o Destino
          //lendo a categoria
          int category = Integer.parseInt(line.nextToken());
          //lendo o privilegio do par (para tráfego não uniforme)
          int privilege = Integer.parseInt(line.nextToken());
          Pair p = new Pair(searchNode(nameS), searchNode(nameD));
          pairList.add(p);
          p.setId(pairList.indexOf(p));
          p.setCategory(category);
          p.setPrivilege(privilege);
        }
      }
      else {
        pairList = this.computeAllPairs();
      }
    }
    catch (IOException e) {
      System.out.println("File Error - Sintaxe or file not found");
    }
    return pairList;
  }

//------------------------------------------------------------------------------
  /**
   * Calcula todas as combinações de pares e armazena em pairList
   * @return Vector
   */
  private Vector<Pair> computeAllPairs() {
    Vector<Pair> pairList = new Vector<Pair> ();
    for (int i = 0; i < nodeList.size(); i++) {
      for (int j = 0; j < nodeList.size(); j++) {
        if (i != j) {
          Pair p = new Pair(nodeList.get(i), nodeList.get(j));
          pairList.add(p);
          p.setId(pairList.indexOf(p));
        }
      }
    }
    this.pairs= pairList;
    return pairList;
  }

  //------------------------------------------------------------------------------
  public void calculateMeasurePair() {
    Vector<PairMeasure> listPairMeasure = new Vector<PairMeasure> ();
    int routeType = this.routingControl.getRoutingType();

    for (int i = 0; i < this.trafficControl.getPairList().size(); i++) {
      Pair aux = this.trafficControl.getPairList().get(i);
      double size = 0;
      if (routeType == 0) { //roteamento fixo
        size = this.routingControl.getRoutes(aux).get(0).getHops();
      }
      else if ( (routeType == 2) || (routeType == 3)) {
        Vector<Route>
            routes = this.routingControl.getRoutes(aux);
        size = routes.get(0).getHops();
        if (routes.get(1) != null) {
          size += routes.get(1).getHops();
        }
      }
      listPairMeasure.add(new PairMeasure(aux.getName(), aux.getNumGenerated(),
                                          aux.getPbPair(), size,
                                          aux.getCategory()));

    }
    this.getMeasurements().setListPairMeasure(listPairMeasure);
    this.getMeasurements().sizeOrderListPairMeasure();
  }

//------------------------------------------------------------------------------
public void calculateMeasureLink() {
  Vector<LinkMeasure> listLinkMeasure = new Vector<LinkMeasure> ();
  for (int i = 0; i < this.linkList.size(); i++) {
    Link aux = this.linkList.get(i);
    listLinkMeasure.add(new LinkMeasure(aux.getName(), aux.getNumberFailure(),
                                        aux.getUtilization()));
  }
  if (this.routingControl.getRoutingType() == 0) { //roteamento fixo
    Vector<Route>
        allRoutes = ( (RoutingWithoutSurvival)this.routingControl.
                     getRoutingObject()).
        getRoutesForAllPairs();
    for (int i = 0; i < allRoutes.size(); i++) {
      Route r = allRoutes.get(i);
      for (int j = 0; j < r.getHops(); j++) {
        Link linkOfRoute = r.getLink(j);
        for (int l = 0; l < listLinkMeasure.size(); l++) {
          if (linkOfRoute.getName().equals(listLinkMeasure.get(l).getName())) {
            listLinkMeasure.get(l).addRoute(r);
            break;
          }
        }
      }
    }
  }else if (this.routingControl.getRoutingType() == 1) {//roteamento adaptativo-LLR
      
  } else {
    Vector<Vector<Route>>
        allRoutes = ( (RoutingWithSurvival)this.routingControl.
                     getRoutingObject()).getkDisjointRoutesForAllPairs();
    for (int i = 0; i < allRoutes.size(); i++) {
      Vector<Route> routes = allRoutes.get(i);
      for (int r = 0; r < routes.size(); r++) {
        Route route = routes.get(r);
        if (route != null) {
          for (int j = 0; j < route.getHops(); j++) {
            Link linkOfRoute = route.getLink(j);
            for (int l = 0; l < listLinkMeasure.size(); l++) {
              if (linkOfRoute.getName().equals(listLinkMeasure.get(l).getName())) {
                listLinkMeasure.get(l).addRoute(route);
                break;
              }
            }
          }
        }
      }
    }
  }
  this.getMeasurements().setListLinkMeasure(listLinkMeasure);
}
//------------------------------------------------------------------------------
public void calculateMeasureNode() {
  //calcula a soma dos picos de utilizacao de WC de todos os nos
  this.measurements.calculateSumMaximumWCUtilization();

  Vector<NodeMeasure> listNodeMeasure = this.measurements.getListNodeMeasure();
  int numWave = this.linkList.firstElement().getNumWave();
  for (int i = 0; i < this.nodeList.size(); i++) {
    NodeMeasure aux = listNodeMeasure.get(i);
    aux.setNumMaxWC(this.nodeList.get(i).getOxc().getNumInterfaces()*numWave);
  }
  if (this.routingControl.getRoutingType() == 0) { //roteamento fixo
    Vector<Route>
        allRoutes = ( (RoutingWithoutSurvival)this.routingControl.
                     getRoutingObject()).
        getRoutesForAllPairs();
    for (int i = 0; i < allRoutes.size(); i++) {
      Route r = allRoutes.get(i);
      for (int j = 1; j < r.size()-1; j++) {//apenas nós intermédiários
        Node nodeOfRoute = r.getNode(j);
        for (int l = 0; l < listNodeMeasure.size(); l++) {
          if (nodeOfRoute.getName().equals(listNodeMeasure.get(l).getName())) {
            listNodeMeasure.get(l).addRoute(r);
            break;
          }
        }
      }
    }
  }else if (this.routingControl.getRoutingType() == 1) { //roteamento fixo
      
  }
  else {
    Vector<Vector<Route>>
        allRoutes = ( (RoutingWithSurvival)this.routingControl.
                     getRoutingObject()).getkDisjointRoutesForAllPairs();
    for (int i = 0; i < allRoutes.size(); i++) {
      Vector<Route> routes = allRoutes.get(i);
      for (int r = 0; r < routes.size(); r++) {
        Route route = routes.get(r);
        if (route != null) {
          for (int j = 1; j < route.size()-1; j++) {//apenas nós intermédiários
            Node nodeOfRoute = route.getNode(j);
            for (int l = 0; l < listNodeMeasure.size(); l++) {
              if (nodeOfRoute.getName().equals(listNodeMeasure.get(l).getName())) {
                listNodeMeasure.get(l).addRoute(route);
                break;
              }
            }
          }
        }
      }
    }
  }
}
/**
 * Gera um par aleatorio
 * @return Pair
 * @return Pair
 */
public Pair pairGenerator() {
  return this.trafficControl.pairGenerator();
}

/**
 * Retorna um par a partir de origem e destino.
 * @param source
 * @param destination
 * @return
 */
public Pair searchPair(Node source, Node destination){
    return this.trafficControl.searchPair(source, destination);
}

public Vector<Vector<Route>> getRoutes()
{	
	//Vector<Route> routes = new Vector<Route> ();
	//Vector<Vector<Route>> routes;
	Vector <Vector<Route>>  routes = new Vector<Vector<Route>> ();
	for(int k=0;k<this.pairs.size(); k++)
	{	
	 	  //Route r = new R(nodeList.get(i), nodeList.get(j));
          //pairList.add(p);
          routes.add(this.routingControl.getRoutes(this.pairs.get(k)));
		//routes.add[k]=this.RoutingControl.getroutes(this.pairs.get[k].getId());
	}//for
	return routes;
	

}
public void setActualLinklist(Vector<Link> linklist)
{
	this.linkList_actual_route=linklist;
}


}
