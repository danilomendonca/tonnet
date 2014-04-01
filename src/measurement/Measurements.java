package measurement;

import java.util.*;

import network.*;

public class Measurements {

  /**
   * carga
   */
  private double arrivedRate;
  /**
   * hurst mínimo
   */
  private float hurstMin;
  /**
   * hurst máximo
   */
  private float hurstMax;
  /**
   * tipo de comutação
   */
  private int switchingType;
  /**
   * numero da replicação
   */
  private int replication;
  /**
   *  nº de requisições bloqueadas.
   */
  private double numBlocking;

  /**
   * num de bloqueios de requsições (Proteção) por ausência exclusivamente de
   * recurso para  rota de backup.
   */
  private double numBlockingAbsenceBackupProtection;

  /**
   * nº de requisições geradas.
   */
  private double numGeneratedReq;

  /**
   * Time spent by skip burst events
   */
  private double skipBurstEventsTime;
  
  /**
   * tamanho total de todas requisições atendidas
   */
  private double allSizeOfPrimaryAcceptedReq;
  /**
   * tamanho total de todas requisições bloqueidas
   */
  private double allSizeOfPrimaryBlockedReq;
  /**
   * tamanho total de todas requisições de backup atendidas
   */
  private double allSizeOfBackupAcceptedReq;
  /**
   * tamanho total de todas requisições de backup bloqueidas
   */
  private double allSizeOfBackupBlockedReq;

  /**
   * Nº de falhas geradas
   */
  private double numGeneratedFailure;

  /**
   * Nº de falhas geradas que geram de fato a falha de alguma conexão
   */
  private double numGeneratedFailureEffective;

  /**
   * para incremento da utilização
   */
  private double sumOfUtilization;

  /**
   * para incremento da utilização por comprimento de onda
   */
  private double[] sumOfWavelenthUtilization;

  /**
   * Guara a soma do numero maximo de WC utilizados por todos os nos
   */
  private int sumMaximumWCUtilization;
  /**
   * armazena o somatórios da probabilidade de restauração
   */
  private double sumRestorability;

  /**
   * referencia a lista de links da malha para atualizar metricas dos links
   */
  private Vector<Link> linkList;
  /**
   * armazena o nome dos links e respctivas metricas
   */
  private Vector<LinkMeasure> listLinkMeasure;

  /**
   * armazena o nome dos nos e respctivas metricas
   */
  private Vector<NodeMeasure> listNodeMeasure;

  /**
   * armazena o nome dos pares e respectivas metricas
   */
  private Vector<PairMeasure> listPairMeasure;
  private double sumOfRestorationTime = 0;

  public Measurements(Vector<Link> linkList, Vector<Node> nodeList) {
    this.numBlocking = 0;
    this.numGeneratedReq = 0;
    this.numGeneratedFailure = 0;
    this.sumOfUtilization = 0;
    this.numBlockingAbsenceBackupProtection = 0;
    this.linkList = linkList;
    this.sumOfWavelenthUtilization = new double[this.linkList.get(0).getNumWave()];

    this.listNodeMeasure = new Vector<NodeMeasure> ();
    for (int i = 0; i < nodeList.size(); i++) {
      this.listNodeMeasure.add(new NodeMeasure(nodeList.get(i).getName()));
    }
  }

  //------------------------------------------------------------------------------
  public int getReplication() {
    return this.replication;
  }

  //------------------------------------------------------------------------------
  public void setReplication(int replication) {
    this.replication = replication;
  }
  
    //------------------------------------------------------------------------------
  public int getSwitchingType() {
    return this.switchingType;
  }

  //------------------------------------------------------------------------------
  public void setSwitchingType(int switchingType) {
    this.switchingType = switchingType;
  }


  //------------------------------------------------------------------------------

  public double getArrivedRate() {
    return this.arrivedRate;
  }
  
  //------------------------------------------------------------------------------

  public float getHurstMin() {
    return this.hurstMin;
  }
  
  //------------------------------------------------------------------------------

