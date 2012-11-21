package wavelengthAssignment;

import network.*;

public class FirstFit extends WaveAlgo {

  /**
   * método invocado pelas requisições
   *Dado uma lista de comprimentos de onda livres Comprimentos de onda livres continuos.cenario sem conversao
   *Seta em waveList quais os comprimentos de onda serao utilizados
   * @param waveList int[] comprimentos de onda serao utilizados
   * @param freeWave int[] lista de comprimentos de onda livres
   * @param category int categoria do par
   * @return boolean
   */
  public boolean setWaveAssignment(int[]waveList, int[] freeWave, int category) {
    int index;
    index = 0;
    //seta o comprimento de onda a ser utilizado por uma requisição
    //escolhendo o 1º comprimento de onda da lista de comprimento de onda livre
    for (int i = 0; i < waveList.length; i++) {
      waveList[i]= freeWave[index];
    }
    return true;
  }

}

