package wavelengthAssignment;

import network.*;

public class FairFit extends WaveAlgo {

  /**
   * método invocado pelas requisições
   *Dado uma lista de comprimentos de onda livres Comprimentos de onda livres continuos.cenario sem conversao
   *Seta em waveList quais os comprimentos de onda serao utilizados
   * @param waveList int[] comprimentos de onda serao utilizados
   * @param freeWave int[] lista de comprimentos de onda livres
   * @param category int categoria do par
   * @return boolean
   */
  public boolean setWaveAssignment(int[]waveList, int[] freeWave, int category, boolean controlChannel) {
    int index;
    index = 0;
    double utilization = this.mesh.calculateUtilization();
    int threshold=40;

   if (utilization > 0.46){
     if (category == 1)
       threshold = 20;
     if (category == 2)
       threshold = 30;
     if (category == 3)
       threshold = 40;
   }



    //seta o comprimento de onda a ser utilizado por uma requisição
    //escolhendo o 1º comprimento de onda da lista de comprimento de onda livre
    int reserved = controlChannel ? 1 : 0; //seleciona último comprimento de onda livre para ser canal de controle
    for (int i = 0; i < waveList.length - reserved; i++) {
      if (freeWave[index]<=threshold)
      waveList[i]= freeWave[index];
      else
        return false;
    }
    return true;
  }

}
