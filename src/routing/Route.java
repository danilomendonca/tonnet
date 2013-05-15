package routing;

import java.util.*;
import network.*;

public class Route {

  private Vector<Node> nodeList;
  private Vector<Link> linkList;
  private char routingType = BURST_ROUTING;
  public static final char BURST_ROUTING = 'B';
  public static final char CIRCUIT_ROUTING = 'C';

  /**
   * Creates a new instance of Route.
   * @param nodeList Vector Node List.
   */
  public Route(Vector<Node> nodeList) {
    this.nodeList = nodeList;
    this.linkList = new Vector<Link> ();
    this.computeLinks();
  }

//------------------------------------------------------------------------------
  private void computeLinks() {
    for (int i = 0; i < this.nodeList.size() - 1; i++) {
      this.linkList.add(this.nodeList.get(i).getOxc().linkTo(this.nodeList.get(
          i + 1).getOxc()));
    }
  }

//------------------------------------------------------------------------------
  /**
   * Getter for source Node.
   * @return Node. The source Node of this Route.
   */
  public Node getOrigem() {
    if (nodeList.size() > 0) {
      return (nodeList.firstElement());
    }
    else {
      System.out.println("lista de nó esta vazia");
      return null;
    }
  }

//------------------------------------------------------------------------------
  /**
   * Getter for destination node.
   * @return Node. The destination node of route.
   */
  public Node getDestino() {
    if (nodeList.size() > 0) {
      return (nodeList.lastElement());
    }
    else {
      System.out.println("lista de nó esta vazia");
      return null;
    }
  }

//------------------------------------------------------------------------------
  /**
   * Returns the Node at the specified position in this Route.
   * @param index index of Node to return.
   * @return Node at the specified index.
   */
  public Node getNode(int index) {
    return (nodeList.get(index));
  }

//------------------------------------------------------------------------------
  /**
   * Returns the Link at the specified position in this Route.
   * @param index index of Link to return.
   * @return Node at the specified index.
   */
  public Link getLink(int index) {
    return (linkList.get(index));
  }

//------------------------------------------------------------------------------
  /**
   * Returns next Node at Node n.
   * @param n Node.
   * @return Node. Next Node at Node n.
   */
  public Node getNext(Node n) {
    Node aux;
    String nameN = n.getName();
    for (int i = 0; i < nodeList.size() - 1; i++) {
      aux = nodeList.get(i);
      if (aux.getName().equalsIgnoreCase(nameN)) {
        return nodeList.get(i + 1);
      }
    }
    System.err.println("ERRO: proximo no nao foi encontrado para o no : " +
                       n.getName());
    System.out.println("ERRO: class Route");
    return null;
  }

//------------------------------------------------------------------------------
  /**
   * Creates a new instances of Route.
   * @param a Route
   * @return true if existent link; false otherwise.
   */
  public boolean linkEquals(Route a) {
    Node ori1, ori2, prox1, prox2;

    for (int i = 0; i <= this.nodeList.size() - 2; i++) {
      ori1 = (this.nodeList.get(i));
      for (int j = 0; j <= a.nodeList.size() - 2; j++) {
        ori2 = (a.nodeList.get(j));
        if (ori1.equals(ori2)) { // existem pelo menos dois nos iguais !!!
          prox1 = this.getNext(ori1);
          prox2 = a.getNext(ori2);
          if (prox1.equals(prox2)) {
            return true;
          }
        }
      }
    }
    return false;
  }
  
//------------------------------------------------------------------------------
  /**
   * Getter for property routingType.
   * @return Vector.
   */
  public char getRoutingType() {
    return this.routingType;
  }  
  

//------------------------------------------------------------------------------
  /**
   * Getter for property nodeList.
   * @return Vector.
   */
  public Vector<Node> getNodeList() {
    return this.nodeList;
  }

//------------------------------------------------------------------------------
  /**
   * Getter for property linkList.
   * @return Vector<Link>.
   */
  public Vector<Link> getLinkList() {
    return this.linkList;
  }

//------------------------------------------------------------------------------
  /**
   * Returns the number of Nodes in this Route.
   * @return int. The number of Nodes in this Route.
   */
  public int size() {
    return nodeList.size();
  }

//------------------------------------------------------------------------------
  /**
   * Returns the number of hops in this Route.
   * @return int. The number of hops in this Route.
   */
  public int getHops() {
    return this.nodeList.size() - 1;
  }