  public float getHurstMax() {
    return this.hurstMax;
  }

  //------------------------------------------------------------------------------
  public void setArrivedRate(double arrivedRate) {
    this.arrivedRate = arrivedRate;
  }
  
  //------------------------------------------------------------------------------
  public void setHurstMin(float hurst) {
    this.hurstMin = hurst;
  }
  
  //------------------------------------------------------------------------------
  public void setHurstMax(float hurst) {
    this.hurstMax = hurst;
  }

  //------------------------------------------------------------------------------
  /**
   * soma do tamanho de todas requisições bloqueiadas dividido
   * pelo nº de requisições bloqueidas
   * @return double
   */
  public double getAverageSizeOfPrimaryBlockedReq() {
    return this.allSizeOfPrimaryBlockedReq / this.numBlocking;
  }

  //------------------------------------------------------------------------------
  /**
   * soma do tamanho de todas requisições atendidas dividido
   * pelo nº de requisições atendidas
   * @return double
   */
  public double getAverageSizeOfPrimaryAcceptedReq() {
    return this.allSizeOfPrimaryAcceptedReq /
        (this.numGeneratedReq - this.numBlocking);
  }

  //------------------------------------------------------------------------------
  /**
   * soma do tamanho de todas requisições de Backup bloqueiadas dividido
   * pelo nº de requisições bloqueidas
   * @return double
   */
  public double getAverageSizeOfBackupBlockedReq() {
    return this.allSizeOfBackupBlockedReq / this.numBlocking;
  }

  //------------------------------------------------------------------------------
  /**
   * soma do tamanho de todas requisições de Backup atendidas dividido
   * pelo nº de requisições atendidas
   * @return double
   */
  public double getAverageSizeOfBackupAcceptedReq() {
    return this.allSizeOfBackupAcceptedReq /
        (this.numGeneratedReq - this.numBlocking);
  }

  //------------------------------------------------------------------------------
  /**
   * retorna numBlocking / numGeneratedReq
   * @return double
   */
  public double getPb() {
    return (this.numBlocking / this.numGeneratedReq);
  }

  //------------------------------------------------------------------------------
  public double getPbBlockingAbsenceBackupProtection() {
    return this.numBlockingAbsenceBackupProtection / this.numGeneratedReq;
  }

//------------------------------------------------------------------------------
  public double getNumGeneratedReq() {
    return this.numGeneratedReq;
  }

  //------------------------------------------------------------------------------
  public double getNumGeneratedFailure() {
    return this.numGeneratedFailure;
  }

//------------------------------------------------------------------------------
  public double getNumBlocking() {
    return this.numBlocking;
  }

  //------------------------------------------------------------------------------
  public double getSumRestorability() {
    return this.sumRestorability;
  }

  //------------------------------------------------------------------------------
  public Vector<Link> getLinkList() {
    return this.linkList;
  }

  //------------------------------------------------------------------------------
  public Vector<LinkMeasure> getListLinkMeasure() {
    return this.listLinkMeasure;
  }

  //------------------------------------------------------------------------------
  public Vector<NodeMeasure> getListNodeMeasure() {
    return this.listNodeMeasure;
  }

  //------------------------------------------------------------------------------
  public Vector<PairMeasure> getListPairMeasure() {
    return this.listPairMeasure;
  }

  //------------------------------------------------------------------------------
  /**
   * retorna a probabilidade de restaurabilidade
   * sumRestorability / numGeneratedFailure
   * @return double
   */
  public double getPbRestorability() {
    return this.sumRestorability / this.numGeneratedFailureEffective;
  }

  //------------------------------------------------------------------------------
  /**
   * incrementa o num. de requisições geradas.
   */
  public void incNumGeneratedReq() {
    this.numGeneratedReq++;
    //System.out.prln("req num "+ this.numGeneratedReq);
  }

  //------------------------------------------------------------------------------
  /**
   * soma ao tamanho de todas requisições atendidas
   */
  public void sumAllSizeOfPrimaryAcceptedReq(double size) {
    this.allSizeOfPrimaryAcceptedReq += size;
  }

