package gui;

import java.io.*;
import java.util.*;
import network.Link;

public class Topologia {
  private PrintWriter resul;
  private PrintWriter salvaTop;
  private Vector<NoGrf> listaNo;
  private Vector<LinkGrf> listaLink;
  
  public Topologia() {
    listaNo = new Vector();
    listaLink = new Vector();
  }

  public void adicionarNo(NoGrf x) {
    listaNo.add(x);
  }

  public Vector getListaNo() {
    return listaNo;
  }

  public Vector getListaLink() {
    return listaLink;
  }
  
  public LinkGrf getLink(int index){
    return this.listaLink.get(index);
  }

  public NoGrf getNo(int index){
    return this.listaNo.get(index);
  }

//-----------------------------------------
  public LinkGrf procuraLink(int id) {
    for (int i = 0; i <= listaLink.size() - 1; i++) {
      if ( ( (LinkGrf) listaLink.get(i)).getId() == id) {

        return ( (LinkGrf) listaLink.get(i));
      }
    }
    System.out.println(" Link: " + id + " nao foi encontrado");
    return null;

  }

//-----------------------------
  public void adicionarLink(LinkGrf l) {
    if (l == null) {
      //     System.out.println("LinkGrf null !!!");
    }
    else {
      listaLink.add(l);
    }
  }

//-------------------------------------------
  public void removeLink(LinkGrf l) {
    NoGrf ori, dest;    
    for (int i = 0; i <= listaLink.size() - 1; i++) {
      if ( ( (LinkGrf) listaLink.get(i)) == l) {
        ori = ( (LinkGrf) listaLink.get(i)).getNoOrigem();
        dest = ( (LinkGrf) listaLink.get(i)).getNoDestino();
        dest.getOxc().removeLink(ori.getOxc());
        ori.getOxc().removeLink(dest.getOxc());
        listaLink.remove(i);
      }
    }
  }

//-------------------------------------------
  public void removeNo(NoGrf n) {
    for (int i = 0; i <= listaNo.size() - 1; i++) {
      if ( ( (NoGrf) listaNo.get(i)) == n) {
        listaNo.remove(i);
      }
    }

  }

  public Vector inverteRotas(Vector rotas) {
    String rotaAux;
    Vector rotasDef = new Vector();
    for (int i = 0; i < rotas.size(); i++) {
      rotaAux = (String) rotas.get(i);
      rotasDef.add(this.inverteString(rotaAux));
    }
    return rotasDef;
  }

  public String inverteString(String strRota) {
    String aux = new String();
    String aux2 = new String();
    int j = 0, l = 0;
    for (int i = strRota.length() - 1; i >= 0; i--) {
      j = i;
      l = i;
      aux2 = "";
      if (i != 0) {
        while (strRota.charAt(j) != '\t') {
          j--;
          if (j == -1)
            break;
        }
        for (int a = j; a < i; a++) {
          aux2 = aux2 + strRota.charAt(a + 1);
        }

        if (strRota.charAt(i) == '\t')
          aux2 = "\t";
        aux = aux + aux2;
      }
      else {
        aux = aux + strRota.charAt(0);

      }
      int c = l - j;
      for (int d = 1; d < c; d++) {
        i--;
      }

    }
    return aux;
  }