  //------------------------------------------------------------------------------
  /**
   * retorna true se a rota possuir pelo menos um comprimento de onda
   * contínuo livre. Além disso o método define os comprimento de onda que serão
   * utilizados em cada enlace da rota. Esse método não faz o estabelecimento do
   * lightpath.Se failure for true analisa se o link tem falha ou não.
   * Se failure for true analisa se a rota possui enlaces com falha.
   * @param waveList int[]
   * @param m Mesh
   * @param category int
   * @param failure boolean
   * @return boolean
   */
  private boolean tryEstablishWithoutConversion(int[] waveList,
                                                Mesh m, int category,
                                                boolean failure) {
    int[] freeWaves = null;
    freeWaves = this.getAllWaveEmpty(failure);
    if (freeWaves.length > 0) {
      //  if (freeWaves[0]!=0)
      //  System.out.println("PARE");
    	//m.getRoutes();
    	m.setActualLinklist(linkList);
        //se houver canal de controle, reserva último comprimento de onda livre
        /*if(hasControlChannel) {
            controlChannel[0] = freeWaves[freeWaves.length - 1];
            freeWaves = Arrays.copyOf(freeWaves, freeWaves.length - 1);            
        }*/
      return m.callWaveAssign(waveList, freeWaves, category);
    }
    return false;
  }

  //----------------------------------------------------------------------------
  /**
   * retorna true se a rota possuir pelo menos um comprimento de onda
   * em cada enlace da rota. Além disso o método define os
   * comprimento de onda que serão utilizados em cada enlace da rota.
   * Esse método não faz o estabelecimento do lightpath.
   * @param waveList int[]
   * @param failure boolean
   * @return boolean
   */
  private boolean tryEstablishWithFullConversion(int[] waveList,
                                                 boolean failure) {
    int auxWaveList[] = new int[waveList.length];

    for (int i = 0; i < this.getLinkList().size(); i++) {
      if (this.linkList.get(i).getAnyWaveEmpty(failure) != -1) {
        auxWaveList[i] = this.linkList.get(i).getAnyWaveEmpty(
            failure);
      }
      else {
        return false;
      }
    }

    for (int i = 0; i < this.linkList.size(); i++) {
      waveList[i] = auxWaveList[i];
    }

    return true;
  }

  //----------------------------------------------------------------------------
  private boolean tryEstablishWithSparsePartialConversion(int[]
      waveList, Mesh m, int category, boolean failure) {
    //posicao na rota dos wcrs
    Vector<Integer> posWcr = this.getPosFreeWcrs();
    //todas combinacoes ordenadas pela quantidade de "1"s
    Vector<String>
        binaryCombinations = Convert.binaryCombination(posWcr.size());
    //lista de comprimentos de ondas livres por subrota
    Vector<Vector<Integer>> freeWavesBySubRoutes = new Vector<Vector<Integer>> ();
    //guarda o numero de saltos de cada subrota
    Vector<Integer> subRoutesHops = new Vector<Integer> ();
    //tentando estabelecer, utilizando o menor numero de conversores
    //percorrendo todas combinacoes
    boolean flag = true;
    for (int i = 0; i < binaryCombinations.size(); i++) {
      String combination = binaryCombinations.get(i);
      freeWavesBySubRoutes = new Vector<Vector<Integer>> ();
      subRoutesHops = new Vector<Integer> ();
      int n = 0; //guarda posicao inicial da segmentacao
      int f = 0; //posicao final da segmentacao
      flag = true;
      for (int j = 0; j < combination.length(); j++) {
        if (combination.charAt(j) == '1') {
          f = posWcr.get(j);
          Vector<Integer> aux = this.getAllWaveEmpty(n, f, failure);
          if (aux.size() > 0) { //existe pelo menos um comprimento de onda livre
            freeWavesBySubRoutes.add( (Vector<Integer>) aux.clone());
            subRoutesHops.add(f - n);
            n = f;
          }
          else {
            flag = false;
            j = combination.length(); //break!
          }
        }
      }
      //ultima subrota
      Vector<Integer>
          aux = this.getAllWaveEmpty(f, this.size() - 1, failure);
      if (aux.size() > 0) { //existe pelo menos um comprimento de onda livre
        freeWavesBySubRoutes.add( (Vector<Integer>) aux.clone());
        subRoutesHops.add(this.size() - 1 - f);
      }
      else {
        flag = false;
      }
      if (flag == true) {
        i = binaryCombinations.size();
      }
    }
    if (flag == false) {
      return false;
    }

    //utilizar algoritmo para escolher os comprimentos de ondas
    int p = 0;
    for (int i = 0; i < freeWavesBySubRoutes.size(); i++) {
      //comprimentos de ondas livres na subrota i
      int freeWaves[] = new int[freeWavesBySubRoutes.get(i).size()];
      for (int j = 0; j < freeWaves.length; j++) {
        freeWaves[j] = freeWavesBySubRoutes.get(i).get(j);
      }

      int[] waveListI = new int[subRoutesHops.get(i)];
      m.callWaveAssign(waveListI, freeWaves, category);
      for (int k = 0; k < waveListI.length; k++) {
        waveList[p++] = waveListI[k];
      }

    }
    return true;
  }

