package simulator;

import java.io.FileNotFoundException;
import java.util.Vector;

import measurement.LinkMeasure;
import measurement.Measurements;
import network.Node;
import root.Printer;
import routing.Route;

public class SimulationManagement extends Thread {
    
    private Simulator simulator;
    private double significativeLevel;
    private boolean failure;
    private double fixLinkRate;
    private double occurRate;
    private String fileRes;
    private Object gui;
    
    private Vector<Vector<Simulation>> simulations;
    /**
     * armazena os resultados para todos os pontos com todas replicações
     */
    private Vector<Vector<Measurements>> mainMeasurements;
    
    public SimulationManagement(Vector<Vector<Simulation>> simulations) {
        fileRes = new String("files\tmp\results.res");
        this.simulations = simulations;
        //um Measurements para cada simulacao
        this.mainMeasurements = new Vector<Vector<Measurements>> ();
    }
    
    //------------------------------------------------------------------------------
    public void setFailure(boolean failure) {
        this.failure = failure;
    }
    //------------------------------------------------------------------------------
    public void setFixLinkRate(double fixLinkRate) {
        this.fixLinkRate = fixLinkRate;
    }
    //------------------------------------------------------------------------------
    public void setOccurRate(double occurRate) {
        this.occurRate = occurRate;
    }
    //------------------------------------------------------------------------------
    public void setFileRes(String pathRes) {
        this.fileRes = pathRes;
    }
    //------------------------------------------------------------------------------
    public void setGui(Object gui) {
        this.gui = gui;
    }
    
    //------------------------------------------------------------------------------
    public void setSignificativeLevel(double level) {
        this.significativeLevel = level;
    }
    