  public int getNmLink() {
    return listaLink.size();
  }

//----------------------------------
  public int existeLink(NoGrf current1, NoGrf current2) {
    LinkGrf auxLink;
    for (int i = 0; i < listaLink.size(); i++) {
      auxLink = (LinkGrf) listaLink.get(i);
      if ( ( (auxLink.getNoDestino().getName().equalsIgnoreCase(current1.getName())) &&
            (auxLink.getNoOrigem().getName().equalsIgnoreCase(current2.getName())) ||
          ( (auxLink.getNoOrigem().getName().equalsIgnoreCase(current1.getName())) &&
           (auxLink.getNoDestino().getName().equalsIgnoreCase(current2.getName()))))) {
        return (i);
      }

    }
    return ( -1);
  }

//-------------------------------------
  /*public void salvarTopologia(String arquivo) {
    if (currPrincipal.JRsim_Malha.isSelected()) {
      if (currPrincipal.JPtab_Rotas_Dij.isSelected()) {
        this.chamaDij(false);
      }
      else {
        currPrincipal.instRotaManual = new RotaManual();
        currPrincipal.instRotaManual.geraArq(currPrincipal.rotManual);
      }
      this.arqTopologia(arquivo);
    }
    try {
      salvaTop = new PrintWriter(new FileOutputStream(arquivo +
          "/Simulacao.rwa"));
    }
    catch (IOException e) {
      System.out.println("erro de arquivo2: " + arquivo);
    }
    //Imprime parametros de trafego
    salvaTop.println(String.valueOf(currPrincipal.jSpinner3.getValue()));
    if (currPrincipal.JRtipo_Traf_exp.isSelected()) {
      salvaTop.println("exponencial");
    }
    else
      salvaTop.println("uniforme");
    salvaTop.println(currPrincipal.JTfator_cresc1.getText());
    salvaTop.println(currPrincipal.JTfator_cresc.getText());
    salvaTop.println(currPrincipal.JTnum_pontos.getText());
    salvaTop.println(currPrincipal.JTrequi.getText());
    salvaTop.println(currPrincipal.JTrepli.getText());
    //  salvaTop.println(currPrincipal.JTarq_Resul.getText());
    if (currPrincipal.JR_FF.isSelected()) {
      salvaTop.println("+FF");
    }
    else {
      salvaTop.println("-FF");
    }
    if (currPrincipal.JR_Random.isSelected()) {
      salvaTop.println("+RD");

    }
    else {
      salvaTop.println("-RD");
    }

    if (currPrincipal.JR_MU.isSelected()) {
      salvaTop.println("+MU");

    }
    else {
      salvaTop.println("-MU");
    }

    if (currPrincipal.JR_LU.isSelected()) {
      salvaTop.println("+LU");

    }
    else {
      salvaTop.println("-LU");
    }

    if (currPrincipal.JR_MS.isSelected()) {
      salvaTop.println("+MS");

    }
    else {
      salvaTop.println("-MS");
    }

    if (currPrincipal.JR_RCL.isSelected()) {
      salvaTop.println("+RCL");

    }
    else {
      salvaTop.println("-RCL");
    }

    if (currPrincipal.JC_F_C.isSelected()) {
      salvaTop.println("+CT");

    }
    else {
      salvaTop.println("-CT");
    }

    salvaTop.println("-MB");

    if (currPrincipal.JPtab_Rotas_Dij.isSelected()) {
      salvaTop.println("+Dijkstra");
    }
    if (currPrincipal.JPtab_Rotas_Man.isSelected()) {
      salvaTop.println("+Manual");
    }
    if (currPrincipal.JPtab_Rotas_Man2.isSelected()) {
      salvaTop.println("+Fixo_Alternativo");
    }
    if (currPrincipal.JPtab_Rotas_ADP.isSelected()) {
      salvaTop.println("+Adaptativo");
    }
    if (currPrincipal.JPtab_Rotas_OneStep.isSelected()) {
      salvaTop.println("+OneStep");
    }
    if (currPrincipal.JPtab_Rotas_TwoStep.isSelected()||currPrincipal.JPtab_Rotas_TwoStep1.isSelected()||currPrincipal.JPtab_Rotas_TwoStep2.isSelected()) {
      salvaTop.println("+TwoStep");
    }
    if (currPrincipal.JPtab_Rotas_TwoStepBck.isSelected()) {
      salvaTop.println("+TwoStepBck");
    }

    salvaTop.println(currPrincipal.JTsim_Anel.getText());
    if (currPrincipal.JRsim_Anel_Uni.isEnabled() &&
        currPrincipal.JRsim_Anel_Uni.isSelected()) {
      salvaTop.println("+Unidirecional");
    }
    if (currPrincipal.JRsim_Anel_Uni.isEnabled() &&
        currPrincipal.JRsim_Anel_Bi.isSelected()) {
      salvaTop.println("+Bidirecional");
    }
    if (!currPrincipal.JRsim_Anel_Uni.isEnabled()) {
      salvaTop.println("Malha");
    }
    salvaTop.println(currPrincipal.Barra_Progresso.getValue());
    if(currPrincipal.jCheckBox2.isSelected())
    salvaTop.println("Fixo");
    else{
      if(currPrincipal.jC_Restr.isSelected())
        salvaTop.println("SintIlimitado");
        else
          salvaTop.println("SintLimitado");
    }
    salvaTop.println(currPrincipal.jSpinner1.getValue());
    salvaTop.println(currPrincipal.jSpinner2.getValue());
    // Imprime na segubda linha do arquivo, a (x,y) dos nos.
    for (int i = 0; i < listaNo.size(); i++) {
      elemento_noGrf aux = ( (elemento_noGrf) listaNo.get(i));
      salvaTop.print( +aux.getX() + "," + aux.getY() + "\t");


    }
    salvaTop.println();
    if(currPrincipal.jCheckBox3.isSelected()&&!currPrincipal.jC_Restr.isSelected()){
      for (int i = 0; i < listaNo.size(); i++) {
        elemento_noGrf aux = ( (elemento_noGrf) listaNo.get(i));
        salvaTop.print( +aux.getNumTransSint() + "," + aux.getNumRecep() + "\t");

      }
    }
     salvaTop.println();
    if(currPrincipal.jCheckBox2.isSelected()){
      for (int i = 0; i < listaNo.size(); i++) {
        elemento_noGrf aux = ( (elemento_noGrf) listaNo.get(i));
        for (int j = 0; j < aux.transmissoresFixos.size(); j++) {
          salvaTop.print(aux.getSintoniaTransmissorFixo(j) + ",");
        }
        salvaTop.print("\t");
      }
    }
          salvaTop.flush();




    salvaTop.close();
  }
*/
//-----------------------------------------
/*
  void arqTopologia(String arquivo2) {
    String arquivo = arquivo2 + "/topologia.txt";
    try {
      resul = new PrintWriter(new FileOutputStream(arquivo));
    }
    catch (IOException e) {
      System.out.println("erro de arquivo1: " + arquivo);
    }
    // Imprime na primeira linha do arquivo, a sequencia dos Identificadores dos nos.
    for (int i = 0; i < listaNo.size(); i++) {
      elemento_noGrf aux = ( (elemento_noGrf) listaNo.get(i));
      resul.print(aux.get_id() + "\t");
      resul.flush();

    }
    resul.println("");
    //-----------------
    for (int i = 0; i < listaNo.size(); i++) {
      elemento_noGrf aux = ( (elemento_noGrf) listaNo.get(i));
      for (int j = 0; j < aux.lista_interface.size(); j++) {

        resul.println(aux.get_id() + "\t"
                      +
                      ( (SimMalha.Interfacce) aux.lista_interface.get(j)).get_id() +
                      "\t"
                      +
                      ( (SimMalha.Interfacce) aux.lista_interface.get(j)).
                      prox_no.get_id() +
                      "\t");
        resul.flush();
      }
    }
    resul.close();
  }
*/
//-----------------------------------------
  public NoGrf procuraNo(String name) {
    for (int i = 0; i <= listaNo.size() - 1; i++) {
      if ( ( (NoGrf) listaNo.get(i)).getName().equalsIgnoreCase(name)) {
        return ((NoGrf) listaNo.get(i));
      }
    }
    System.out.println(" No: " + name + " nao foi encontrado");
    return null;
  }