  //----------------------------------------------------------------------------
  /**
   * retorna true se a rota possuir pelo menos um comprimento de onda em cada
   * enlace da rota de acordo com a disposição dos conversores.
   * Além disso o método define os comprimento de onda que serão utilizados em
   * cada enlace da rota. Esse método não faz o estabelecimento do lightpath.
   * Se failure for true analisa se a rota possui enlaces com falha.
   * @param waveList int[]
   * @param m Mesh
   * @param category int Categoria do par da requisição
   * @param failure boolean
   * @return boolean
   */
  public boolean tryEstablish(int[] waveList, Mesh m, int category,
                              boolean failure) {
    //...................rota com apenas um link ou SEM CONVERSÃO...............
    if ( (this.size() == 2) || (m.getConversionType() == 0)) {
      
      return this.tryEstablishWithoutConversion(waveList, m,
                                                category, failure);
    }
    //...................CONVERSÃO TOTAL...................
    else if (m.getConversionType() == 1) {
  /*    if (this.tryEstablishWithoutConversion(waveList, m, category, failure)) {
        return true;
      }*/
      return this.tryEstablishWithFullConversion(waveList, failure);
 /*     return this.tryEstablishWithSparsePartialConversion(waveList, m,
          category, failure);*/
    }
    //...................CONVERSÃO ESPARSA-PARCIAL...................
    return this.tryEstablishWithSparsePartialConversion(waveList, m,
        category, failure);
  }

//------------------------------------------------------------------------------
  /**
   * retorna um comprimento de onda livre para cada enlace que compoe a rota.
   * Se failure for true analisa se a rota possui enlaces com falha.
   * @param failure boolean
   * @return int[]
   */
  public int[] getWaveEmptyByLink(boolean failure) {
    int[] waves = new int[this.getHops()];

    for (int i = 0; i < this.getHops(); i++) {
      waves[i] = -1;
    }

    for (int i = 0; i < this.getHops(); i++) {
      int numWave = this.linkList.get(i).getNumWave();
      for (int j = 0; j < numWave; j++) {
        if (this.linkList.get(i).waveEmpty(j, failure)) {
          waves[i] = j;
        }
      }
    }

    return waves;
  }
  
  public int getFirstLinkNumWave(){
      
      int numWave = this.linkList.firstElement().getNumWave();
      if(routingType == BURST_ROUTING)
          numWave--;
      
      return numWave;
  }

  //------------------------------------------------------------------------------
  /**
   * retorna os comprimentos de onda contínuos livres.
   * Se failure for true analisa se a rota possui enlaces com falha.
   * @param failure boolean
   * @return int[]
   */
  public int[] getAllWaveEmpty(boolean failure) {
    int numWave = getFirstLinkNumWave();
    
    int[] setFree = new int[numWave];

    for (int j = 0; j < numWave; j++) {
      setFree[j] = j;
    }

    int freeNumber = numWave;
    for (int i = 0; i < this.linkList.size(); i++) {
      for (int j = 0; j < numWave; j++) {
        if (!this.linkList.get(i).waveEmpty(j, failure)) {
          if (setFree[j] != -1) {
            setFree[j] = -1;
            freeNumber--;
          }
        }
      }
    }
    int freeWave[] = new int[freeNumber];

    int j = 0;
    for (int i = 0; i < numWave; i++) {
      if (setFree[i] != -1) {
        freeWave[j] = setFree[i];
        j++;
      }
    }

    return freeWave;
  }