    //------------------------------------------------------------------------------
    public void run() {
        //loop para simular todos os pontos
        for (int i = 0; i < this.simulations.size(); i++) {
            //para guardar os resultados de todas replicações deste ponto...
            this.mainMeasurements.add(new Vector<Measurements> ());
            //loop para simular todas replicações do ponto
            for (int j = 0; j < this.simulations.get(i).size(); j++) {
                //simulação atual
                Simulation actualSimulation = this.simulations.get(i).get(j);
                //atualiza a simulação no simulator
                this.simulator = new Simulator(actualSimulation);
                //inicia simulação e guarda resultados no mainMeasurements
                this.mainMeasurements.get(i).add(this.simulator.start(failure, fixLinkRate, occurRate));
                
                //imprimindo progresso da simulacao (%)
                int percent =  ( ( (i * this.simulations.get(i).size()) + (j + 1)) *
                        100) /
                        (this.simulations.size() *
                        this.simulations.get(i).size());
                
                System.out.print(percent + "%..");
                if (gui instanceof gui.Main )
                    ((gui.Main) gui).progressRefresh(percent,mainMeasurements);
                else if (gui instanceof gui.JanelaValidacao )
                    ((gui.JanelaValidacao) gui).progressRefresh(percent);
            }
        } 
        System.out.println();
        try {
            printResults(fileRes);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        if (gui instanceof gui.Main )
            ((gui.Main) gui).simFinished();
        else if (gui instanceof gui.JanelaValidacao )
            ((gui.JanelaValidacao) gui).simFinished();
        
    }
   
    /**
     * exibe todos os resultados e imprime no arquivo
     * @param file String
     * @throws FileNotFoundException
     */
    public void printResults(String file) throws FileNotFoundException {
        Printer out = new Printer(file,false);
        int type = this.getSimulator().getSimulation().getSimulationType();
//..............................................................................
        //######################## GERAL ########################\\
        //Pb Bloqueio geral
        this.printPbBlocking(out);
        out.println();
        
        //Utilização Rede
        this.printUtilization(out);
        out.println();
        
        //tamanho medio das requisições atendidas.
        this.printAverageSizeOfPrimaryAcceptedReq(out);
        out.println();
        
        //tamanho medio das requisições bloqueidas.
        this.printAverageSizeOfBlockedReq(out);
        out.println("outras...");
        //######################## PROTEÇÃO ########################\\
        if ( (type == 3) || (type == 4) || (type == 5)) {
            out.println();
            //Pb Bloqueio por ausencia de backup
            this.printBlockingAbsenceBackupProtection(out);
            out.println();
            
            //tamanho medio das requisições de backup atendidas.
            this.printAverageSizeOfBackupAcceptedReq(out);
            out.println();
            
            //tamanho medio das requisições de backup  bloqueidas.
            this.printAverageSizeOfBackupBlockedReq(out);
        }
        
        //########################## GERAL OUTRAS ##########################\\
        //Pb restaurabilidade
        this.printPbRestorability(out);
        
        //Utilização por link
        this.printUtilizationPerWavelengh(out);
        
        //Utilização por link
        this.printUtilizationPerLink(out);
        
        //para cada link exibe as rotas que utilizam este link
        this.printRoutesPerLink(out);
        
        //falhas por link
        this.printFailurePerLink(out);
        
        //Pb Bloqueio por Par(um par por linha)
        this.printPbBlockingPerPair(out);
        
        //numero de vezes que o Par foi gerado (um par por linha)
        this.printReqPerPair(out);
//..............................................................................
        out.closeFile();
    }
    
    //------------------------------------------------------------------------------
    /**
     * escreve no arquivo e na console o num de vezes que um par foi gerado.
     * carga 1 ReqPair 1 ReqPair 2 ReqPair n
     * carga n ReqPair 1 ReqPair 2 ReqPair n
     * @param out Printer
     */
    private void printReqPerPair(Printer out) {
        out.println("connections number per pair");
        //Grava os valores da carga
        this.printArrivedRate(out);
        
        //numero de pares
        int numOfPair = mainMeasurements.get(0).get(0).getListPairMeasure().size();
        //percorre a lista de pares
        for (int p = 0; p < numOfPair; p++) {
            //nome do par
            out.print(mainMeasurements.get(0).get(0).getListPairMeasure().get(p).
                    getName() + "\t");
            //percorre a carga para o par p
            for (int c = 0; c < mainMeasurements.size(); c++) {
                double sumReqPair = 0;
                int numReplication = mainMeasurements.get(c).size();
                //percorre as replicações do par (p)
                for (int r = 0; r < numReplication; r++) {
                    //soma as requisições de todas replicações do par p para carga c
                    sumReqPair +=
                            mainMeasurements.get(c).get(r).getListPairMeasure().get(p).
                            getNumberReq();
                }
                //calcula o numero medio de requisições do par p para carga c
                double averageReq = sumReqPair / numReplication;
                //grava o numero medio de requisições do par p
                out.print(averageReq + "\t");
            }
            out.println();
        }
        out.println();
    }
    
    //------------------------------------------------------------------------------
    /**
     * escreve no arquivo e na console a Pb de Bloqueio por ausencia de backup.
     * carga 1 replicação 1 replicaçao n media
     * carga n replicação 1 replicaçao n media
     * @param out Printer
     */
    private void printBlockingAbsenceBackupProtection(Printer out) {
        out.println("Blocking absence backup protection probability ");
        // gravar o numero das replicacoes
        this.printNumberReplications(out);
        
        //percorre a carga
        for (int i = 0; i < mainMeasurements.size(); i++) {
            double sumPB = 0;
            //exibe a carga i
            out.print(mainMeasurements.get(i).get(0).getArrivedRate() + "\t");
            int numReplication = mainMeasurements.get(i).size();
            //para calculo do erro
            double[] pb = new double[numReplication];
            //percorre as replicações da carga i
            for (int j = 0; j < numReplication; j++) {
                pb[j] = mainMeasurements.get(i).get(j).
                        getPbBlockingAbsenceBackupProtection();
                //Pb Bloqueio replicações
                out.print(pb[j] + "\t");
                //soma das replicações
                sumPB += pb[j];
            }
            double mediaPB = sumPB / numReplication;
            //Pb Bloqueio Geral
            out.print(mediaPB + "\t");
            //erro para o intervalo de confiança...............
            double erro = Statistics.calculateError(pb, mediaPB,
                    this.significativeLevel);
            out.println(erro + "\t");
            //intervalo de confiança...............
        }
    }
    
    //------------------------------------------------------------------------------
    /**
     * escreve no arquivo e na console a Pb de Bloqueio.
     * carga 1 replicação 1 replicaçao n media
     * carga n replicação 1 replicaçao n media
     * @param out Printer
     */
    private void printPbBlocking(Printer out) {
        out.println("Blocking probability");
        // gravar o numero das replicacoes
        this.printNumberReplications(out);
        
        //percorre a carga
        for (int i = 0; i < mainMeasurements.size(); i++) {
            double sumPB = 0;
            //exibe a carga i
            out.print(mainMeasurements.get(i).get(0).getArrivedRate() + "\t");
            int numReplication = mainMeasurements.get(i).size();
            //para calculo do erro
            double[] pb = new double[numReplication];
            //percorre as replicações para carga i
            for (int j = 0; j < numReplication; j++) {
                //Pb Bloqueio replicações
                out.print(mainMeasurements.get(i).get(j).getPb() + "\t");
                //soma das replicações
                pb[j] = mainMeasurements.get(i).get(j).getPb();
                sumPB += pb[j];
            }
            
            double mediaPB = sumPB / numReplication;
            //Pb Bloqueio Geral
            out.print(mediaPB + "\t");
            //TESTE...
      /* pb = new double[25];
       int j = 0;
       pb[j++] = 240;
       pb[j++] = 99;
       pb[j++] = 322;
       pb[j++] = 230;
       pb[j++] = 230;
       pb[j++] = 300;
       pb[j++] = 220;
       pb[j++] = 280;
       pb[j++] = 240;
       pb[j++] = 100;
       pb[j++] = 321;
       pb[j++] = 230;
       pb[j++] = 180;
       pb[j++] = 350;
       pb[j++] = 220;
       pb[j++] = 280;
       pb[j++] = 240;
       pb[j++] = 104;
       pb[j++] = 317;
       pb[j++] = 230;
       pb[j++] = 280;
       pb[j++] = 250;
       pb[j++] = 220;
       pb[j++] = 280;
       pb[j++] = 249.5;*/
            //...TESTE
            //erro para o intervalo de confiança...............
            double erro = Statistics.calculateError(pb, mediaPB,
                    this.significativeLevel);
            out.println(erro + "\t");
            //intervalo de confiança...............
        }
    }
    
    //------------------------------------------------------------------------------
    /**
     * escreve no arquivo e na console a utilização geral da rede.
     * carga 1 replicação 1 replicaçao n media
     * carga n replicação 1 replicaçao n media
     * @param out Printer
     */
    private void printUtilization(Printer out) {
        out.println("Network utilization ");
        // gravar o numero das replicacoes
        this.printNumberReplications(out);
        //percorre a carga
        for (int i = 0; i < mainMeasurements.size(); i++) {
            double sumUtil = 0;
            //exibe Carga
            out.print(mainMeasurements.get(i).get(0).getArrivedRate() + "\t");
            int numReplication = mainMeasurements.get(i).size();
            //para calculo do erro
            double[] pbUtil = new double[numReplication];
            for (int j = 0; j < numReplication; j++) {
                //Util replicações
                pbUtil[j] = mainMeasurements.get(i).get(j).getUtilization();
                out.print(pbUtil[j] + "\t");
                sumUtil += pbUtil[j];
            }
            //PbGeral
            double mediaPB = sumUtil / numReplication;
            out.print(mediaPB + "\t");
            //erro para o intervalo de confiança...............
            double erro = Statistics.calculateError(pbUtil, mediaPB,
                    this.significativeLevel);
            out.println(erro + "\t");
            //intervalo de confiança...............
        }
    }
    
    //------------------------------------------------------------------------------
    /**
     * escreve no arquivo e na console a Pb Bloqueio medio por Par.
     * carga 1 PbPair 1 PbPair 2 PbPair n
     * carga n PbPair 1 PbPair 2 PbPair n
     * @param out Printer
     */
    private void printPbBlockingPerPair(Printer out) {
        out.println("Blocking per pair probability");
        out.print("size\tcategory\t");
        //Grava os valores da carga
        this.printArrivedRate(out);
        
        //numero de pares
        int numOfPair = mainMeasurements.get(0).get(0).getListPairMeasure().size();
        //para calculo da justiça
        double[][] average = new double[mainMeasurements.size()][numOfPair];
        //percorre a lista de pares
        
        for (int p = 0; p < numOfPair; p++) {
            //exibe o tamanho das requisições feitas pelo par p
            out.print(mainMeasurements.get(0).get(0).getListPairMeasure().get(p).
                    getSize() + "\t");
            //exibe a categoria do par p
            out.print(mainMeasurements.get(0).get(0).getListPairMeasure().get(p).
                    getCategory() + "\t");
            //exibe o nome do par p
            out.print(mainMeasurements.get(0).get(0).getListPairMeasure().get(p).
                    getName() + "\t");
            //percorre a carga
            for (int c = 0; c < mainMeasurements.size(); c++) {
                double sumBlockPair = 0;
                int numReplication = mainMeasurements.get(c).size();
                //percorre as replicações do par (p) para a carga c
                for (int r = 0; r < numReplication; r++) {
                    //soma bloqueio
                    sumBlockPair += mainMeasurements.get(c).get(r).getListPairMeasure().
                            get(p).getPbBlocking();
                }
                //calcula o bloqueio medio do par
                double averageBlock = sumBlockPair / numReplication;
                //grava o bloqueio medio do par
                out.print(averageBlock + "\t");
                average[c][p] = averageBlock;
            }
            out.println();
        }
        // Calculo do nivel de justica.................
        out.print("\t\tJustica\t");
        for (int c = 0; c < mainMeasurements.size(); c++) {
            out.print(Statistics.fairness(average[c]) + "\t");
        }
        out.println();
    }
    
//------------------------------------------------------------------------------
    /**
     * escreve no arquivo e na console a Utilizacao media por Link.
     * carga 1 utilLink 1 utilLink 2 utilLink n
     * carga n utilLink 1 utilLink 2 utilLink n
     * @param out Printer
     */
    private void printUtilizationPerLink(Printer out) {
        out.println("Utilization per link");
        //grava o nome dos links
        this.printLinkName(out);
        //numero de links
        int numLinks = mainMeasurements.get(0).get(0).getListLinkMeasure().size();
        //percorre a carga
        for (int c = 0; c < mainMeasurements.size(); c++) {
            out.print(mainMeasurements.get(c).get(0).getArrivedRate() + "\t");
            //percorre a lista de links da carga (c)
            for (int l = 0; l < numLinks; l++) {
                double sumUtiliLink = 0;
                int numReplication = mainMeasurements.get(c).size();
                //percorre as replicações do Link (l)
                for (int r = 0; r < numReplication; r++) {
                    //soma utilizacao (replicações)
                    sumUtiliLink += mainMeasurements.get(c).get(r).getListLinkMeasure().
                            get(l).
                            getUtilization();
                }
                
                //calcula a a utilização media do link
                double averageUtil = sumUtiliLink / numReplication;
                //grava a utilização media do link
                out.print(averageUtil + "\t");
            }
            out.println();
        }
        out.println();
    }
    //------------------------------------------------------------------------------
    /**
     * escreve no arquivo e na console a Utilizacao media por comprimento de ondak.
     * carga 1 utilLbda 1 utilLbda 2 utilLbda n
     * carga n utilLbda 1 utilLbda 2 utilLbda n
     * @param out Printer
     */
    private void printUtilizationPerWavelengh(Printer out) {
        out.println("Utilization per wavelengh");
        //grava o indice dos comprimentos de onda
        this.printIndexOfWavelengh(out);//!!!!!!
        //numero de comprimentos de onda
        int numWaves = mainMeasurements.get(0).get(0).getLinkList().get(0).getNumWave();
        //percorre a carga
        for (int c = 0; c < mainMeasurements.size(); c++) {
            out.print(mainMeasurements.get(c).get(0).getArrivedRate() + "\t");
            //percorre a lista de links de comprimentos de onda da carga (c)
            for (int w = 0; w < numWaves; w++) {
                double sumWaveUtil = 0;
                int numReplication = mainMeasurements.get(c).size();
                //percorre as replicações da carga (c)
                for (int r = 0; r < numReplication; r++) {
                    //soma utilizacao do comprimento de onda (replicações)
                    sumWaveUtil += mainMeasurements.get(c).get(r).
                            getWavelenghUtilization()[w];
                }
                //calcula a a utilização media do comprimento de onda
                double averageUtil = sumWaveUtil / numReplication;
                //grava a utilização media do comprimento de onda
                out.print(averageUtil + "\t");
            }
            out.println();
        }
        out.println();
    }
    //----------------------------------------------------------------------------
    /**
     * escreve no arquivo e na console as rotas que utilizam um determinado ink.
     * @param out Printer
     */
    private void printRoutesPerLink(Printer out) {
        out.println("Routes per link");
        out.println("Link\tRoutes number\tRoutes");
        //uma simulação qualquer (primeiro ponto e primeira replicação)
        Measurements anyMeasure = mainMeasurements.get(0).get(0);
        //numero de links
        int numLinks = anyMeasure.getListLinkMeasure().size();
        //percorre a lista de links(Measure) da primeira simulação
        for (int l = 0; l < numLinks; l++) {
            LinkMeasure linkMeasure = anyMeasure.getListLinkMeasure().get(l);
            //nome do link
            out.print(linkMeasure.getName() + "\t");
            Vector<Route> routes = linkMeasure.getRoutes();
            //numero de rotas que utilizam este link
            out.print(routes.size()+"\t");
            //percorre as rotas do link l
            for (int i = 0; i < routes.size(); i++) {
                Vector<Node> nodeList = routes.get(i).getNodeList();
                for (int n = 0; n < nodeList.size(); n++) {
                    out.print(nodeList.get(n).getName() + ";");
                }
                out.print("\t");
            }
            out.println();
        }
        out.println();
    }
    
//------------------------------------------------------------------------------
    /**
     * escreve no arquivo e na console o num de falhas por Link.
     * carga 1 failLink 1 failLink 2 failLink n
     * carga n failLink 1 failLink 2 failLink n
     * @param out Printer
     */
    private void printFailurePerLink(Printer out) {
        out.println("Failure per link");
        //grava o nome dos links
        this.printLinkName(out);
        //numero de links
        int numLinks = mainMeasurements.get(0).get(0).getListLinkMeasure().size();
        //percorre a carga
        for (int c = 0; c < mainMeasurements.size(); c++) {
            //exibe a carga c
            out.print(mainMeasurements.get(c).get(0).getArrivedRate() + "\t");
            //percorre a lista de links da carga (c)
            for (int l = 0; l < numLinks; l++) {
                double sumFailLink = 0;
                int numReplication = mainMeasurements.get(c).size();
                //percorre as replicações do Link (l)
                for (int r = 0; r < numReplication; r++) {
                    //soma as replicações
                    sumFailLink +=
                            mainMeasurements.get(c).get(r).getListLinkMeasure().get(l).
                            getNumberFailure();
                }
                
                //calcula o numero media de falhas no link
                double averageFail = sumFailLink / numReplication;
                //grava o numero media de falhas no link
                out.print(averageFail + "\t");
            }
            out.println();
        }
        out.println();
    }
    
    //------------------------------------------------------------------------------
    /**
     * escreve no arquivo e na console a Pb de restaurabilidade.
     * carga 1 replicação 1 replicaçao n media
     * carga n replicação 1 replicaçao n media
     * @param out Printer
     */
    private void printPbRestorability(Printer out) {
        out.println("Restorability probability");
        // gravar o numero das replicacoes
        this.printNumberReplications(out);
        //percorre a carga
        for (int i = 0; i < mainMeasurements.size(); i++) {
            double sumPR = 0;
            //exibe a carga i
            out.print(mainMeasurements.get(i).get(0).getArrivedRate() + "\t");
            int numReplication = mainMeasurements.get(i).size();
            //para calculo do erro
            double[] pb = new double[numReplication];
            //percorre as replicações da carga i
            for (int j = 0; j < numReplication; j++) {
                pb[j] = mainMeasurements.get(i).get(j).getPbRestorability();
                out.print(pb[j] + "\t");
                //soma Pb replicações
                sumPR += pb[j];
            }
            double mediaPB = sumPR / numReplication;
            //Pb Bloqueio Geral
            out.print(mediaPB + "\t");
            //erro para o intervalo de confiança...............
            double erro = Statistics.calculateError(pb, mediaPB,
                    this.significativeLevel);
            out.println(erro + "\t");
            //intervalo de confiança...............
        }
        out.println();
    }
    
    //------------------------------------------------------------------------------
    /**
     * escreve no arquivo e na console o tamanho medio das requisições atendidas.
     * carga 1 media
     * carga n media
     * @param out Printer
     */
    private void printAverageSizeOfPrimaryAcceptedReq(Printer out) {
        out.println("Average length of established connections");
        // gravar o numero das replicacoes
        this.printNumberReplications(out);
        //percorre a carga
        for (int i = 0; i < mainMeasurements.size(); i++) {
            double sumAverageSize = 0;
            //exibe a carga i
            out.print(mainMeasurements.get(i).get(0).getArrivedRate() + "\t");
            int numReplication = mainMeasurements.get(i).size();
            //para calculo do erro
            double[] pbAS = new double[numReplication];
            //percorre as replicações para carga i
            for (int j = 0; j < numReplication; j++) {
                pbAS[j] = mainMeasurements.get(i).get(j).
                        getAverageSizeOfPrimaryAcceptedReq();
                out.print(pbAS[j] + "\t");
                //soma do tamanho medio das replicações
                sumAverageSize += pbAS[j];
            }
            //tamanho medio
            double mediaAS = sumAverageSize / numReplication;
            out.print(mediaAS + "\t");
            //erro para o intervalo de confiança...............
            double erro = Statistics.calculateError(pbAS, mediaAS,
                    this.significativeLevel);
            out.println(erro + "\t");
            //intervalo de confiança...............
        }
    }
    
    //------------------------------------------------------------------------------
    /**
     * escreve no arquivo e na console o tamanho medio das requisições bloqueidas.
     * carga 1 media
     * carga n media
     * @param out Printer
     */
    private void printAverageSizeOfBlockedReq(Printer out) {
        out.println("Average length of blocked connections");
        // gravar o numero das replicacoes
        this.printNumberReplications(out);
        //percorre a carga
        for (int i = 0; i < mainMeasurements.size(); i++) {
            double sumAverageSize = 0;
            //exibe a carga i
            out.print(mainMeasurements.get(i).get(0).getArrivedRate() + "\t");
            int numReplication = mainMeasurements.get(i).size();
            //para calculo do erro
            double[] pbAS = new double[numReplication];
            //percorre as replicações para carga i
            for (int j = 0; j < numReplication; j++) {
                //soma do tamanho medio das replicações
                pbAS[j] = mainMeasurements.get(i).get(j).
                        getAverageSizeOfPrimaryBlockedReq();
                out.print(pbAS[j] + "\t");
                sumAverageSize += pbAS[j];
            }
            //tamanho medio
            double mediaAS = sumAverageSize / numReplication;
            out.print(mediaAS + "\t");
            //erro para o intervalo de confiança...............
            double erro = Statistics.calculateError(pbAS, mediaAS,
                    this.significativeLevel);
            out.println(erro + "\t");
            //intervalo de confiança...............
        }
    }
    
    //------------------------------------------------------------------------------
    /**
     * escreve no arquivo e na console o tamanho medio das requisições de backup atendidas.
     * carga 1 media
     * carga n media
     * @param out Printer
     */
    private void printAverageSizeOfBackupAcceptedReq(Printer out) {
        out.println("Average length of established backup connections");
        // gravar o numero das replicacoes
        this.printNumberReplications(out);
        //percorre a carga
        for (int i = 0; i < mainMeasurements.size(); i++) {
            double sumAverageSize = 0;
            //exibe a carga i
            out.print(mainMeasurements.get(i).get(0).getArrivedRate() + "\t");
            int numReplication = mainMeasurements.get(i).size();
            //para calculo do erro
            double[] pbAS = new double[numReplication];
            //percorre as replicações para carga i
            for (int j = 0; j < numReplication; j++) {
                pbAS[j] = mainMeasurements.get(i).get(j).
                        getAverageSizeOfBackupAcceptedReq();
                out.print(pbAS[j] + "\t");
                //soma do tamanho medio das replicações
                sumAverageSize += pbAS[j];
            }
            //tamanho medio
            double mediaAS = sumAverageSize / numReplication;
            out.print(mediaAS + "\t");
            //erro para o intervalo de confiança...............
            double erro = Statistics.calculateError(pbAS, mediaAS,
                    this.significativeLevel);
            out.println(erro + "\t");
            //intervalo de confiança...............
        }
    }
    
//------------------------------------------------------------------------------
    /**
     * escreve no arquivo e na console o tamanho medio das requisições de backup bloqueidas.
     * carga 1 media
     * carga n media
     * @param out Printer
     */
    private void printAverageSizeOfBackupBlockedReq(Printer out) {
        out.println("Average length of blocked backup connections");
        // gravar o numero das replicacoes
        this.printNumberReplications(out);
        //percorre a carga
        for (int i = 0; i < mainMeasurements.size(); i++) {
            double sumAverageSize = 0;
            //exibe a carga i
            out.print(mainMeasurements.get(i).get(0).getArrivedRate() + "\t");
            int numReplication = mainMeasurements.get(i).size();
            //para calculo do erro
            double[] pbAS = new double[numReplication];
            //percorre as replicações para carga i
            for (int j = 0; j < numReplication; j++) {
                pbAS[j] = mainMeasurements.get(i).get(j).
                        getAverageSizeOfBackupBlockedReq();
                //soma do tamanho medio das replicações
                out.print(pbAS[j] + "\t");
                sumAverageSize += pbAS[j];
            }
            //tamanho medio
            double mediaAS = sumAverageSize / numReplication;
            out.print(mediaAS + "\t");
            //erro para o intervalo de confiança...............
            double erro = Statistics.calculateError(pbAS, mediaAS,
                    this.significativeLevel);
            out.println(erro + "\t");
            //intervalo de confiança...............
        }
    }
    
    //------------------------------------------------------------------------------
    /**
     * gravar o numero das replicacoes
     * @param out Printer
     */
    private void printNumberReplications(Printer out) {
        out.print("Traffic load in Erlang\t");
        for (int j = 1; j <= mainMeasurements.get(0).size(); j++) {
            out.print("Reply " + j + "\t");
        }
        out.print("Average\t");
        out.println("Error\t");
    }
    
//------------------------------------------------------------------------------
    /**
     * Grava o nome dos pares, percorrendo a lista de pares
     *
     * @param out Printer
     */
    private void printPairName(Printer out) {
        Measurements anyMeasure = mainMeasurements.get(0).get(0);
        for (int p = 0; p < anyMeasure.getListPairMeasure().size(); p++) {
            out.print("\t" + anyMeasure.getListPairMeasure().get(p).getName());
        }
        out.println();
    }
    
    //------------------------------------------------------------------------------
    /**
     * Grava os valores das cargas
     * @param out Printer
     */
    private void printArrivedRate(Printer out) {
        for (int c = 0; c < mainMeasurements.size(); c++) {
            out.print("\t" + mainMeasurements.get(c).get(0).getArrivedRate());
        }
        out.println();
    }
    
    //------------------------------------------------------------------------------
    /**
     * Grava o nome dos links, percorrendo a lista de links
     * @param out Printer
     */
    private void printLinkName(Printer out) {
        Measurements anyMeasure = mainMeasurements.get(0).get(0);
        for (int l = 0; l < anyMeasure.getListLinkMeasure().size(); l++) {
            out.print("\t" + anyMeasure.getListLinkMeasure().get(l).getName());
        }
        out.println();
    }
    //------------------------------------------------------------------------------
    /**
     * Grava o indice dos comprimentos de onda
     * @param out Printer
     */
    private void printIndexOfWavelengh(Printer out) {
        Measurements anyMeasure = mainMeasurements.get(0).get(0);
        int waveNumber = anyMeasure.getLinkList().get(0).getNumWave();
        for (int w = 0; w < waveNumber; w++) {
            out.print("\tWave " + w);
        }
        out.println();
    }
//------------------------------------------------------------------------------
    public Vector<Vector<Measurements>> getMeasurements() {
        return this.mainMeasurements;
    }
    
//------------------------------------------------------------------------------
    public Simulator getSimulator() {
        return simulator;
    }
    
}
