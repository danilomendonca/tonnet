package routing;

import java.util.Vector;

public class Convert {
  private Convert() {
  }

  //----------------------------------------------------------------------------
  /**
   * Converte um numero decimal para um numero binário
   * @param dec O numero decimal para converter
   * @return String contendo o numero binário.
   */
  public static String decToBinary(int dec) {
    String result = "";

    while (dec > 0) {
      result = (dec & 1) + result;
      dec >>= 1;
    }

    return result;
  }

  //----------------------------------------------------------------------------
  /**
   * Converte um numero decimal em um numero binário
   * @param bin O numero binário para converter
   * @return int contendo o numero decimal.
   */
  public static int binaryToDec(String bin) {
    int i, result = 0;

    for (i = 0; i < bin.length(); i++) {
      result <<= 1;
      if (bin.charAt(i) == '1') {
        result++;
      }
    }

    return result;
  }
  //----------------------------------------------------------------------------
   /**
    * retorna todas combinacoes binarias possiveis para a variavel max(000,001,010,100,...)
    * Minimiza o numero de conversores a serem utilizados
    * @param max int maximo de variaveis;
    * @return Vector contendo todas as combinacoes.
    */
   public static Vector<String> binaryCombination(int max){
     //numero maximo de combinacoes possíveis
     int combinationNumber = ( (Double) Math.pow(2, max)).intValue();
     Vector<String> result = new Vector<String>(combinationNumber-1);
   //  result.add("000");
     for (int i = 0; i < combinationNumber; i++) {
       //combinacao i
       String aux = Convert.decToBinary(i);
       //completando com "0" à esquerda
         while (aux.length()<max){
           String tmp = "0";
           tmp+=aux;
           aux=tmp;
         }
         //System.out.println(aux);
       result.add(aux);
     }
     //ordenando as combinacoes pelo menor numero de "1"s
     for (int i = 0; i < result.size(); i++) {
       for (int j = 0; j < result.size(); j++) {
         //numero de "1"s da combinacao i
         int contI = 0;
         for (int c = 0; c < result.get(i).length(); c++) {
           if (result.get(i).charAt(c)=='1')
           contI++;
         }
         //numero de "1"s da combinacao j
         int contJ = 0;
         for (int c = 0; c < result.get(j).length(); c++) {
           if (result.get(j).charAt(c) == '1')
             contJ++;
         }
         if (contI<contJ){
           String aux = result.get(i);
           result.set(i,result.get(j));
           result.set(j,aux);
         }
       }
     }
     return result;
  }
  //----------------------------------------------------------------------------
  /**
   * retorna todas combinacoes binarias possiveis para a variavel max(000,001,010,100,...)
   * Maximiza a continuidade de comprimento de onda nos primeiros segmentos
   * @param max int maximo de variaveis;
   * @return Vector contendo todas as combinacoes.
   */
  public static Vector<String> binaryCombination2(int max){
    //numero maximo de combinacoes possíveis
    int combinationNumber = ( (Double) Math.pow(2, max)).intValue();
    Vector<String> result = new Vector<String>(combinationNumber-1);
  //  result.add("000");
    for (int i = 0; i < combinationNumber; i++) {
      //combinacao i
      String aux = Convert.decToBinary(i);
      //completando com "0" à esquerda
        while (aux.length()<max){
          String tmp = "0";
          tmp+=aux;
          aux=tmp;
        }
        //System.out.println(aux);
      result.add(aux);
    }

    return result;
  }
}