  public void setNumWaveOfLink(int indexOfLink, int numWave) {
      LinkGrf l = this.listaLink.get(indexOfLink);
      l.setNumWave(numWave);
      l.getSource().linkTo(l.getDestination()).setNumWave(numWave);
      if(l.isBidirecional()){
        l.getDestination().linkTo(l.getSource()).setNumWave(numWave);
      }
  }

  public void setCostOfLink(int indexOfLink, double cost) {
      LinkGrf l = this.listaLink.get(indexOfLink);
      l.setCost(cost);
      l.getSource().linkTo(l.getDestination()).setCost(cost);  
      if(l.isBidirecional()){
        l.getDestination().linkTo(l.getSource()).setCost(cost);
      }
  }

  public void setDistanceOfLink(int indexOfLink, double distance) {
      LinkGrf l = this.listaLink.get(indexOfLink);
      l.setDistance(distance);
      
  }

  /**
   *Configura o link - se é bidirecional.
   *@param indexOfLink int indice do link
   *@param bi boolean bidirecional
   */
  public void setLinkBidirecional(int indexOfLink, boolean bi) {
      LinkGrf l = this.listaLink.get(indexOfLink);
      l.setBidirecional(bi);      
      if (bi){
        Link aux = l.getDestination().linkTo(l.getSource());
        if (aux==null){
            l.getDestination().addLink(new Link(l.getDestination(),l.getSource(),l.getCost(),l.getNumWave()));
        }
      }else{
          l.getDestination().removeLink(l.getSource());
      }      
  }
}