  //------------------------------------------------------------------------------
  /**
   * retorna os comprimentos de onda contínuos livres
   * Se failure for true analisa se a rota possui enlaces com falha.
   * @param a int indice do primeiro no subrota
   * @param b int indice do ultimo no da subrota
   * @param failure boolean
   * @return Vector
   */
  public Vector<Integer> getAllWaveEmpty(int a, int b,
                                         boolean failure) {
    int numWave = this.linkList.get(a).getNumWave();
    int[] setFree = new int[numWave];

    for (int j = 0; j < numWave; j++) {
      setFree[j] = j;
    }

    int freeNumber = numWave;
    for (int i = a; i < b; i++) {
      for (int j = 0; j < numWave; j++) {
        if (!this.linkList.get(i).waveEmpty(j, failure)) {
          if (setFree[j] != -1) {
            setFree[j] = -1;
            freeNumber--;
          }
        }
      }
    }
    Vector<Integer> freeWave = new Vector<Integer> (freeNumber);

    for (int i = 0; i < numWave; i++) {
      if (setFree[i] != -1) {
        freeWave.add(setFree[i]);
      }
    }

    return freeWave;
  }

//------------------------------------------------------------------------------

  /**
   *Define que os comprimentos de onda alocados ficam ocupados nos
   *enlaces desta rota.
   * @param waveList int[] comprimentos de onda alocados
   * @return boolean
   */
  public boolean useWavelength(int[] waveList) {
    boolean flag = true;
    for (int i = 0; i < this.getHops(); i++) {
      if (!this.getLink(i).useWavelength(waveList[i])) {
        flag = false;
      }
    }
    return flag;
  }

  //------------------------------------------------------------------------------
  /**
   * Define que os comprimentos de onda alocados ficam livres nos enlaces desta
   * rota
   * @param waveList int[] comprimentos de onda alocados
   * @return boolean
   */
  public boolean liberateWavelength(int[] waveList) {
    boolean flag = true;
    for (int i = 0; i < this.getHops(); i++) {
      if (!this.getLink(i).liberateWavelength(waveList[i])) {
        flag = false;
      }
    }
    // System.out.println("Par liberado : "+this.getOrigem().getName()+","+this.getDestino().getName());
    //   System.out.println("Wave : "+this.waveList[0]);
    // this.printRoute();
    return flag;
  }

  //----------------------------------------------------------------------------
  /**
   * Print Route.
   */
  public void printRoute() {
    for (int i = 0; i < nodeList.size(); i++) {
      System.out.print(nodeList.get(i).getName() + ";");
    }
    System.out.println();
  }

  /**
   * varre os nós intermediários da rota e verifica se pelo menos um deles possui
   * capacidade atual de conversão.
   * @return boolean
   */
  public boolean canConversion() {
    int contWCR = 0;
    for (int i = 1; i < nodeList.size() - 1; i++) {
      if (nodeList.get(i).canConverter()) {
        return true;
      }
    }
    return false;
  }

  //----------------------------------------------------------------------------
  /**
   * retorna o numero de nos que contem conversores livres
   * @return int
   */
  public int getFreeWcrNumber() {
    int cont = 0;
    for (int i = 1; i < this.nodeList.size() - 1; i++) {
      if (this.nodeList.get(i).canConverter()) {
        cont++;
      }
    }
    return cont;
  }

  //----------------------------------------------------------------------------
  /**
   * Retorna as posicoes dos nos que tem conversores livres
   * @return Vector
   */
  public Vector<Integer> getPosFreeWcrs() {
    Vector<Integer> pos = new Vector<Integer> ();
    for (int i = 1; i < this.nodeList.size() - 1; i++) {
      if (this.nodeList.get(i).canConverter()) {
        pos.add(new Integer(i));
      }
    }
    return pos;
  }

  //----------------------------------------------------------------------------
  /**
   * containThisLink
   * @param link Link
   * @return boolean
   */
  public boolean containThisLink(Link link) {
    return this.linkList.contains(link);
  }

  //----------------------------------------------------------------------------
  public boolean anyLinkFaild() {
    for (int i = 0; i < this.linkList.size(); i++) {
      if (this.linkList.get(i).faildLink()) {
        return true;
      }
    }
    return false;
  }

  //----------------------------------------------------------------------------
  /**
   * retorna o numero de links desta rota depois do faildLink
   * @param faildlink Oxc
   * @return int
   */
  public int hopNumFromNodeBToNodeX(Link faildlink) {
    int count = 0;
    for (int i = 0; i < this.linkList.size(); i++) {
      if (this.linkList.get(i) == faildlink) {
        count = 0;
      }
      else {
        count++;
      }

    }
    return count;
  }

}