  //------------------------------------------------------------------------------
  /**
   * soma ao tamanho de todas requisições bloqueidas
   */
  public void sumAllSizeOfPrimaryBloqckedReq(double size) {
    this.allSizeOfPrimaryBlockedReq += size;
  }

  //------------------------------------------------------------------------------
  /**
   * soma ao tamanho de todas requisições de backup atendidas
   */
  public void sumAllSizeOfBackupAcceptedReq(double size) {
    this.allSizeOfBackupAcceptedReq += size;
  }

  //------------------------------------------------------------------------------
  /**
   * soma ao tamanho de todas requisições de backup bloqueidas
   */
  public void sumAllSizeOfBackupBloqckedReq(double size) {
    this.allSizeOfBackupBlockedReq += size;
  }

  //------------------------------------------------------------------------------
  /**
   * incrementa o num de falhas geradas
   */
  public void incNumGeneratedFailure() {
    this.numGeneratedFailure++;
  }

  //------------------------------------------------------------------------------
  /**
   * incrementa o num. de bloqueios.
   */
  public void incBlocked() {
    this.numBlocking++;
  }

  //------------------------------------------------------------------------------
  /**
   * incrementa num. de bloqueios por inviabilidade da rota de backup na
   * proteção
   */
  public void incBlockingAbsenceBackupProtection() {
    this.numBlockingAbsenceBackupProtection++;
  }

  //----------------------------------------------------------------------------
  /**
   * incrementa o nº de falhas que de fato afeta alguma conexão
   */
  public void incNumGeneratedFailureEffective() {
    this.numGeneratedFailureEffective++;
  }

  //----------------------------------------------------------------------------
  /**
   * Faz o somatório da utilização da rede. Isto é feito toda vez que chega uma
   * requisição de conexão.
   * @param utilization double
   */
  public void sumOfUtilization(double utilization) {
    this.sumOfUtilization += utilization;
  }

  //----------------------------------------------------------------------------
  /**
   * Faz o somatório da utilização da rede. Isto é feito toda vez que chega uma
   * requisição de conexão.
   * @param utilization double
   */
  public void sumOfWavelenghtUtilization(double[] waveUtilization) {
    for (int w = 0; w < waveUtilization.length; w++) {
      this.sumOfWavelenthUtilization[w] += waveUtilization[w];
    }
  }

//------------------------------------------------------------------------------
  public double getUtilization() {
    return this.sumOfUtilization / this.numGeneratedReq;
  }

  //------------------------------------------------------------------------------
  public int getSumMaximumWCUtilization() {
    return this.sumMaximumWCUtilization;
  }

  //------------------------------------------------------------------------------
  public double[] getWavelenghUtilization() {
    int wavesNumber = this.sumOfWavelenthUtilization.length;
    double[] wavelengthUtilization = new double[wavesNumber];

    for (int w = 0; w < wavesNumber; w++) {
      wavelengthUtilization[w] = this.sumOfWavelenthUtilization[w] /
          this.numGeneratedReq;
    }

    return wavelengthUtilization;
  }

  //------------------------------------------------------------------------------
  public void sizeOrderListPairMeasure() {
    for (int i = 0; i < this.listPairMeasure.size(); i++) {
      for (int j = 0; j < this.listPairMeasure.size(); j++) {
        if (this.listPairMeasure.get(i).getSize() <
            this.listPairMeasure.get(j).getSize()) {
          PairMeasure aux = this.listPairMeasure.get(i);
          this.listPairMeasure.set(i, this.listPairMeasure.get(j));
          this.listPairMeasure.set(j, aux);
        }
      }
    }
  }

