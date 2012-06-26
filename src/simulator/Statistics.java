package simulator;

import java.util.Hashtable;

public class Statistics {
  private Statistics(){

  }
  //------------------------------------------------------------------------------
  /**
   * Calcula o nivel de justica dos valores
   * @param values double[]
   * @return double nivel de justica
   */
  public static double fairness(double[]values) {
    double numPairs = values.length;
    double media = 0;
    //deslocando o intervalo[0,1]->[1,2]
    for (int i = 0; i < numPairs; i++) {
      values[i]=values[i]+1;
    }
    //calculando a media
    for (int i = 0; i < numPairs; i++) {
      media += values[i];
    }
    media = media / numPairs;
    //calculando a soma dos Xi/media...
    double sumXiDivMedia = 0;
    for (int i = 0; i < numPairs; i++) {
      sumXiDivMedia += values[i] / media;
    }
    //numerador
    double numerador = Math.pow(sumXiDivMedia, 2);
    //calculando a soma dos quadrados de Xi/media
    double sumXiDivMedia2 = 0;
    for (int i = 0; i < numPairs; i++) {
      sumXiDivMedia2 +=
          Math.pow(values[i] / media, 2);
    }
    //denominador
    double denominador = numPairs * sumXiDivMedia2;

    return numerador / denominador;
  }

  //------------------------------------------------------------------------------
  /**
   * Retorna o valor t da Tabela T-Student
   * Referente ao Nivel de Confianca e o Grau de liberdade(n-1)
   * @param gl double Grau de liberdade
   * @param ns double Nivel de Significancia
   * @return double t
   */
  public static double getTStudent(double gl, double ns) {
    Hashtable<String,Double> tStudent = new Hashtable<String,Double>();
    //("grau de liberdade,nivel de significancia",t)
    //2 replicações
    tStudent.put("1.0,0.01", 63.657);
    tStudent.put("1.0,0.03", 21.205);
    tStudent.put("1.0,0.05", 12.706);
    //3 replicações
    tStudent.put("2.0,0.05", 4.303);
    //4 replicações
    tStudent.put("3.0,0.05", 3.182);
    //5 replicações
    tStudent.put("4.0,0.05", 2.776);
    //6 replicações
    tStudent.put("5.0,0.05", 2.571);
    //7 replicações
    tStudent.put("6.0,0.05", 2.447);
    //8 replicações
    tStudent.put("7.0,0.05", 2.365);
    //9 replicações
    tStudent.put("8.0,0.05", 2.306);
    //10 replicações
    tStudent.put("9.0,0.01", 3.250);
    tStudent.put("9.0,0.05", 2.262);
    //25 replicações
    tStudent.put("24.0,0.05", 2.064);

    String key = gl+","+ns;
    if (tStudent.get(key)!=null)
      return ((double)tStudent.get(key));
    //System.out.println(".......amostras/nivel de significancia nao tabelado! t = 1.96.......");
    return 1.096;//NS=0.05,AMOSTRAS GRANDES

  }
  //------------------------------------------------------------------------------
    /**
     * Calcula o desvio padrão dos amostras
     * @param values double[] valores das amostras
     * @param media double media das amostras
     * @return double s
     */
    public static double calculateDesvioPadrao(double[] values, double media) {
      double n = values.length; //amostras
      double s; //desvio padrao
      double sumDesvio = 0; //soma dos desvios relativos a media

      /**
       * Soma das diferencas entre as amostras e a media elevado ao quadrado
       */
      for (int i = 0; i < n; i++) {
        sumDesvio += Math.pow(values[i] - media, 2);
      }
      double variancia = sumDesvio / (n - 1);
      s = Math.sqrt(variancia);

      return s;
  }
//------------------------------------------------------------------------------
  /**
   * Calcula o erro para o intervalo de confiança da media
   * @param values double[] valores das amostras
   * @param media double media das amostras
   * @param ns double Nivel de Significancia
   * @return double erro
   */
  public static double calculateError(double[] values, double media, double ns) {
    double n = values.length; //amostras
    double t = getTStudent(n-1,ns);
    double s; //desvio padrao
    s=Statistics.calculateDesvioPadrao(values,media);

    double erro = t * s / Math.sqrt(n);
    //ajuste para validacao
    erro*=2;
    return erro;
  }

}
