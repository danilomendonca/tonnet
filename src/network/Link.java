package network;

public class Link {

  private Oxc source;
  private Oxc destination;
  private double cost;
  private boolean[] wavelength;
  private int numberWave;
  private boolean faild=false;
  private double numberFailure=0;
  private double sumUtilization;
  private double numAnalysesUtilization = 0;
  private double distance = 0;

  /**
   * Creates a new instance of Link.
   * @param s Oxc New value of property source.
   * @param d Oxc New value of property destination.
   * @param cost int New value of property cost.
   * @param numberWave int New value of property numberWave
   */
  public Link(Oxc s, Oxc d, double cost, int numberWave) {
    this.source = s;
    this.destination = d;
    this.cost = cost;
    this.numberWave = numberWave;
    this.wavelength = new boolean[numberWave];
  }

//------------------------------------------------------------------------------
  /**
   * Setter for property numberWave.
   * @param n int number Wavelength
   */
  public void setNumWave(int n) {
    this.numberWave = n;
  }

//------------------------------------------------------------------------------
  /**
   * Retorna o numero de comprimentos de onda.
   * @return int
   */
  public int getNumWave() {
    return this.numberWave;
  }

//------------------------------------------------------------------------------
  /**
   * is node x destination of this link.
   * @param x Oxc
   * @return true if Oxc x is destination of this Link; false otherwise.
   */
  public boolean adjacent(Oxc x) {
    if (destination == x)
      return true;
    else
      return false;
  }

//------------------------------------------------------------------------------
  /**
   * ocupa o comprimento de onda "i"
   * @param i indice do comprimento de onda
   * @return boolean
   */
  public boolean useWavelength(int i) {
    if (wavelength[i] == false) {
      wavelength[i] = true;
      return true;
    }
    else {
      System.err.println("ERRO:classe Link. metodo useWavelength()");
      System.err.println("Link("+source.getName()+","+destination.getName()+") wavelenth:" + i);
    return false;
    }
  }
//------------------------------------------------------------------------------
  /**
   * ocupa o comprimentod de onda "i"
   * @param i indice do comrimento de onda
   * @return boolean
   */
  public boolean liberateWavelength(int i) {
      if (wavelength[i] == true) {
        wavelength[i] = false;
        return true;
      }
      else {
        System.err.println("ERRO:classe Link. metodo liberateWavelength()");
        System.err.println("Link("+source.getName()+","+destination.getName()+") wavelenth:" + i);
        return false;
      }
  }
  //------------------------------------------------------------------------------
  /**
   * Retorna true se o comprimento de onda i estiver livre.
   * Se failure for true analisa se o link tem falha ou não.
   * @param i int indice do comprimento de onda
   * @param failure boolean
   * @return boolean
   */
  public boolean waveEmpty(int i,boolean failure) {
      if ((this.faild == true)&&(failure))
        return false;
      else { // se o enlace não tiver falhas
        if (i < wavelength.length && wavelength[i] == false) { //adicionada verificação da existência do lambda dfmendonca
          return true;
        }
        else {
          return false;
        }
      }
  }
  //------------------------------------------------------------------------------
  /**
   * retorna o índice de um comprimento de onda livre.
   * Se failure for true considera falha no enlace
   * @param failure boolean
   * @return int
   */
  public int getAnyWaveEmpty(boolean failure) {

      if ((this.faild == true)&&(failure))
        return -1;
      else { // se o enlace não tiver falhas
        for (int i = 0; i < this.wavelength.length; i++) {
          if (wavelength[i] == false)
            return i;
        }
        return -1;
      }
  }

//------------------------------------------------------------------------------
  /**
   * Getter for property destination.
   * @return Oxc destination
   */
  public Oxc getDestination() {
    return destination;
  }

//------------------------------------------------------------------------------
  /**
   * Setter for property destination.
   * @param destination Oxc New value of property destination.
   */
  public void setDestination(Oxc destination) {
    this.destination = destination;
  }

//------------------------------------------------------------------------------
  /**
   * Setter for property source.
   * @param source Oxc New value of property source.
   */
  public void setSource(Oxc source) {
    this.source = source;
  }
  //------------------------------------------------------------------------------
  /**
   * Getter for property source.
   * @return Oxc source
   */
  public Oxc getSource() {
    return source;
  }
  //------------------------------------------------------------------------------
  /**
   * Getter for property cost.
   * @return double cost
   */
  public double getCost() {
    return cost;
  }
  //------------------------------------------------------------------------------
  /**
   * Setter for property Cost.
   * @param cost double new cost.
   */
  public void setCost(double cost) {
    this.cost = cost;
  }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

  
  //------------------------------------------------------------------------------
  /**
   * configura o enlace como falho. retorna true se a operação foi feita com
   * sucesso.
   * @return boolean
   */
  public boolean setFaild(){
    if (!faild){
      numberFailure++;
      faild = true;
      return true;
    }
    else{
      String name="<"+source.getName()+","+destination.getName()+">";
      System.out.println("tentativa de falhar o link "+name );
      System.out.println("O link "+name+" ja estava com falha");
      System.out.println("class: Link, metodo: setFaild()");
      return false;
    }
  }
  //------------------------------------------------------------------------------
  /**
   * corrigi a falha do enlace. retorna true se a operação foi feita com
   * sucesso.
   * @return boolean
   */
  public boolean fixLink(){
    if (faild){
      faild = false;
      return true;
    }
    else{
      String name="<"+source.getName()+","+destination.getName()+">";
      System.out.println("tentativa de reparar o link "+name );
      System.out.println("O link "+name+" nao estava com falha");
      System.out.println("class: Link, metodo: fixLink()");
      return false;
    }

  }
  //------------------------------------------------------------------------------
  /**
   * getNumberFailure
   * @return double
   */
  public double getNumberFailure() {
    return this.numberFailure;
  }
  //------------------------------------------------------------------------------
  /**
   * retorna o nome do link no formato <origem, destino>
   * @return String
   */
  public String getName(){
    return  "<" + getSource().getName() + "," + getDestination().getName()+">";
  }
  //------------------------------------------------------------------------------
  /**
   * faildLink
   *
   * @return boolean
   */
  public boolean faildLink() {
    return this.faild;
  }
  //------------------------------------------------------------------------------
  /**
   * Faz o somatório da utilização do enlace e contabiliza o nº de vezes que
   * essa medida foi feita para posteriormente calcular a utilização média do
   * enlace. utilização média = sumUtilization/numAnalysesUtilization.
   */
  public void calcSumUtilization() {
    this.sumUtilization=this.sumUtilization+(this.numWaveBusy()/this.numberWave);
    this.numAnalysesUtilization++;
  }
  //------------------------------------------------------------------------------
  /**
   * retorna a utilização media
   * @return double
   */
  public double getUtilization(){
    return this.sumUtilization/this.numAnalysesUtilization;
  }
  //------------------------------------------------------------------------------
  /**
   * para reiniciar o calculo de utilizacao
   */
  public void reStartUtilization(){
    this.sumUtilization=0;
    this.numAnalysesUtilization=0;
  }
  //------------------------------------------------------------------------------
  /**
   * retorna o numero de comprimentos de onda ocupados.
   * @return double
   */
  public double numWaveBusy() {
    double count=0;
    for (int i = 0; i < this.wavelength.length; i++) {
      if (wavelength[i]==true)
      count++;
    }
    return count;
  }
  //------------------------------------------------------------------------------
  /**
   * atualiza o custo do link com base no mumero de comprimentos de onda
   * ocupados neste instante
   */
  public void updateCost(){
    this.cost = this.numWaveBusy();
  }
}