  //------------------------------------------------------------------------------
  /**
   * reiniciar todas metricas
   */
  public void reStart() {
    this.sumOfRestorationTime = 0;
    this.numGeneratedReq = 0;
    this.numGeneratedReq = 0;
    this.numBlocking = 0;
    this.sumOfUtilization = 0;
    this.numBlockingAbsenceBackupProtection = 0;
    this.allSizeOfPrimaryBlockedReq = 0;
    this.allSizeOfPrimaryAcceptedReq = 0;
    this.allSizeOfBackupBlockedReq = 0;
    this.allSizeOfBackupAcceptedReq = 0;
    //para reiniciar o calculo de utilizacao do link
    for (int i = 0; i < this.linkList.size(); i++) {
      this.linkList.get(i).reStartUtilization();
    }

  }

  //------------------------------------------------------------------------------
  public void sumRestorability(double d) {
    this.sumRestorability = this.sumRestorability + d;
  }

  //------------------------------------------------------------------------------
  /**
   * CalcSumUtilizationPerLink
   */
  public void calcSumUtilizationPerLink() {
    for (int i = 0; i < linkList.size(); i++) {
      Link aux = linkList.get(i);
      aux.calcSumUtilization();
    }
  }

  //------------------------------------------------------------------------------
  /**
   * atualiza o wCMaximumUtilization de cada Node
   */
  public void verifyMaximumWCUtilization(Vector<Node> nodes) {
    for (int i = 0; i < listNodeMeasure.size(); i++) {
      if (nodes.get(i).getWcBank() != null) {
        listNodeMeasure.get(i).updateMaximumWCUtilization(nodes.get(i).
            getWcBank().
            getNumberUseWc());
      }
    }
  }

  //------------------------------------------------------------------------------  /**
   public void calculateSumMaximumWCUtilization() {
     this.sumMaximumWCUtilization = 0;
     for (int i = 0; i < this.listNodeMeasure.size(); i++) {
       NodeMeasure aux = this.listNodeMeasure.get(i);
       this.sumMaximumWCUtilization += aux.getMaximumWCUtilization();
     }
   }

  //------------------------------------------------------------------------------  /**
   /**
    * atualiza seus resultados com os resultados de measurements
    * @param measurements Measurements
    */
   public void copy(Measurements measurements) {
     this.numBlocking = measurements.numBlocking;
     this.numBlockingAbsenceBackupProtection = measurements.
         numBlockingAbsenceBackupProtection;
     this.numGeneratedReq = measurements.numGeneratedReq;
     this.numGeneratedFailure = measurements.numGeneratedFailure;
     this.sumOfUtilization = measurements.sumOfUtilization;
     this.sumRestorability = measurements.sumRestorability;
     this.linkList = measurements.linkList;
     this.listLinkMeasure = measurements.listLinkMeasure;
     this.listNodeMeasure = measurements.listNodeMeasure;
     this.listPairMeasure = measurements.listPairMeasure;
     this.allSizeOfPrimaryBlockedReq = measurements.allSizeOfPrimaryBlockedReq;
     this.allSizeOfPrimaryAcceptedReq = measurements.allSizeOfBackupAcceptedReq;
     this.allSizeOfBackupBlockedReq = measurements.allSizeOfBackupBlockedReq;
     this.allSizeOfBackupAcceptedReq = measurements.allSizeOfBackupAcceptedReq;
   }

//------------------------------------------------------------------------------
  /**
   * setListLinkMeasure
   * @param listLinkMeasure Vector
   */
  public void setListLinkMeasure(Vector<LinkMeasure> listLinkMeasure) {
    this.listLinkMeasure = listLinkMeasure;
  }

  //------------------------------------------------------------------------------
  /**
   * setListNodeMeasure
   * @param listNodeMeasure Vector
   */
  public void setListNodeMeasure(Vector<NodeMeasure> listNodeMeasure) {
    this.listNodeMeasure = listNodeMeasure;
  }

//------------------------------------------------------------------------------
  /**
   * setListPairMeasure
   * @param listPairMeasure Vector
   */
  public void setListPairMeasure(Vector<PairMeasure> listPairMeasure) {
    this.listPairMeasure = listPairMeasure;
  }

  /**
   * sumRestorationTime
   */
  public void sumRestorationTime(double rTime) {
    this.sumOfRestorationTime = this.sumOfRestorationTime + rTime;

  }

}
